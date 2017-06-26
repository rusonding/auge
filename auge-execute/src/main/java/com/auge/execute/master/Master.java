package com.auge.execute.master;

import com.auge.execute.worker.Worker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixun on 2017/6/20.
 */
public class Master {
    private static Logger logger = LoggerFactory.getLogger(Master.class);
    public static final int port = 8080;
    private Map<String, Worker> workers;

    public Map<String, Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Map<String, Worker> workers) {
        this.workers = workers;
    }

    public Master() {
        workers = new HashMap<String, Worker>();
    }

    public synchronized boolean addWorker(Worker worker) {
        if(workers.get(worker.getId()) != null) {
            logger.warn("worker already exists!");
            return false;
        }
        workers.put(worker.getId(), worker);
        return true;
    }

    public void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new MasterChannelInitializer(new Master()))
                     .option(ChannelOption.SO_BACKLOG, 128)
                     .childOption(ChannelOption.SO_KEEPALIVE, true);
            logger.info("starting master");
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("start master error:", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Master().startServer();
    }
}
