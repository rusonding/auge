package com.auge.execute.worker;

import com.auge.execute.message.MessageDecoder;
import com.auge.execute.message.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by lixun on 2017/6/22.
 */
public class WorkerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Worker worker;
    public WorkerChannelInitializer(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", new MessageDecoder())
                     .addLast("encoder", new MessageEncoder())
                     .addLast(new WorkerHandler(worker));
    }
}
