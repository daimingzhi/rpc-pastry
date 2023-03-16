package com.easy4coding.rpc.config;

import com.easy4coding.rpc.consumer.proxy.RpcProxyFactory;
import com.easy4coding.rpc.registry.*;
import lombok.Data;

/**
 * @author dmz
 * @date Create in 11:27 上午 2023/3/10
 */
@Data
public class ReferenceConfig<T> {

    private Class<T> interfaceClass;

    private RegistryConfig registryConfig;

    public T reference() {

        final RegistryConfig.RegistryType registryType = registryConfig.getRegistryType();

        RpcServiceDirectory<T> rpcServiceDirectory =
            new RpcServiceDirectory<>(interfaceClass.getName(), interfaceClass);

        Registry registry;
        final String registryHost = registryConfig.getAddress().split(":")[0];
        final int registryPort = Integer.parseInt(registryConfig.getAddress().split(":")[1]);
        if (registryType.equals(RegistryConfig.RegistryType.LOCAL)) {
            registry = RegistryConfig.getLocalRegistry();
        } else if (registryType.equals(RegistryConfig.RegistryType.REDIS)) {
            registry = new RpcRedisRegistry(registryHost, registryPort);
        } else {
            registry = new RpcZookeeperRegistry(registryHost, registryPort);
        }
        registry.subscribe(interfaceClass.getName(), rpcServiceDirectory);

        return RpcProxyFactory.createProxy(interfaceClass, rpcServiceDirectory);
    }
}
