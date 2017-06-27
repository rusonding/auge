package com.auge.execute.job;

/**
 * Created by lixun on 2017/6/24.
 */
public class JobConstants {
    public static final int DEPENDENCE_NO = 0;
    public static final int DEPENDENCE_ALL = 1;
    public static final int DEPENDENCE_PARENT = 2;
    public static final int DEPENDENCE_CHILD = 3;

    public static final int JOB_STATUS_READY = 1;
    public static final int JOB_STATUS_RUNNING = 2;
    public static final int JOB_STATUS_FINISH = 3;
    public static final int JOB_STATUS_FAIL = 4;
}
