package com.easy4coding.rpc;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.api.DmzService;
import com.easy4coding.rpc.api.entity.Book;
import com.easy4coding.rpc.consumer.proxy.RpcProxyFactory;

/**
 * @author dmz
 * @date Create in 4:10 下午 2023/3/5
 */
public class RpcClient {

    public static void main(String[] args) {
        // consumer生成代理对象，发起rpc调用
        final DmzService proxy = RpcProxyFactory.createProxy(DmzService.class, "127.0.0.1", 8080);
        proxy.say("hello");
        proxy.borrow(new Book("big book", "big"));
        final Book buy = proxy.buy(100);
        System.out.println("client buy a book：" + JSON.toJSONString(buy));
    }

}
