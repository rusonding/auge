package com.auge.scheduler;

import com.auge.execute.master.MasterUtil;
import com.auge.execute.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lixun on 2017/7/2.
 */
public class ScheduleCallbackThread {
    private static Logger logger = LoggerFactory.getLogger(ScheduleCallbackThread.class);
    private static ScheduleCallbackThread instance = new ScheduleCallbackThread();
    public static ScheduleCallbackThread getInstance(){
        return instance;
    }
    private LinkedBlockingQueue<Message> callBackQueue = new LinkedBlockingQueue<Message>();

    private Thread triggerCallbackThread;
    private boolean toStop = false;
    public void start() {
        triggerCallbackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(!toStop){
                    try {
                        Message callback = getInstance().callBackQueue.take();
                        if (callback != null) {
                            // callback
                            try {
                                MasterUtil.callMaster(callback);
                            } catch (Exception e) {
                                logger.error("ScheduleCallbackThread Exception:", e);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        });
        triggerCallbackThread.setDaemon(true);
        triggerCallbackThread.start();
    }
    public void toStop(){
        toStop = true;
    }

    public static void pushCallBack(Message callback){
        getInstance().callBackQueue.add(callback);
        logger.debug("push callback request, jobId:{}", callback.getJob().getJobId());
    }
}
