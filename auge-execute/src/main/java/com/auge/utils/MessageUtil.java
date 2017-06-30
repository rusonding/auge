package com.auge.utils;

import com.auge.model.Job;
import com.auge.execute.message.Message;
import com.auge.execute.message.MessageHeader;
import com.auge.execute.message.MessageType;

/**
 * Created by lixun on 2017/6/22.
 */
public class MessageUtil {


    public static Message getJobMessage(Job job) {
        Message msg = new Message();
        MessageHeader header = new MessageHeader();
        header.setMagic((byte) 0x01);
        header.setMsgType((byte) 0x01);
        header.setReserve((short) 0);
        header.setSn((short) 0);
//        byte[] bodyBytes = sb.toString().getBytes(Charset.forName("utf-8"));
//        int bodySize = bodyBytes.length;
//        header.setLen(bodySize);
        msg.setJob(job);
        msg.setClientId("cleintid---------");
        msg.setType(MessageType.JOB_SUBMIT);
        return msg;
    }
}
