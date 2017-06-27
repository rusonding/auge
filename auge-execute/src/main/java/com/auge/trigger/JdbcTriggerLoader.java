package com.auge.trigger;

import com.auge.db.AbstractJdbcLoader;
import com.auge.execute.executor.CommandJobExecutor;
import com.auge.model.Job;
import com.auge.model.Trigger;
import com.auge.utils.Props;
import com.auge.utils.Utils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lixun on 2017/6/26.
 */
public class JdbcTriggerLoader  extends AbstractJdbcLoader implements TriggerLoader {
    private static Logger logger = Logger.getLogger(JdbcTriggerLoader.class);

    private static String GET_ALL_TRIGGERS = "SELECT  t1.trigger_id,t1.trigger_time,t1.cron_expression,t1.trigger_status " +
                    " ,t2.job_id,t2.command" +
                    " FROM  triggers t1 " +
                    " join jobs t2" +
                    " on t1.job_id=t2.job_id" +
                    " where t2.parentId=0 ";

    private static String REMOVE_TRIGGER = "DELETE FROM triggers WHERE trigger_id=?";
    private static String ADD_TRIGGER = "insert into triggers(trigger_time,cron_expression,job_id,trigger_status,last_modify_time) values(?,?,?,?,?)";
    private static String UPDATE_TRIGGER = "UPDATE triggers SET trigger_time=?, last_modify_time=? WHERE trigger_id=?";

    public JdbcTriggerLoader(Props props) {
        super(props);
    }

    private Connection getConnection() throws TriggerLoaderException {
        Connection connection = null;
        try {
            connection = super.getDBConnection(false);
        } catch (Exception e) {
            DbUtils.closeQuietly(connection);
            throw new TriggerLoaderException("Error getting DB connection.", e);
        }
        return connection;
    }

    @Override
    public List<Trigger> loadTriggers() throws TriggerLoaderException {
        logger.info("Loading all trigger from db.");
        Connection connection = getConnection();

        QueryRunner runner = new QueryRunner();
        ResultSetHandler<List<Trigger>> handler = new TriggerResultHandler();
        List<Trigger> triggers;
        try {
            triggers = runner.query(connection, GET_ALL_TRIGGERS, handler);
        } catch (SQLException e) {
            logger.error(GET_ALL_TRIGGERS + " failed.");
            throw new TriggerLoaderException("Loading triggers from db failed. ", e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
        logger.info("Loaded " + triggers.size() + " triggers.");
        return triggers;
    }

    @Override
    public void addTrigger(Trigger t) throws TriggerLoaderException {
        logger.info("Inserting trigger " + t.toString() + " into db.");
        t.setLastModifyTime(System.currentTimeMillis());
//        t.setTriggerTime(Utils.getCronTime(t.getCronExpression(), t.getTriggerTime()));
        t.setTriggerTime(Utils.getCronTime(t.getCronExpression(), System.currentTimeMillis()));
        Connection connection = getConnection();

        try {
            QueryRunner runner = new QueryRunner();
            runner.update(connection, ADD_TRIGGER, t.getTriggerTime(), t.getCronExpression(), t.getJob().getJobId(), t.getTriggerStatus().getNumVal(), t.getLastModifyTime());
            connection.commit();
        } catch (Exception e) {
            throw new TriggerLoaderException("Error uploading trigger", e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    @Override
    public void updateTrigger(Trigger t) throws TriggerLoaderException {
        t.setLastModifyTime(System.currentTimeMillis());
        System.out.println("before:" + t.getTriggerTime());
//        t.setTriggerTime(Utils.getCronTime(t.getCronExpression(), t.getTriggerTime()));
        t.setTriggerTime(Utils.getCronTime(t.getCronExpression(), System.currentTimeMillis()));
        Connection connection = getConnection();
        try {
            QueryRunner runner = new QueryRunner();
            int updates = runner.update(connection, UPDATE_TRIGGER, t.getTriggerTime(), t.getLastModifyTime(),
                            t.getTriggerId());
            connection.commit();
            if (updates == 0) {
                throw new TriggerLoaderException("No trigger has been updated.");
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Updated " + updates + " records.");
                }
            }
        } catch (Exception e) {
            throw new TriggerLoaderException("Failed to update trigger "
                    + t.toString() + " into db!");
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    @Override
    public void removeTrigger(Trigger t) throws TriggerLoaderException {
        logger.info("Removing trigger " + t.toString() + " from db.");

        QueryRunner runner = createQueryRunner();
        try {
            int removes = runner.update(REMOVE_TRIGGER, t.getTriggerId());
            if (removes == 0) {
                throw new TriggerLoaderException("No trigger has been removed.");
            }
        } catch (SQLException e) {
            logger.error(REMOVE_TRIGGER + " failed.");
            throw new TriggerLoaderException("Remove trigger " + t.toString()
                    + " from db failed. ", e);
        }
    }


    public class TriggerResultHandler implements ResultSetHandler<List<Trigger>> {

        @Override
        public List<Trigger> handle(ResultSet rs) throws SQLException {
            if (!rs.next()) {
                return Collections.<Trigger> emptyList();
            }
            ArrayList<Trigger> triggers = new ArrayList<Trigger>();
            do {
                long triggerId = rs.getLong(1);
                long triggerTime = rs.getLong(2);
                String cronExpression = rs.getString(3);
                TriggerStatus triggerStatus = TriggerStatus.fromInteger(rs.getInt(4));
                String jobId = rs.getString(5);
                String jobCommand = rs.getString(6);
                Job job = new Job();
                job.setJobId(jobId);
                job.setExecutor(new CommandJobExecutor(jobCommand));

                Trigger t = new Trigger();
                t.setTriggerId(triggerId);
                t.setTriggerTime(triggerTime);
                t.setCronExpression(cronExpression);
                t.setTriggerStatus(triggerStatus);
                t.setJob(job);
                try {
                    triggers.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Failed to load trigger " + triggerId);
                }
            } while (rs.next());
            return triggers;
        }
    }
}
