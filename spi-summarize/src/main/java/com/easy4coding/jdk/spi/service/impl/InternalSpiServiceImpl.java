package com.easy4coding.jdk.spi.service.impl;

import com.easy4coding.jdk.spi.service.SpiService;

/**
 * @author dmz
 * @date Create in 3:22 下午 2021/12/12
 */
public class InternalSpiServiceImpl implements SpiService {

    static {
        try {
            Class.forName("com.donot.exist.class");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void say(String words) {
        System.out.println("dmz say " + words);
    }
}
