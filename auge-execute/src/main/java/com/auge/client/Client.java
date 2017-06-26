package com.auge.client;

import com.auge.execute.executor.CommandJobExecutor;
import com.auge.execute.executor.Executor;
import com.auge.execute.job.Job;
import com.auge.execute.job.JobType;
import com.auge.execute.message.Message;
import com.auge.execute.message.MessageType;
import com.auge.execute.worker.WorkerChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by lixun on 2017/6/23.
 */
public class Client {
    private static String host = "127.0.0.1";
    private static int port = 8080;

    public void startClient() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ClientChannelInitializer())
            ;
            ChannelFuture f = bootstrap.connect(host, port).sync();

            Job job = new Job();
            job.setJobId("job_id_1111");
            job.setJobType(JobType.COMMAND);
            Executor executor = new CommandJobExecutor("cmd /c dir");
            job.setExecutor(executor);
            Message msg = new Message();
            msg.setJob(job);
            msg.setClientId("11clientid111");
            msg.setType(MessageType.SUBMIT);
            f.channel().writeAndFlush(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Client().startClient();
    }
}
