package com.easy4coding.rpc.provider.server;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.consumer.client.RpcRequest;
import com.easy4coding.rpc.consumer.client.RpcResponse;
import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dmz
 * @date Create in 5:40 下午 2023/3/5
 */
@Slf4j
public class NioServer implements RpcServer {

    private final String host;

    private final int port;

    private final AtomicBoolean isOpen = new AtomicBoolean(false);

    private final Executor executor;

    private final Selector selector;

    public NioServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        try {
            selector = Selector.open();
        } catch (IOException e) {
            log.error("here comes an error while creating server", e);
            throw new RpcException(e);
        }
    }

    @Override
    public void open() {
        if (isOpen.compareAndSet(false, true)) {
            doOpen();
        }
    }

    private void doOpen() {
        try {
            final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(host, port));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                        final SocketChannel client = ssc.accept();
                        if (client == null) {
                            continue;
                        }
                        log.debug("accept client,host:{},port:{}",
                            ((InetSocketAddress)client.getRemoteAddress()).getHostName(),
                            ((InetSocketAddress)client.getRemoteAddress()).getPort());
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        final SocketChannel client = (SocketChannel)key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
                        final int read = client.read(byteBuffer);
                        if (read > 0) {
                            final String requestStr = new String(byteBuffer.array());
                            log.info("receive request:{}", requestStr);
                            final RpcRequest rpcRequest = JSON.parseObject(requestStr, RpcRequest.class);
                            final RpcResponse rpcResponse = RequestHandler.handle(rpcRequest);
                            byteBuffer.clear();
                            byteBuffer.put(JSON.toJSONString(rpcResponse).getBytes(StandardCharsets.UTF_8));
                            byteBuffer.flip();
                            client.write(byteBuffer);
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("Here comes an error while opening the server.", e);
            throw new RpcException(e);
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen.get();
    }
}
