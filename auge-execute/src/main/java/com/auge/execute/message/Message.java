package com.auge.execute.message;

import com.auge.execute.job.Job;
import com.auge.execute.worker.Worker;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by lixun on 2017/6/22.
 */
public class Message implements Serializable {
    private String clientId;
    private MessageType type;
    private Job job;
    private String workerId;

    public Message() {
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Message(MessageType type) {
        this.type = type;
    }
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

//    @Override
//    public String toString() {
//        return new Gson().toJson(Message.class);
//    }
}
