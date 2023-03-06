package com.easy4coding.rpc.provider.server;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.RpcRequest;
import com.easy4coding.rpc.consumer.client.RpcResponse;
import com.easy4coding.rpc.consumer.exception.RpcException;
import com.easy4coding.rpc.provider.ServiceExporter;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author dmz
 * @date Create in 9:26 下午 2023/3/5
 */
public class RequestHandler {

    public static RpcResponse handle(RpcRequest rpcRequest) {
        final String serviceName = rpcRequest.getServiceName();
        final Object target = ServiceExporter.getTarget(serviceName);
        final String[] paramsDesc = rpcRequest.getParamsDesc();
        final Class<?>[] classes = Arrays.stream(paramsDesc).map(className -> {
            try {
                return RequestHandler.class.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new RpcException("can not found class: " + className);
            }
        }).toArray(Class[]::new);

        final Object[] params = rpcRequest.getParams();
        for (int i = 0; i < classes.length; i++) {
            params[i] = JSON.parseObject(JSON.toJSONString(params[0]), classes[i]);
        }

        RpcResponse rpcResponse;
        try {
            final Method declaredMethod = target.getClass().getDeclaredMethod(rpcRequest.getMethodName(), classes);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(target, params);
            rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setCode(1);
            rpcResponse.setErrorMsg("");
            rpcResponse.setResult(invoke);
        } catch (Exception e) {
            rpcResponse = RpcResponse.errorResponse(e.getMessage(), rpcRequest.getRequestId());
        }
        return rpcResponse;
    }

}
