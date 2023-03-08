package com.easy4coding.rpc;

import com.easy4coding.rpc.api.DmzService;
import com.easy4coding.rpc.provider.ServiceExporter;
import com.easy4coding.rpc.provider.server.netty.NettyServer;
import com.easy4coding.rpc.provider.service.DmzServiceImpl;

/**
 * @author dmz
 * @date Create in 6:20 下午 2023/3/5
 */
public class RpcServer {
    public static void main(String[] args) throws Exception {

        // 1.provider暴露服务
        ServiceExporter.export(DmzService.class, new DmzServiceImpl());

        // 2、provider启动server服务器
        //        BioServer bioServer = new BioServer("127.0.0.1", 8080);
        //        bioServer.open();
        //        NioServer nioServer = new NioServer("127.0.0.1", 8080);
        //        nioServer.open();
        NettyServer nettyServer = new NettyServer("127.0.0.1", 8080);
        nettyServer.open();

        System.in.read();

    }
}
