package com.easy4coding.dubbo.scan;

import com.easy4coding.dubbo.scan.config.DubboConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author dmz
 * @date Create in 7:58 下午 2021/12/23
 */
public class IssueMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DubboConfig.class);
        System.out.println(ac.getBean("demoServiceImpl1"));
        System.out.println(ac.getBean("demoServiceImpl2"));
    }

}
