package com.auge.client;

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
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.info("receive master msg:" + new Gson().toJson(msg));
    }
}
