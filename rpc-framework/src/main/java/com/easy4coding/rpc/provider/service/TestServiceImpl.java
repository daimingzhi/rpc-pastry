package com.easy4coding.rpc.provider.service;

import com.alibaba.fastjson.JSON;
import com.easy4coding.rpc.api.entity.Book;
import com.easy4coding.rpc.api.TestService;

/**
 * @author dmz
 * @date Create in 12:28 上午 2021/12/12
 */
public class TestServiceImpl implements TestService {

    @Override
    public void say(String words) {
        System.out.println("dmz say " + words);
    }

    @Override
    public void borrow(Book book) {
        System.out.println("dmz borrow a book" + JSON.toJSONString(book));
    }

    @Override
    public Book buy(Integer money) {
        System.out.println("dmz spent " + money + " buy a book!");
        return new Book("网络是如何连接的?", "daLao");
    }

}
