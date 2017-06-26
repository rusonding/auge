package com.auge.execute.master;

import com.auge.execute.message.MessageDecoder;
import com.auge.execute.message.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by lixun on 2017/6/22.
 */
public class MasterChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Master master;
    public MasterChannelInitializer(Master master) {
        this.master = master;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", new MessageDecoder())
                     .addLast("encoder", new MessageEncoder())
                     .addLast("handler", new MasterHandler(master));
    }
}
