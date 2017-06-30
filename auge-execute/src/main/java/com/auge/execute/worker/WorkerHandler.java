package com.auge.execute.worker;

import com.auge.execute.message.Message;
import com.auge.execute.message.MessageType;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lixun on 2017/6/20.
 */
public class WorkerHandler extends SimpleChannelInboundHandler<Message> {
    private static Logger logger = LoggerFactory.getLogger(WorkerHandler.class);
    private Worker worker;
    public WorkerHandler(Worker worker) {
        this.worker = worker;
    }

    public WorkerHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        switch (msg.getType()) {
            case MASTER_CONNECT_SUCCESS:
                logger.info("connect success.");
                break;
            case MASTER_EXECUTE:
                System.out.println("exec job:" + new Gson().toJson(msg));
                int executeState = msg.getJob().getExecutor().execute();
                if (executeState == 0) {
                    //execute success,send msg to master
                    msg.setType(MessageType.WORKER_SUCCESS);
                } else {
                    msg.setType(MessageType.WORKER_FAILURE);
                }
                ctx.writeAndFlush(msg);
                break;
            default:
                msg.setType(MessageType.WORKER_FAILURE);
                ctx.writeAndFlush(msg);


        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message msg = new Message(MessageType.WORKER_CONNECT_EXECUTOR);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("master inactive: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("master closed: " + ctx.channel().remoteAddress());
    }
}
