package com.auge.client;

import com.auge.execute.message.MessageDecoder;
import com.auge.execute.message.MessageEncoder;
import com.auge.execute.worker.WorkerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by lixun on 2017/6/22.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", new MessageDecoder())
                     .addLast("encoder", new MessageEncoder())
                     .addLast(new ClientHandler());
    }
}
