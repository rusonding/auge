package com.auge.execute.message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * Created by lixun on 2017/6/22.
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    private Kryo kryo = new Kryo();
    public MessageEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] body = convertToBytes(msg);
        int dataLength = body.length;
        //body lenth is message header
        out.writeInt(dataLength);
        out.writeBytes(body);
    }

    private byte[] convertToBytes(Message msg) {
        ByteArrayOutputStream bos = null;
        Output output = null;
        try {
            bos = new ByteArrayOutputStream();
            output = new Output(bos);
            kryo.writeObject(output, msg);
            output.flush();
            return bos.toByteArray();
        } catch (KryoException e) {
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(bos);
        }
        return null;
    }
}
