package com.easy4coding.rpc.api;

import com.easy4coding.rpc.api.entity.Book;

/**
 * @author dmz
 * @date Create in 12:27 上午 2021/12/12
 */
public interface TestService {

    /**
     *  无返回值
     */
    void say(String words);

    /**
     * 有返回值
     */
    Book buy(Integer money);

    /**
     * 复杂参数
     */
    void borrow(Book book);
}
