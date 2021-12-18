package com.easy4coding.jdk.spi;

import com.easy4coding.jdk.spi.service.SpiService;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author dmz
 * @date Create in 3:11 下午 2021/12/12
 */
public class JdkSPI {

    public static void main(String[] args) {
        final ServiceLoader<SpiService> load = ServiceLoader.load(SpiService.class);
        final Iterator<SpiService> iterator = load.iterator();
        while (iterator.hasNext()) {
           iterator.next().say("hello");
        }
    }

}
