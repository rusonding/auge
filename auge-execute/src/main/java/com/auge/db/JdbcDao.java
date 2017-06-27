package com.auge.db;

import com.auge.model.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixun on 2017/6/25.
 */
public class JdbcDao {
    public static String getJobSate(Job job) {
        return null;
    }

    public static String updateJobSate(Job job) {
        return null;
    }

    /**
     * JobConstants.DEPENDENCE_ALL =  JobConstants.DEPENDENCE_CHILD+JobConstants.DEPENDENCE_PARENT
     * @param job
     * @return
     */
    public static List<Job> getJobAllNodes(Job job) {
        List list = new ArrayList<Job>();
        return list;
    }

    /**
     * JobConstants.DEPENDENCE_CHILD
     * @param job
     * @return
     */
    public static List<Job> getJobChildNodes(Job job) {
        List list = new ArrayList<Job>();
        return list;
    }

    /**
     * JobConstants.DEPENDENCE_PARENT
     * @param job
     * @return
     */
    public static List<Job> getJobParentNodes(Job job) {
        List list = new ArrayList<Job>();
        return list;
    }
}
