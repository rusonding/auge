package com.auge.job;

import com.auge.execute.executor.CommandJobExecutor;
import com.auge.execute.executor.Executor;
import com.auge.model.Job;
import com.auge.execute.message.Message;
import com.auge.execute.message.MessageType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * Created by lixun on 2017/6/23.
 */
public class JobClient {
    private static String host = "127.0.0.1";
    private static int port = 8080;

    public void execJob(Job job) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Channel channel = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new JobClientChannelInitializer())
            ;
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            channel = cf.channel();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Message msg = new Message();
        msg.setJob(job);
        msg.setClientId("scheduler-client");
        msg.setType(MessageType.JOB_SUBMIT);
        msg.setChannelId(channel.id().toString());

        JobClientHandler clientHandler = channel.pipeline().get(JobClientHandler.class);
        synchronized (clientHandler) {
            channel.writeAndFlush(msg);
            try {
                clientHandler.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            channel.close();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        Job job = new Job();
        job.setJobId("1");
        job.setJobType(JobType.COMMAND);
        Executor executor = new CommandJobExecutor("cmd /c dir");
        job.setExecutor(executor);

        new JobClient().execJob(job);
    }
}
