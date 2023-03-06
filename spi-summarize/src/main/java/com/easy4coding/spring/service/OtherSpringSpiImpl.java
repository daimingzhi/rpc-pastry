package com.easy4coding.spring.service;

import com.easy4coding.spring.condition.SpringSpiCondition;
import org.springframework.context.annotation.Conditional;

/**
 * @author dmz
 * @date Create in 6:18 下午 2021/12/18
 */

@Conditional(SpringSpiCondition.class)
public class OtherSpringSpiImpl implements SpringSpiInterface{
    @Override
    public void say() {
        System.out.println("other say hello spring spi ");
    }
}
