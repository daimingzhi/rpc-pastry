package com.easy4coding.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.RpcClient;
import com.easy4coding.rpc.consumer.client.RpcRequest;
import com.easy4coding.rpc.consumer.client.RpcResponse;
import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author dmz
 * @date Create in 12:42 上午 2021/12/12
 */
@Slf4j
public class RpcProxyFactory {

    public static <T> T createProxy(final Class<T> clazz, String host, int port) {
        return ((T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {clazz},
            new InvocationHandler() {

                RpcClient rpcClient = RpcClient.newRpcClient(host, port);

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 调用远程服务方法
                    final String methodName = method.getName();
                    RpcRequest request = new RpcRequest();
                    request.setServiceName(clazz.getName());
                    request.setMethodName(methodName);
                    request.setParams(args);
                    request.setParamsDesc(Arrays.stream(args).map(o -> o.getClass().getName()).toArray(String[]::new));
                    final RpcResponse rpcResponse = rpcClient.sendRequest(request);
                    log.info("accept response from server:{}", rpcResponse);
                    if (rpcResponse.isSuccess()) {
                        final Class<?> returnType = method.getReturnType();
                        final Object result = rpcResponse.getResult();
                        return JSON.parseObject(JSON.toJSONString(result),returnType);
                    }
                    throw new RpcException(rpcResponse.getErrorMsg());
                }
            }));
    }

}
