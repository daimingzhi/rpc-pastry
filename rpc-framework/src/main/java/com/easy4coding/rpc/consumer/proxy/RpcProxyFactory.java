package com.easy4coding.rpc.consumer.proxy;

import com.easy4coding.rpc.consumer.client.RpcClient;
import com.easy4coding.rpc.registry.RpcInstance;
import com.easy4coding.rpc.registry.RpcServiceDirectory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

        // 负载均衡
        RpcInstance selected = select(providers);

        return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {clazz},
            new RpcInvocationHandler(RpcClient.newRpcClient(selected.getHost(), selected.getPort()), clazz));

    }

    /**
     * 负载均衡：随机选择一个节点
     */
    private static RpcInstance select(List<RpcInstance> providers) {
        final int index = ThreadLocalRandom.current().nextInt(providers.size());
        return providers.get(index);
    }

}
