package com.auge.execute.worker;

import com.auge.scheduler.ScheduleCallbackThread;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by lixun on 2017/6/20.
 */
public class Worker {
    private String id;
    private Channel channel;
    private int executorNum;
    public static final int executorMaxNum = 10;
    public static String host = "127.0.0.1";
    public static int port = 8080;
    private static Logger logger = LoggerFactory.getLogger(Worker.class);
    public Worker() {
        this.executorNum = 0;
    }

    public Worker(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
        this.executorNum = 0;
    }

    public int getExecutorNum() {
        return executorNum;
    }

    public void setExecutorNum(int executorNum) {
        this.executorNum = executorNum;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public synchronized void incrementExec() {
        this.executorNum++;
    }

    public synchronized void decreaseExec() {
        this.executorNum--;
    }

    public void startWorker() {
        ScheduleCallbackThread.getInstance().start();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new WorkerChannelInitializer(new Worker()))
            ;
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("start worker error:", e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        new Worker().startWorker();
    }
}
