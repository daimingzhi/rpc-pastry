package com.easy4coding.rpc.registry.redis;

import com.easy4coding.rpc.registry.Registry;
import com.easy4coding.rpc.registry.RpcInstance;
import com.easy4coding.rpc.registry.RpcSubscriber;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis注册中心，服务注册时，key为服务名，value为服务的元信息：ip、port
 *
 * 消费者跟服务提供者通过redis的发布-订阅机制进行通信
 *
 * @author dmz
 * @date Create in 7:11 下午 2023/3/9
 */
@Slf4j
public class RpcRedisRegistry implements Registry {

    private final String REGISTER = "register";
    private final String UNREGISTER = "unregister";
    private final String host;
    private final int port;
    private final Jedis jedis;
    /**
     * 注册有效时间,默认30s,30s内没有续约,则视为服务下线
     */
    private final long effectiveTime = 30 * 1000;
    private final ScheduledExecutorService deferExecutor = Executors.newScheduledThreadPool(1);
    private List<RpcInstance> registered = new ArrayList<>();

    public RpcRedisRegistry(String host, int port) {
        this.host = host;
        this.port = port;
        this.jedis = new Jedis(host, port);

        // 续约
        deferExecutor.scheduleAtFixedRate(() -> {
            for (RpcInstance rpcInstance : registered) {
                jedis.hset(rpcInstance.getInterfaceName(), rpcInstance.getHost() + ":" + rpcInstance.getPort(),
                    System.currentTimeMillis() + effectiveTime + "");
            }
        }, 15 * 1000, 15 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void register(RpcInstance rpcInstance) {

        // 保存服务信息到redis
        jedis.hset(rpcInstance.getInterfaceName(), rpcInstance.getHost() + ":" + rpcInstance.getPort(),
            System.currentTimeMillis() + effectiveTime + "");

        registered.add(rpcInstance);

        // 发布服务订阅事件
        jedis.publish(rpcInstance.getInterfaceName(), REGISTER);
    }

    @Override
    public void unRegister(RpcInstance rpcInstance) {

        // 删除redis中的对应服务信息
        jedis.hdel(rpcInstance.getInterfaceName(), rpcInstance.getHost() + ":" + rpcInstance.getPort());

        registered.remove(rpcInstance);

        // 发布服务下线事件
        jedis.publish(rpcInstance.getInterfaceName(), UNREGISTER);
    }

    @Override
    public void subscribe(String interfaceName, RpcSubscriber rpcListener) {

        // 首次订阅，拉取所有provider
        notify(interfaceName, System.currentTimeMillis(), rpcListener);

        Executors.newSingleThreadExecutor().submit(() -> {
            // 启动监听,blocking
            jedis.subscribe(new SubscribeTask(rpcListener), interfaceName);
        });

    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    private void notify(String interfaceName, long now, RpcSubscriber rpcSubscriber) {
        final Map<String, String> rpcProviderMetadata = jedis.hgetAll(interfaceName);
        List<RpcInstance> rpcInstances = new ArrayList<>();
        for (Map.Entry<String, String> rpcInstanceEntry : rpcProviderMetadata.entrySet()) {
            final String timestamp = rpcInstanceEntry.getValue();
            if (Long.parseLong(timestamp) >= now) {
                final String[] hostAndPort = rpcInstanceEntry.getKey().split(":");
                final String host = hostAndPort[0];
                final int port = Integer.parseInt(hostAndPort[1]);
                RpcInstance rpcInstance = new RpcInstance(interfaceName, host, port);
                rpcInstances.add(rpcInstance);
            }
        }
        rpcSubscriber.notify(rpcInstances);
    }

    private class SubscribeTask extends JedisPubSub {

        private final RpcSubscriber rpcSubscriber;

        public SubscribeTask(RpcSubscriber rpcSubscriber) {
            this.rpcSubscriber = rpcSubscriber;
        }

        @Override
        public void onMessage(String interfaceName, String message) {
            if (REGISTER.equals(message) || UNREGISTER.equals(message)) {
                // 首次订阅，拉取所有provider
                log.debug("client accept message:{},channel:{}", message, interfaceName);
                RpcRedisRegistry.this.notify(interfaceName, System.currentTimeMillis(), rpcSubscriber);
            }
        }
    }
}
