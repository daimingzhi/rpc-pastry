package com.easy4coding.rpc.registry;

import com.easy4coding.rpc.consumer.exception.RpcException;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务目录，consumer对指定service的provider的缓存
 *
 * @author dmz
 * @date Create in 5:56 下午 2023/3/11
 */
public class RpcServiceDirectory<T> implements RpcSubscriber {

    private final List<RpcInstance> cacheProviders = new ArrayList<>();
    private final String interfaceName;
    private final Class<T> clazz;

    public RpcServiceDirectory(String interfaceName, Class<T> clazz) {
        this.interfaceName = interfaceName;
        this.clazz = clazz;
    }

    @Override
    public synchronized void notify(List<RpcInstance> providers) {
        cacheProviders.clear();
        if (providers.isEmpty()) {
            return;
        }
        cacheProviders.addAll(providers);
    }

    public List<RpcInstance> getCacheProviders() {
        if (cacheProviders.isEmpty()) {
            throw new RpcException("No provider available for the service: " + interfaceName);
        }
        return cacheProviders;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
