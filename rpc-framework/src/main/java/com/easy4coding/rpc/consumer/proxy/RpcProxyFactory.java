package com.easy4coding.rpc.consumer.proxy;

import com.easy4coding.rpc.consumer.client.RpcClient;
import com.easy4coding.rpc.consumer.exception.RpcException;
import com.easy4coding.rpc.registry.RpcInstance;
import com.easy4coding.rpc.registry.RpcServiceDirectory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dmz
 * @date Create in 12:42 上午 2021/12/12
 */
@Slf4j
public class RpcProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<T> clazz, String host, int port) {
        return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {clazz},
            new RpcInvocationHandler(RpcClient.newRpcClient(host, port), clazz));
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<T> clazz, RpcServiceDirectory<T> directory) {
        final List<RpcInstance> providers = directory.getCacheProviders();
        return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {clazz},
            (proxy, method, args) -> {

                final List<T> remoteServices = providers.stream()
                    .map(provider -> (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[] {clazz},
                        new RpcInvocationHandler(RpcClient.newRpcClient(provider.getHost(), provider.getPort()),
                            clazz)))
                    .collect(Collectors.toList());

                // 负载均衡
                T t = select(remoteServices);

                // 集群容错
                try {
                    return method.invoke(t, args);
                } catch (Exception e) {
                    throw new RpcException(e.getMessage());
                }
            });

    }

    private static <T> T select(List<T> remoteServices) {
        return remoteServices.get(0);
    }

}
