package com.easy4coding.rpc.consumer.client.channel;

/**
 * @author dmz
 * @date Create in 2:31 下午 2023/3/5
 */
public interface RpcChannel {

    void send(Object msg) throws Exception;

    byte[] receive() throws Exception;

    void close();

}
