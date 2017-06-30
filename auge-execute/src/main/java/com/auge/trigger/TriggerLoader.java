package com.auge.trigger;

import com.auge.model.Job;
import com.auge.model.Trigger;

import java.util.List;

/**
 * Created by lixun on 2017/6/26.
 */
public interface TriggerLoader {
    public List<Trigger> loadTriggers() throws TriggerLoaderException;
    public void addTrigger(Trigger t) throws TriggerLoaderException;
    public void updateTrigger(Trigger t) throws TriggerLoaderException;
    public void removeTrigger(Trigger t) throws TriggerLoaderException;
    public List<Job> loadJobs(String jobId);

}
