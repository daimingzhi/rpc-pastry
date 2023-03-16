package com.easy4coding.rpc.registry;

/**
 * @author dmz
 * @date Create in 3:01 下午 2023/3/5
 */
public interface Registry {

    String getHost();

    int getPort();

    /**
     * 注册一个rpc实例
     */
    void register(RpcInstance rpcInstance);

    /**
     * 注册一个rpc实例
     */
    void unRegister(RpcInstance rpcInstance);

    /**
     * 订阅指定的服务
     */
    void subscribe(String interfaceName, RpcSubscriber rpcListener);

}
