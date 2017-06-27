package com.auge.execute.master;

import com.auge.execute.message.Message;
import com.auge.utils.Utils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by lixun on 2017/6/24.
 */
public class ProcessQueue implements Runnable {
    private MasterHandler masterHandler;

    public ProcessQueue(MasterHandler masterHandler) {
        this.masterHandler = masterHandler;
    }

    @Override
    public void run() {
        while (true) {
            ConcurrentLinkedQueue<Message> messageQueue = MasterHandler.messageQueue;
            if (messageQueue.size() > 0) {
                Message message = messageQueue.poll();
                masterHandler.submit(message);
            }
            Utils.sleep(1000);
        }
    }
}
