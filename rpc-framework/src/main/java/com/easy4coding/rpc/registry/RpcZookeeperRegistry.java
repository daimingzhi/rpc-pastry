package com.easy4coding.rpc.registry;

import com.easy4coding.rpc.consumer.exception.RpcException;
import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.concurrent.TimeUnit;

/**
 * @author dmz
 * @date Create in 7:11 下午 2023/3/9
 */
@Slf4j
public class RpcZookeeperRegistry implements Registry {

    private final CuratorFramework client;

    private final String host;

    private final int port;

    private final String ROOT = "/rpc-pastry";

    /**
     * 已创建的持久节点
     */
    private final ConcurrentSet<String> createdPersistentPath = new ConcurrentSet<>();

    public RpcZookeeperRegistry(String host, int port) {
        this.host = host;
        this.port = port;
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
            .connectString(host + ":" + port)
            .retryPolicy(new RetryNTimes(1, 1000))
            .connectionTimeoutMs(5000)
            .sessionTimeoutMs(30 * 1000);

        this.client = builder.build();
        this.client.start();
        boolean connected = false;
        try {
            connected = client.blockUntilConnected(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignore) {

        }
        if (!connected) {
            throw new IllegalStateException("zookeeper not connected");
        }
    }

    @Override
    public void register(RpcInstance rpcInstance) {
        String providerPath = ROOT + "/" + rpcInstance.getInterfaceName() + "/provider";
        if (!createdPersistentPath.contains(providerPath)) {
            try {
                client.create().forPath(providerPath);
            } catch (Exception e) {
                log.error("Failed to register,create path failed,path: " + providerPath);
                throw new RpcException(e);
            }
            createdPersistentPath.add(providerPath);
        }
        final String providerInstancePath = providerPath + "/" + rpcInstance.getHost() + ":" + rpcInstance.getPort();
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(providerInstancePath);
        } catch (Exception e) {
            log.error("Failed to register,create path failed,path: " + providerInstancePath);
            throw new RpcException(e);
        }

    }

    @Override
    public void unRegister(RpcInstance rpcInstance) {
        String providerPath = ROOT + "/" + rpcInstance.getInterfaceName() + "/provider";
        final String providerInstancePath = providerPath + "/" + rpcInstance.getHost() + ":" + rpcInstance.getPort();
        try {
            client.delete().deletingChildrenIfNeeded().forPath(providerInstancePath);
        } catch (KeeperException.NoNodeException ignore) {

        } catch (Exception e) {
            log.error("Failed to unregister,delete path failed,path: " + providerInstancePath);
            throw new RpcException(e);
        }
    }

    @Override
    public void subscribe(String interfaceName, RpcSubscriber rpcListener) {
        // 添加对provider节点的监听
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}
