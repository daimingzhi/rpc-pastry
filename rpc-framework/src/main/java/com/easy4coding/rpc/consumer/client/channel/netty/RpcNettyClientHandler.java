package com.easy4coding.rpc.consumer.client.channel.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author dmz
 * @date Create in 6:39 下午 2023/3/6
 */
public class RpcNettyClientHandler extends ChannelDuplexHandler {

    private final LinkedBlockingDeque<byte[]> res;

    public RpcNettyClientHandler(LinkedBlockingDeque<byte[]> res) {
        this.res = res;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf byteBuf = (ByteBuf)msg;
        final int index = byteBuf.writerIndex();
        byte[] bytes = new byte[index];
        byteBuf.readBytes(bytes);
        res.offer(bytes);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buffer = Unpooled.wrappedBuffer(((byte[])msg));
        super.write(ctx, buffer, promise);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
