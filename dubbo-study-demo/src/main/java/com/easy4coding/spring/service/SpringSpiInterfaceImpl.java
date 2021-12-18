package com.easy4coding.spring.service;

/**
 * @author dmz
 * @date Create in 6:12 下午 2021/12/18
 */
public class SpringSpiInterfaceImpl implements SpringSpiInterface {
    @Override
    public void say() {
        System.out.println("hello spring spi");
    }
}
