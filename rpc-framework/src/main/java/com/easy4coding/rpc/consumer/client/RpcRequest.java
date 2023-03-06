package com.easy4coding.rpc.consumer.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dmz
 * @date Create in 12:56 下午 2021/12/12
 */
@Data
@Slf4j
public class RpcRequest {

    private static final AtomicLong INVOKE_ID = new AtomicLong(1);
    private long requestId;
    private String serviceName;
    private String methodName;
    private Object[] params;
    private String[] paramsDesc;

    public RpcRequest() {
        this.requestId = INVOKE_ID.incrementAndGet();
    }
}
