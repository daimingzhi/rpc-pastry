package com.easy4coding.rpc.provider.server.netty;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.RpcRequest;
import com.easy4coding.rpc.consumer.client.RpcResponse;
import com.easy4coding.rpc.provider.server.RequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dmz
 * @date Create in 6:45 下午 2023/3/6
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcNettyServerHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf byteBuf = (ByteBuf)msg;
        final int index = byteBuf.writerIndex();
        byte[] bytes = new byte[index];
        byteBuf.readBytes(bytes);
        String requestStr = new String(bytes);
        log.info("receive request:{}", requestStr);
        final RpcRequest rpcRequest = JSON.parseObject(requestStr, RpcRequest.class);
        final RpcResponse rpcResponse = RequestHandler.handle(rpcRequest);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(JSON.toJSONBytes(rpcResponse)));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

}
