package com.easy4coding.rpc.provider.server;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.RpcRequest;
import com.easy4coding.rpc.consumer.client.RpcResponse;
import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dmz
 * @date Create in 5:40 下午 2023/3/5
 */
@Slf4j
public class BioServer implements RpcServer {

    private final String host;

    private final int port;

    private final AtomicBoolean isOpen = new AtomicBoolean(false);

    private final Executor executor;

    public BioServer(String host, int port) {
        this.host = host;
        this.port = port;
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void open() {
        if (isOpen.compareAndSet(false, true)) {
            doOpen();
        }
    }

    public void doOpen() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host, port));
            while (true) {
                final Socket client = serverSocket.accept();
                log.info("accept client,host:{},port:{}", client.getInetAddress().getHostAddress(), client.getPort());
                executor.execute(() -> clientHandleTask(client));
            }
        } catch (IOException e) {
            log.error("fail to start server", e);
            throw new RpcException(e);
        }
    }

    private void clientHandleTask(Socket client) {
        while (true) {
            try {
                byte[] bytes = new byte[1024 * 10];
                final int read = client.getInputStream().read(bytes);
                if (read > 0) {
                    final String requestStr = new String(bytes);
                    log.info("receive request:{}", requestStr);
                    final RpcRequest rpcRequest = JSON.parseObject(requestStr, RpcRequest.class);
                    final RpcResponse rpcResponse = RequestHandler.handle(rpcRequest);
                    client.getOutputStream().write(JSON.toJSONString(rpcResponse).getBytes(StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                log.error("error occur while handling client", e);
            }
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen.get();
    }
}
