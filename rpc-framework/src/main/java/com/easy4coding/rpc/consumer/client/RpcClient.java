package com.easy4coding.rpc.consumer.client;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.channel.NioChannel;
import com.easy4coding.rpc.consumer.client.channel.RpcChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dmz
 * @date Create in 12:26 上午 2021/12/12
 */
public class RpcClient {

    private final RpcChannel rpcChannel;

    private static final Map<String, RpcClient> channelMap = new ConcurrentHashMap<>();

    private RpcClient(RpcChannel rpcChannel) {
        this.rpcChannel = rpcChannel;
    }

    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        rpcChannel.send(request);
        final byte[] receive = rpcChannel.receive();
        return JSON.parseObject(new String(receive), RpcResponse.class);
    }

    public static RpcClient newRpcClient(String host, int port) {
        String cacheKey = host + ":" + port;
        return channelMap.computeIfAbsent(cacheKey, key -> new RpcClient(new NioChannel(host, port)));
    }

}
