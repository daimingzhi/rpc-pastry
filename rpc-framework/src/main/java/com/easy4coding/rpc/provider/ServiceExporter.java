package com.easy4coding.rpc.provider;

import com.easy4coding.rpc.consumer.exception.RpcException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dmz
 * @date Create in 5:42 下午 2023/3/5
 */
public class ServiceExporter {

    private static final Map<String, Object> EXPORTER_MAP = new ConcurrentHashMap<>();

    public static void export(Class<?> clazz, Object target) {
        if (!clazz.isAssignableFrom(target.getClass())) {
            throw new IllegalArgumentException("illegal argument,the object did not implement " + clazz.getName());
        }
        final String interfaceName = clazz.getName();
        EXPORTER_MAP.put(interfaceName, target);
    }

    public static Object getTarget(String serviceName) {
        final Object o = EXPORTER_MAP.get(serviceName);
        if (o == null) {
            throw new RpcException("there is no provider in server");
        }
        return o;
    }
}
