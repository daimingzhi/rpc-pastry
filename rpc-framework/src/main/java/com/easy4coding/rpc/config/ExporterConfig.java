package com.easy4coding.rpc.config;

import com.easy4coding.rpc.provider.ServiceExporter;
import com.easy4coding.rpc.provider.server.netty.NettyServer;
import com.easy4coding.rpc.registry.*;
import com.easy4coding.rpc.registry.redis.RpcRedisRegistry;
import com.easy4coding.rpc.registry.zookeeper.RpcZookeeperRegistry;
import lombok.Data;

/**
 * @author dmz
 * @date Create in 3:07 下午 2023/3/10
 */
@Data
public class ExporterConfig<T> {

    private Class<T> serviceInterface;

    /**
     * 服务实现类
     */
    private Object object;

    /**
     * 指定服务暴露的端口
     */
    private int port;

    private String host;

    private RegistryConfig registryConfig;

    public void export() {

        startServer(host, port);

        doExport(serviceInterface, object, registryConfig);
    }

    private void doExport(Class<T> interfaceName, Object object, RegistryConfig registryConfig) {
        final RegistryConfig.RegistryType registryType = registryConfig.getRegistryType();
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
        registry.register(new RpcInstance(interfaceName.getName(), host, port));
        ServiceExporter.export(serviceInterface, object);
    }

    private void startServer(String host, int port) {
        NettyServer nettyServer = new NettyServer(host, port);
        nettyServer.open();
    }
}
