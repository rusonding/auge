package com.auge.model;

import com.auge.trigger.TriggerStatus;
import com.google.gson.Gson;

/**
 * Created by lixun on 2017/6/26.
 */
public class Trigger {
    private long triggerId;
    private long triggerTime;
    private String cronExpression;//"0 0 1 ? * *"
    private Job job;
    private TriggerStatus triggerStatus;
    private long lastModifyTime;

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(long triggerId) {
        this.triggerId = triggerId;
    }

    public long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public TriggerStatus getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(TriggerStatus triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
