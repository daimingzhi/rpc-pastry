package com.easy4coding.spring;

import com.easy4coding.spring.service.SpringSpiInterface;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author dmz
 * @date Create in 6:12 下午 2021/12/18
 */
public class SpringSpi {
    public static void main(String[] args) {
        final List<SpringSpiInterface> springSpiInterfaces = SpringFactoriesLoader.loadFactories(SpringSpiInterface.class, SpringSpi.class.getClassLoader());
        springSpiInterfaces.forEach(
                SpringSpiInterface::say
        );
    }
}
