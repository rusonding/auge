package com.auge.trigger;

/**
 * Created by lixun on 2017/6/26.
 */
public class TriggerLoaderException extends Exception {
    private static final long serialVersionUID = 1L;

    public TriggerLoaderException(String message) {
        super(message);
    }

    public TriggerLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TriggerLoaderException(Throwable e) {
        super(e);
    }
}
