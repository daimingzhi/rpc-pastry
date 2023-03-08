package com.easy4coding.rpc.consumer.client.channel;

import com.easy4coding.rpc.consumer.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author dmz
 * @date Create in 2:31 下午 2023/3/5
 */
@Slf4j
public class NioChannel implements RpcChannel {

    private final Selector selector;

    private final SocketChannel socketChannel;

    public NioChannel(String host, int port) {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            final boolean connect = socketChannel.connect(new InetSocketAddress(host, port));
            if (!connect) {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                // 阻塞，接收事件
                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    SocketChannel client = (SocketChannel)key.channel();
                    if (key.isConnectable()) {
                        if (client.isConnectionPending()) {
                            // 取消connect事件
                            key.interestOps(SelectionKey.OP_READ);
                            client.finishConnect();
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("there are some error when connect to server", e);
            throw new RpcException(e);
        }
    }

    @Override
    public void send(byte[] msg) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(msg.length);
        byteBuffer.put(msg);
        byteBuffer.flip();
        final int write = socketChannel.write(byteBuffer);
        log.debug("client send bytes:{}", write);
    }

    @Override
    public byte[] receive() throws Exception {
        // 阻塞，接收事件
        selector.select();
        for (SelectionKey key : selector.selectedKeys()) {
            SocketChannel client = (SocketChannel)key.channel();
            if (key.isReadable()) {
                final ByteBuffer allocate = ByteBuffer.allocate(1024 * 10);
                client.read(allocate);

                client.close();
                return allocate.array();
            }
        }
        return new byte[0];
    }

    @Override
    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            log.error("some error occur when client close", e);
        }
    }
}
