package com.easy4coding.rpc.registry;

import java.util.List;

/**
 * @author dmz
 * @date Create in 11:11 上午 2023/3/10
 */
public interface RpcSubscriber {
    void notify(List<RpcInstance> providers);
}
