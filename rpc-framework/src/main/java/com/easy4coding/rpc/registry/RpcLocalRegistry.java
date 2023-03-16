package com.easy4coding.rpc.registry;

import com.easy4coding.rpc.consumer.exception.RpcException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dmz
 * @date Create in 10:06 上午 2023/3/9
 */
public class RpcLocalRegistry implements Registry {

    /**
     * 注册的服务提供者，key:interfaceName,value:提供者列表
     */
    private final ConcurrentMap<String, List<RpcInstance>> rpcProviders = new ConcurrentHashMap<>();
    /**
     * 进行订阅的消费者，key:interfaceName,value:订阅的消费者列表:RpcListener
     */
    private final ConcurrentMap<String, List<RpcSubscriber>> rpcSubscribers = new ConcurrentHashMap<>();

    @Override
    public void register(RpcInstance provider) {
        final List<RpcInstance> rpcInstances =
            rpcProviders.computeIfAbsent(provider.getInterfaceName(), k -> new ArrayList<>());
        rpcInstances.add(provider);
        final List<RpcSubscriber> rpcSubscribers = this.rpcSubscribers.get(provider.getInterfaceName());
        if (rpcSubscribers == null) {
            return;
        }
        for (RpcSubscriber subscriber : rpcSubscribers) {
            subscriber.notify(rpcInstances);
        }
    }

    @Override
    public void subscribe(String interfaceName, RpcSubscriber subscriber) {
        final List<RpcSubscriber> rpcInstances = rpcSubscribers.computeIfAbsent(interfaceName, k -> new ArrayList<>());
        rpcInstances.add(subscriber);
        final List<RpcInstance> serviceInstance = rpcProviders.get(interfaceName);
        if (serviceInstance == null || serviceInstance.isEmpty()) {
            throw new RpcException("No provider available for the service: " + interfaceName);
        }
        // 首次订阅，直接进行通知
        subscriber.notify(serviceInstance);
    }

    @Override
    public void unRegister(RpcInstance rpcInstance) {
        final List<RpcInstance> rpcInstances = rpcProviders.get(rpcInstance.getInterfaceName());
        if (rpcInstances == null || rpcInstances.isEmpty()) {
            return;
        }
        rpcInstances.remove(rpcInstance);
        final List<RpcSubscriber> rpcSubscribers = this.rpcSubscribers.get(rpcInstance.getInterfaceName());
        if (rpcSubscribers == null) {
            return;
        }
        for (RpcSubscriber subscriber : rpcSubscribers) {
            subscriber.notify(rpcInstances);
        }
    }

    @Override
    public String getHost() {
        throw new UnsupportedOperationException("localRegistry can not get host");
    }

    @Override
    public int getPort() {
        throw new UnsupportedOperationException("localRegistry can not get port");
    }
}
