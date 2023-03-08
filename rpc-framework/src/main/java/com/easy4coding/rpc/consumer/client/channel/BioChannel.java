package com.easy4coding.rpc.consumer.client.channel;

import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dmz
 * @date Create in 2:31 下午 2023/3/5
 */
@Slf4j
public class BioChannel implements RpcChannel {

    private final Socket socket;

    private final AtomicBoolean inUse = new AtomicBoolean(false);

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
    public void send(byte[] msg) throws Exception {
        int count = 0;
        while (!inUse.compareAndSet(false, true)) {
            // 自旋等待连接释放
            TimeUnit.MILLISECONDS.sleep(100);
            count++;
            if (count > 30) {
                // 超过3秒没有获取到连接，抛出异常
                throw new RpcException("连接获取超时！");
            }
        }
        socket.getOutputStream().write(msg);
    }

    @Override
    public byte[] receive() throws Exception {
        // fixme: 不考虑响应数据超过10kb的情况
        byte[] bytes = new byte[1024 * 10];
        socket.getInputStream().read(bytes);
        // 数据接收成功后，释放连接
        inUse.compareAndSet(true, false);
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
