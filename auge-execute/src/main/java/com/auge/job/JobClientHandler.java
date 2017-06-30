package com.auge.job;

import com.auge.execute.message.Message;
import com.auge.execute.message.MessageType;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lixun on 2017/6/20.
 */
public class JobClientHandler extends SimpleChannelInboundHandler<Message> {
    private static Logger logger = LoggerFactory.getLogger(JobClientHandler.class);
    private Message message = null;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.info("client receive master msg:" + new Gson().toJson(msg));
        switch (msg.getType()) {
            case JOB_SUCCESS:
                System.out.println("success");
                execDependenceChildJob(msg.getJob().getJobId());
                break;
            case JOB_FAILURE:
                break;
            case JOB_UNKNOWN:
                break;
            default:
                System.out.println("fail");
        }
        message = msg;
        notifyEvent();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        notifyEvent();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        notifyEvent();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void execDependenceChildJob(String jobId) {
    }

    public Message getResponse() {
            Message response = message;
            message = null;
            return response;
    }

    private synchronized void notifyEvent() {
        this.notifyAll();
    }

}
