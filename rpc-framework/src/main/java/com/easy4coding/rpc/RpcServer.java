package com.easy4coding.rpc;

import com.easy4coding.rpc.api.TestService;
import com.easy4coding.rpc.config.ExporterConfig;
import com.easy4coding.rpc.config.RegistryConfig;
import com.easy4coding.rpc.provider.service.TestServiceImpl;

/**
 * @author dmz
 * @date Create in 6:20 下午 2023/3/5
 */
public class RpcServer {
    public static void main(String[] args) throws Exception {

        //        // 1.provider暴露服务
        //        ServiceExporter.export(TestService.class, new TestServiceImpl());
        //
        //
        //        // 2、provider启动server服务器
        //        //        BioServer bioServer = new BioServer("127.0.0.1", 8080);
        //        //        bioServer.open();
        //        //        NioServer nioServer = new NioServer("127.0.0.1", 8080);
        //        //        nioServer.open();
        //        NettyServer nettyServer = new NettyServer("127.0.0.1", 8080);
        //        nettyServer.open();

        //         使用redisRegistry
        //        ExporterConfig<TestService> exporterConfig = new ExporterConfig<>();
        //        exporterConfig.setServiceInterface(TestService.class);
        //        exporterConfig.setObject(new TestServiceImpl());
        //        exporterConfig.setPort(8080);
        //        exporterConfig.setHost("127.0.0.1");
        //        final RegistryConfig registryConfig = new RegistryConfig();
        //        registryConfig.setAddress("127.0.0.1:6379");
        //        registryConfig.setRegistryType(RegistryConfig.RegistryType.REDIS);
        //
        //        exporterConfig.setRegistryConfig(registryConfig);
        //
        //        exporterConfig.export();

        // 使用zookeeperRegistry
        final RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setRegistryType(RegistryConfig.RegistryType.ZOOKEEPER);

        ExporterConfig<TestService> exporterConfig = new ExporterConfig<>();
        exporterConfig.setServiceInterface(TestService.class);
        exporterConfig.setObject(new TestServiceImpl());
        exporterConfig.setPort(8080);
        exporterConfig.setHost("127.0.0.1");
        exporterConfig.setRegistryConfig(registryConfig);
        exporterConfig.export();

    }
}
