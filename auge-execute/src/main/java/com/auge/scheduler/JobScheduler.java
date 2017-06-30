package com.auge.scheduler;

import com.auge.job.JobClient;
import com.auge.execute.message.Message;
import com.auge.execute.message.MessageType;
import com.auge.model.Job;
import com.auge.trigger.JdbcTriggerLoader;
import com.auge.trigger.TriggerLoader;
import com.auge.utils.Props;
import com.auge.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by lixun on 2017/6/27.
 */
public class JobScheduler {
    private static ExecutorService es = Executors.newFixedThreadPool(2);

    public void schedule(List<Job> jobs) {
        final JobClient client = new JobClient();
        try {
            for (final Job job : jobs) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.execJob(job);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
        }
    }

    private void execDependenceChildJob(String jobId) {
        Props p = Utils.getMysqlAuth();
        TriggerLoader loader = new JdbcTriggerLoader(p);
        List<Job> jobs = loader.loadJobs(jobId);
        schedule(jobs);
    }

    public static void main(String[] args) {
        JobScheduler scheduler = new JobScheduler();
        Props p = Utils.getMysqlAuth();
        TriggerLoader loader = new JdbcTriggerLoader(p);
        List<Job> jobs = loader.loadJobs("0");
        System.out.println("jobs:" + jobs.size());
        scheduler.schedule(jobs);
    }
}
