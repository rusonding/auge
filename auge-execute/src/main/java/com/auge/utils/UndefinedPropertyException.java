package com.auge.utils;

/**
 * Created by lixun on 2017/6/26.
 */
public class UndefinedPropertyException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public UndefinedPropertyException(String message) {
        super(message);
    }
}
