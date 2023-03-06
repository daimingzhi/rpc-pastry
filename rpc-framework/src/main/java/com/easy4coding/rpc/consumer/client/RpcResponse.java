package com.easy4coding.rpc.consumer.client;

import lombok.Data;

/**
 * @author dmz
 * @date Create in 2:57 下午 2023/3/5
 */
@Data
public class RpcResponse {

    private long requestId;

    /**
     * 0失败，1成功
     */
    private int code;

    /**
     * 失败原因
     */
    private String errorMsg;

    private Object result;

    public boolean isSuccess() {
        return code == 1;
    }

    public static RpcResponse errorResponse(String msg,long requestId) {
        RpcResponse error = new RpcResponse();
        error.setCode(0);
        error.setRequestId(requestId);
        error.setErrorMsg(msg);
        return error;
    }

}
