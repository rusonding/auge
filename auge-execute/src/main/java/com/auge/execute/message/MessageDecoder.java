package com.auge.execute.message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by lixun on 2017/6/22.
 */
public class MessageDecoder extends ByteToMessageDecoder {
    public static final int HEAD_LENGTH = 4;
    private Kryo kryo = new Kryo();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) { // header is int type
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] body = new byte[dataLength];
        in.readBytes(body);
        Object o = convertToObject(body);
        out.add(o);
    }

    private Object convertToObject(byte[] body) {
        Input input = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(body);
            input = new Input(bais);
            return kryo.readObject(input, Message.class);
        } catch (KryoException e) {
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(bais);
        }
        return null;
    }
}
