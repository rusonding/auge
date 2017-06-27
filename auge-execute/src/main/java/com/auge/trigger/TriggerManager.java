package com.auge.trigger;

import com.auge.client.Client;
import com.auge.model.Job;
import com.auge.model.Trigger;
import com.auge.scheduler.JobScheduler;
import com.auge.utils.Props;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by lixun on 2017/6/26.
 */
public class TriggerManager implements TriggerManagerAdapter {
    private static Logger logger = Logger.getLogger(TriggerManager.class);
    private final Object syncObj = new Object();
    private TriggerLoader triggerLoader;
    private long lastRunnerThreadCheckTime = -1;
    private long runnerThreadIdleTime = -1;
    private final TriggerScannerThread runnerThread;
    public static final long DEFAULT_SCANNER_INTERVAL_MS = 60000;

    public TriggerManager(TriggerLoader triggerLoader) {
        this.triggerLoader = triggerLoader;
        long scannerInterval = DEFAULT_SCANNER_INTERVAL_MS;
        runnerThread = new TriggerScannerThread(scannerInterval);
    }

    public long getLastRunnerThreadCheckTime() {
        return lastRunnerThreadCheckTime;
    }

    public void setLastRunnerThreadCheckTime(long lastRunnerThreadCheckTime) {
        this.lastRunnerThreadCheckTime = lastRunnerThreadCheckTime;
    }

    public long getRunnerThreadIdleTime() {
        return runnerThreadIdleTime;
    }

    public void setRunnerThreadIdleTime(long runnerThreadIdleTime) {
        this.runnerThreadIdleTime = runnerThreadIdleTime;
    }

    @Override
    public void start() throws Exception {

        List<Trigger> triggers = triggerLoader.loadTriggers();
        for (Trigger trigger : triggers) {
            runnerThread.addTrigger(trigger);
        }
        runnerThread.start();
    }

    private class TriggerScannerThread extends Thread {
        private BlockingQueue<Trigger> triggers;
        private boolean shutdown = false;
        private final long scannerInterval;

        public TriggerScannerThread(long scannerInterval) {
            triggers = new PriorityBlockingQueue<Trigger>(1, new TriggerComparator());
            this.setName("TriggerRunnerManager-Trigger-Scanner-Thread");
            this.scannerInterval = scannerInterval;
        }

        public void shutdown() {
            logger.error("Shutting down trigger manager thread " + this.getName());
            shutdown = true;
            this.interrupt();
        }

        public void addTrigger(Trigger t) {
            synchronized (syncObj) {
                triggers.add(t);
            }
        }

        public void deleteTrigger(Trigger t) {
            triggers.remove(t);
        }

        public void removeTrigger(Trigger t) {
            synchronized (syncObj) {
                runnerThread.deleteTrigger(t);
                try {
//                    triggerLoader.removeTrigger(t);
                    triggerLoader.updateTrigger(t);
                } catch (TriggerLoaderException e) {
                    logger.error("failing remove trigger:", e);
                }
            }
        }

        public void insertTrigger(Trigger t) {
            synchronized (syncObj) {
                try {
                    triggerLoader.addTrigger(t);
                } catch (TriggerLoaderException e) {
                    logger.error("failing insert trigger:", e);
                }
                runnerThread.addTrigger(t);
            }
        }

        public void updateTrigger(Trigger t)  {
            synchronized (syncObj) {
                try {
                    triggerLoader.updateTrigger(t);
                } catch (TriggerLoaderException e) {
                    logger.error("failing update trigger:", e);
                }
            }
        }

        public void run() {
            while (!shutdown) {
                synchronized (syncObj) {
                    try {
                        lastRunnerThreadCheckTime = System.currentTimeMillis();
                        try {
                            checkAllTriggers();
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        } catch (Throwable t) {
                            logger.error(t.getMessage());
                        }
                        runnerThreadIdleTime = scannerInterval - (System.currentTimeMillis() - lastRunnerThreadCheckTime);
                        if (runnerThreadIdleTime < 0) {
                            logger.error("Trigger manager thread " + this.getName()
                                    + " is too busy!");
                        } else {
                            syncObj.wait(runnerThreadIdleTime);
                        }
                    } catch (InterruptedException e) {
                        logger.info("Interrupted. Probably to shut down.");
                    }
                }
            }
        }

        private void checkAllTriggers() {
            long now = System.currentTimeMillis();
            List<Job> jobs = new ArrayList<Job>();
            for (Trigger t : triggers) {
                if (t.getTriggerTime() < now && t.getTriggerStatus().equals(TriggerStatus.READY)) {
                    jobs.add(t.getJob());
                    //delete trigger form queue and update trigger time
                    removeTrigger(t);
                }
                runnerThread.deleteTrigger(t);
            }

            onTriggerTrigger(jobs);

            try {
                List<Trigger> triggers = triggerLoader.loadTriggers();
                for (Trigger trigger : triggers) {
                    runnerThread.addTrigger(trigger);
                }
            } catch (TriggerLoaderException e) {
                logger.error("failing load:", e);
            }
        }

        private void onTriggerTrigger(List<Job> jobs) {
            JobScheduler scheduler = new JobScheduler();
            scheduler.schedule(jobs);

        }

        private class TriggerComparator implements Comparator<Trigger> {
            @Override
            public int compare(Trigger arg0, Trigger arg1) {
                long first = arg1.getTriggerTime();
                long second = arg0.getTriggerTime();

                if (first == second) {
                    return 0;
                } else if (first < second) {
                    return 1;
                }
                return -1;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Props p = new Props();
        p.put("database.type", "mysql");
        p.put("mysql.port", 3306);
        p.put("mysql.host", "localhost");
        p.put("mysql.database", "test");
        p.put("mysql.user", "root");
        p.put("mysql.password", "root");
        p.put("mysql.numconnections", 2);
        TriggerLoader loader = new JdbcTriggerLoader(p);
        TriggerManager tm = new TriggerManager(loader);
        tm.start();

    }
}
