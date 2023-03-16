package com.easy4coding.rpc.registry;

import lombok.Data;

/**
 * @author dmz
 * @date Create in 10:44 下午 2023/3/8
 */
@Data
public class RpcInstance {

    private String interfaceName;

    private String host;

    private int port;

    public RpcInstance(String interfaceName, String host, int port) {
        this.interfaceName = interfaceName;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RpcInstance that = (RpcInstance)o;

        if (port != that.port)
            return false;
        if (interfaceName != null ? !interfaceName.equals(that.interfaceName) : that.interfaceName != null)
            return false;
        return host != null ? host.equals(that.host) : that.host == null;
    }

    @Override
    public int hashCode() {
        int result = interfaceName != null ? interfaceName.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
