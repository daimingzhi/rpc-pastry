package com.easy4coding.rpc;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.api.TestService;
import com.easy4coding.rpc.api.entity.Book;
import com.easy4coding.rpc.config.ReferenceConfig;
import com.easy4coding.rpc.config.RegistryConfig;

/**
 * @author dmz
 * @date Create in 4:10 下午 2023/3/5
 */
public class RpcClient {

    public static void main(String[] args) throws Exception {
        // consumer生成代理对象，发起rpc调用
        //        final TestService proxy = RpcProxyFactory.createProxy(TestService.class, "127.0.0.1", 8080);
        //        proxy.say("hello");
        //        proxy.borrow(new Book("big book", "big"));
        //        final Book buy = proxy.buy(100);
        //        System.out.println("client buy a book：" + JSON.toJSONString(buy));

        // 使用RpcLocalRegistry
        //        ExporterConfig<TestService> exporterConfig = new ExporterConfig<>();
        //        exporterConfig.setServiceInterface(TestService.class);
        //        exporterConfig.setObject(new TestServiceImpl());
        //        exporterConfig.setPort(8080);
        //        exporterConfig.setHost("127.0.0.1");
        //        final RegistryConfig registryConfig = new RegistryConfig();
        //        registryConfig.setRegistryType(RegistryConfig.RegistryType.LOCAL);
        //
        //        exporterConfig.setRegistryConfig(registryConfig);
        //
        //        exporterConfig.export();
        //
        //
        //        ReferenceConfig<TestService> referenceConfig = new ReferenceConfig<>();
        //
        //        referenceConfig.setInterfaceClass(TestService.class);
        //        referenceConfig.setRegistryConfig(registryConfig);
        //
        //        final TestService proxy = referenceConfig.reference();
        //        proxy.say("hello");
        //        proxy.borrow(new Book("big book", "big"));
        //        final Book buy = proxy.buy(100);
        //        System.out.println("client buy a book：" + JSON.toJSONString(buy));

        final RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setRegistryType(RegistryConfig.RegistryType.ZOOKEEPER);
        ReferenceConfig<TestService> referenceConfig = new ReferenceConfig<>();

        referenceConfig.setInterfaceClass(TestService.class);
        referenceConfig.setRegistryConfig(registryConfig);

        final TestService proxy = referenceConfig.reference();
        proxy.say("hello");
        proxy.borrow(new Book("big book", "big"));
        final Book buy = proxy.buy(100);
        System.out.println("client buy a book：" + JSON.toJSONString(buy));

    }

}
