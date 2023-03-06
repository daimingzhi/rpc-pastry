package com.easy4coding.rpc.consumer.exception;

/**
 * @author dmz
 * @date Create in 2:18 下午 2021/12/12
 */
public class RpcException extends RuntimeException{

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message) {
        super(message);
    }
}
