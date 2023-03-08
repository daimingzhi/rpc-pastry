package com.easy4coding.rpc.provider.server.netty;

import com.easy4coding.rpc.provider.server.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author dmz
 * @date Create in 6:44 下午 2023/3/6
 */
public class NettyServer implements RpcServer {

    private final ServerBootstrap serverBootstrap;
    private final String host;
    private final int port;
    private Channel serverSocketChannel;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serverBootstrap = new ServerBootstrap();
        final RpcNettyServerHandler rpcNettyServerHandler = new RpcNettyServerHandler();
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        serverBootstrap.group(boosGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            // 这个套接字选项通知内核，如果端口忙，但TCP状态位于 TIME_WAIT ，
            // 可以重用端口。如果端口忙，而TCP状态位于其他状态，重用端口时依旧得到一个错误信息，指明"地址已经使用中"
            .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
            // 是否启用Nagle算法，启用：TCP_NODELAY=false，数据包会合并再发送
            // 不启用：数据包立马发送
            .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(rpcNettyServerHandler);
                }
            });
    }

    @Override
    public void open() {
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(host, port));
        channelFuture.syncUninterruptibly();
        serverSocketChannel = channelFuture.channel();
    }

    @Override
    public boolean isOpen() {
        return serverSocketChannel.isOpen();
    }
}
