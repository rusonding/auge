package com.auge.utils;

import com.auge.trigger.JdbcTriggerLoader;
import com.auge.trigger.TriggerLoader;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by lixun on 2017/6/24.
 */
public class Utils {
    private static Logger logger = Logger.getLogger(Utils.class);

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Equivalent to Object.equals except that it handles nulls. If a and b are
     * both null, true is returned.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b) {
        if (a == null || b == null) {
            return a == b;
        }

        return a.equals(b);
    }

    public static long getCronTime(String cronExpression, long time) {
        long cronTime = 0;
        try {
            CronExpression ce = new CronExpression(cronExpression);
            cronTime = ce.getTimeAfter(new Date(time)).getTime();
        } catch (ParseException e) {
            logger.error("error CronExpression:"+cronExpression, e);
        }
        return cronTime;
    }

    public static Props getMysqlAuth() {
        Props p = new Props();
        p.put("database.type", "mysql");
        p.put("mysql.port", 3306);
        p.put("mysql.host", "localhost");
        p.put("mysql.database", "test");
        p.put("mysql.user", "root");
        p.put("mysql.password", "root");
        p.put("mysql.numconnections", 2);
        return p;
    }
}
