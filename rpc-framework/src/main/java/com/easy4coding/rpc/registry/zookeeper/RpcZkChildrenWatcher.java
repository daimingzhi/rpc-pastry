package com.easy4coding.rpc.registry.zookeeper;

import com.easy4coding.rpc.consumer.exception.RpcException;
import com.easy4coding.rpc.registry.RpcSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author dmz
 * @date Create in 4:39 PM 2023/3/17
 */
@Slf4j
public class RpcZkChildrenWatcher implements Watcher {

    private final CuratorFramework client;

    private final String parentPath;

    private final String interfaceName;

    private final RpcSubscriber rpcSubscriber;

    public RpcZkChildrenWatcher(CuratorFramework client, String parentPath, RpcSubscriber rpcSubscriber,
        String interfaceName) {
        this.client = client;
        this.parentPath = parentPath;
        this.rpcSubscriber = rpcSubscriber;
        this.interfaceName = interfaceName;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            final List<String> children = client.getChildren().usingWatcher(this).forPath(parentPath);
            RpcZookeeperRegistry.notify(interfaceName, rpcSubscriber, children);
        } catch (Exception e) {
            log.error("watcher processing event occurred error", e);
            throw new RpcException(e);
        }
    }
}
