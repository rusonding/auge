package com.auge.execute.master;

import com.auge.db.JdbcDao;
import com.auge.execute.job.JobConstants;
import com.auge.execute.message.Message;
import com.auge.execute.message.MessageType;
import com.auge.execute.worker.Worker;
import com.auge.execute.worker.WorkerAdmin;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by lixun on 2017/6/20.
 */
public class MasterHandler extends SimpleChannelInboundHandler<Message> {
    private static Logger logger = LoggerFactory.getLogger(MasterHandler.class);
    private Master master;
    public static ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
    public MasterHandler(Master master) {
        this.master = master;

        Thread thread = new Thread(new ProcessQueue(this));
        thread.start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Channel channel = ctx.channel();
        String workerId = WorkerAdmin.getWorkerId(channel);
        logger.debug("workers: "+master.getWorkers());

        switch (msg.getType()) {
            case CONNECT_EXECUTOR:
//                Worker worker = msg.getWorker();
                Worker worker = new Worker();
                worker.setChannel(channel);
                worker.setId(workerId);
                if (master.addWorker(worker)) {
                    logger.info("connected worker:"+workerId);
                }
                channel.write(new Message(MessageType.CONNECT_SUCCESS));
                break;
            case SUBMIT:
                submit(msg);
                break;
            case SUCCESS:
                Worker execWorker = master.getWorkers().get(workerId);
                execWorker.decreaseExec();
                logger.debug("worker " + workerId + " executor num: " + execWorker.getExecutorNum());
                msg.getJob().setJobState(JobConstants.JOB_STATE_FINISH);
                JdbcDao.updateJobSate(msg.getJob());
                break;
            case FAILURE:
                Worker failWorker = master.getWorkers().get(workerId);
                failWorker.decreaseExec();
                logger.warn("fail worker " + workerId + " executor num: " + failWorker.getExecutorNum());
                msg.getJob().setJobState(JobConstants.JOB_STATE_FAIL);
                JdbcDao.updateJobSate(msg.getJob());
                break;
        }
    }

    public void submit(Message msg) {
        Worker freeWorker = WorkerAdmin.getFreeWorker(master.getWorkers());
        msg.setType(MessageType.EXECUTE);
        if (freeWorker.getExecutorNum() > Worker.executorMaxNum) {
            messageQueue.offer(msg);
            msg.getJob().setJobState(JobConstants.JOB_STATE_READY);
            JdbcDao.updateJobSate(msg.getJob());
        } else {
            freeWorker.incrementExec();
            freeWorker.getChannel().writeAndFlush(msg);
            msg.getJob().setJobState(JobConstants.JOB_STATE_RUNNING);
            JdbcDao.updateJobSate(msg.getJob());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("worker connected:" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String workerId = WorkerAdmin.getWorkerId(ctx.channel());
        if (master.getWorkers().get(workerId) != null) {
            logger.warn("worker inactive: " + workerId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("worker closed: " + ctx.channel().remoteAddress());
    }


}
