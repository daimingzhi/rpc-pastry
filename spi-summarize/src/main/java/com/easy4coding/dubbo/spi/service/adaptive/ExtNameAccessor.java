package com.easy4coding.dubbo.spi.service.adaptive;

/**
 * @author dmz
 * @date Create in 5:31 下午 2021/12/18
 */
public interface ExtNameAccessor {
    /**
     * 通过这个方法暴露真实要使用的扩展点名称
     */
    String getExtName();
}
