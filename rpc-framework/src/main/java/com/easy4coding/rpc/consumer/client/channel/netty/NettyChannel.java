package com.easy4coding.rpc.consumer.client.channel.netty;

import com.easy4coding.rpc.consumer.client.channel.RpcChannel;
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
            // 如果端口忙，但TCP状态位于 TIME_WAIT ，可以重用端口。
            // 如果端口忙，而TCP状态位于其他状态，重用端口时依旧得到一个错误信息，指明"地址已经使用中"
            .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
            // 是否启用Nagle算法，启用：TCP_NODELAY=false，数据包会合并再发送
            .option(ChannelOption.TCP_NODELAY, false)
            // 建立连接超时时间
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            // 客户端handler
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RpcNettyClientHandler());
                }
            });
        // connect
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    @Override
    public void send(byte[] msg) throws Exception {
        channel.writeAndFlush(msg);
    }

    @Override
    public byte[] receive() throws Exception {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
