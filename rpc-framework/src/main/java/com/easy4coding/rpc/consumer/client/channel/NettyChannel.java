package com.easy4coding.rpc.consumer.client.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author dmz
 * @date Create in 3:24 下午 2023/3/5
 */
public class NettyChannel implements RpcChannel {

    private final Channel channel;

    public NettyChannel(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup(64);
        bootstrap.group(workerGroup).channel(NioSocketChannel.class)
            //这个套接字选项通知内核，如果端口忙，但TCP状态位于 TIME_WAIT ，
            // 可以重用端口。如果端口忙，而TCP状态位于其他状态，重用端口时依旧得到一个错误信息，指明"地址已经使用中"
            .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
            // 是否启用Nagle算法，启用：TCP_NODELAY=false，数据包会合并再发送
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                }
            });
        // connect
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    @Override
    public void send(Object msg) throws Exception {

    }

    @Override
    public byte[] receive() throws Exception {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
