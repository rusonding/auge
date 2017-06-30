package com.auge.execute.executor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channel;

/**
 * Created by lixun on 2017/6/20.
 */
public class CommandJobExecutor implements Executor {
    private String id;
    private Channel channel;
    private static Logger logger = LoggerFactory.getLogger(CommandJobExecutor.class);

    private String command;
    public CommandJobExecutor(String command) {
        this.command = command;
    }

    public CommandJobExecutor(Channel channel, String id) {
        this.channel = channel;
        this.id = id;
    }

    public CommandJobExecutor() {
    }

    @Override
    public int execute() throws Exception {
        logger.info("exec command:" + command);
        int exitVal = 0;
        Process process = Runtime.getRuntime().exec(command);
        exitVal = process.waitFor();
        if (exitVal != 0) {
            logger.error("failed exec command:"+command);
        }
        return exitVal;
    }

    @Override
    public String toString() {
        return "CommandJobExecutor{" +
                "command='" + command + '\'' +
                '}';
    }


    public static void main(String[] args) throws Exception {
        new CommandJobExecutor("cmd /c dir").execute();
    }

}
