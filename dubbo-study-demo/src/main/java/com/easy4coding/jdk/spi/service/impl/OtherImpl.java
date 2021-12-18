package com.easy4coding.jdk.spi.service.impl;

import com.easy4coding.jdk.spi.service.SpiService;

/**
 * @author dmz
 * @date Create in 3:23 下午 2021/12/12
 */
public class OtherImpl implements SpiService {
    @Override
    public void say(String words) {
        System.out.println("others say " + words);
    }
}
