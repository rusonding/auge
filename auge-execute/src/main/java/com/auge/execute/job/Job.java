package com.auge.execute.job;

import com.auge.execute.executor.Executor;

/**
 * Created by lixun on 2017/6/20.
 */
public class Job {
    private Executor executor;
    private JobType jobType;
    private String jobId;
    private int dependenceType;
    private int jobState;
    private String createTime;
    private String updateTime;
    private String parentId;

    public int getJobState() {
        return jobState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setJobState(int jobState) {
        this.jobState = jobState;
    }

    public int getDependenceType() {
        return dependenceType;
    }

    public void setDependenceType(int dependenceType) {
        this.dependenceType = dependenceType;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}
