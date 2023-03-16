package com.easy4coding.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

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

        // 使用redisRegistry
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

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1" + ":" + 2181)
            .retryPolicy(new RetryNTimes(1, 1000))
            .connectionTimeoutMs(5000)
            .sessionTimeoutMs(30 * 1000);

        final CuratorFramework client = builder.build();

        client.start();

        client.blockUntilConnected();


    }
}
