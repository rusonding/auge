package com.auge.scheduler;

import com.auge.client.Client;
import com.auge.model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by lixun on 2017/6/27.
 */
public class JobScheduler {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public void schedule(List<Job> jobs) {
        List<Future<Job>> futures = new ArrayList<Future<Job>>();
        for (final Job job : jobs) {
            Future<Job> future = es.submit(new Callable<Job>() {
                @Override
                public Job call() throws Exception {
                    Client client = new Client();
                    client.execJob(job);
                    return job;
                }
            });
            futures.add(future);
        }
        try {
            for (Future<Job> future : futures) {
                Job callbackJob = future.get();
                System.out.println("jobid="+callbackJob.getJobId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
