package com.easy4coding.rpc.consumer.client.channel;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author dmz
 * @date Create in 2:31 下午 2023/3/5
 */
@Slf4j
public class BioChannel implements RpcChannel {

    private final Socket socket;

    public BioChannel(String host, int port) {
        this.socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            log.error("there are some error when connect to server", e);
            throw new RpcException(e);
        }
    }

    @Override
    public void send(Object msg) throws Exception {
        socket.getOutputStream().write(JSON.toJSONString(msg).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] receive() throws Exception {
        // fixme: 不考虑响应数据超过10kb的情况
        byte[] bytes = new byte[1024 * 10];
        socket.getInputStream().read(bytes);
        return bytes;
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("some error occur when client close", e);
        }
    }
}
