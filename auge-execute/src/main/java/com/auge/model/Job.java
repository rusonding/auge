package com.auge.model;

import com.auge.execute.executor.Executor;
import com.auge.job.JobType;

/**
 * Created by lixun on 2017/6/20.
 */
public class Job {
    private Executor executor;
    private JobType jobType;
    private String jobId;
    private int dependenceType;
    private int jobStatus;
    private long createTime;
    private long updateTime;
    private String parentId;

    public Job(String jobId) {
        this.jobId = jobId;
    }

    public Job() {
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
