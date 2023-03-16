package com.easy4coding.rpc.config;

import com.easy4coding.rpc.registry.RpcLocalRegistry;
import lombok.Data;
import lombok.Getter;

/**
 * @author dmz
 * @date Create in 3:07 下午 2023/3/10
 */
@Data
public class RegistryConfig {

    private static RpcLocalRegistry localInstance = new RpcLocalRegistry();
    private String address;
    private RegistryType registryType;

    public static RpcLocalRegistry getLocalRegistry() {
        return localInstance;
    }

    @Getter
    public enum RegistryType {

        /**
         * 使用zk作为注册中心
         */
        ZOOKEEPER,

        /**
         * 使用redis作为注册中心
         */
        REDIS,
        /**
         * 本地部署的注册中心
         */
        LOCAL
    }
}
