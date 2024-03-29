package com.easy4coding.rpc.consumer.client;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.channel.RpcChannel;
import com.easy4coding.rpc.consumer.client.channel.netty.NettyChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dmz
 * @date Create in 12:26 上午 2021/12/12
 */
public class RpcClient {

    private final RpcChannel rpcChannel;

    private static final Map<String, RpcClient> CHANNEL_MAP = new ConcurrentHashMap<>();

    private RpcClient(RpcChannel rpcChannel) {
        this.rpcChannel = rpcChannel;
    }

    public static RpcClient newRpcClient(String host, int port) {
        String cacheKey = host + ":" + port;
        return CHANNEL_MAP.computeIfAbsent(cacheKey, key -> new RpcClient(new NettyChannel(host, port)));
    }

    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        // 同一个channel下保证request-response一一对应
        synchronized (rpcChannel){
            rpcChannel.send(JSON.toJSONBytes(request));
            final byte[] receive = rpcChannel.receive();
            return JSON.parseObject(new String(receive), RpcResponse.class);
        }
    }

}
