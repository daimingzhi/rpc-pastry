package com.easy4coding.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.RpcClient;
import com.easy4coding.rpc.consumer.client.RpcRequest;
import com.easy4coding.rpc.consumer.client.RpcResponse;
import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author dmz
 * @date Create in 3:02 下午 2023/3/10
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    private final RpcClient rpcClient;

    private final Class<?> interfaceName;

    public RpcInvocationHandler(RpcClient rpcClient,Class<?> interfaceName) {
        this.rpcClient = rpcClient;
        this.interfaceName = interfaceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 调用远程服务方法
        final String methodName = method.getName();
        RpcRequest request = new RpcRequest();
        request.setServiceName(interfaceName.getName());
        request.setMethodName(methodName);
        request.setParams(args);
        request.setParamsDesc(
            Arrays.stream(args).map(o -> o.getClass().getName()).toArray(String[]::new));
        final RpcResponse rpcResponse = rpcClient.sendRequest(request);
        log.info("accept response from server:{}", rpcResponse);
        if (rpcResponse.isSuccess()) {
            final Class<?> returnType = method.getReturnType();
            final Object result = rpcResponse.getResult();
            return JSON.parseObject(JSON.toJSONString(result), returnType);
        }
        throw new RpcException(rpcResponse.getErrorMsg());
    }


}
