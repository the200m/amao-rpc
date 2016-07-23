package com.amao.rpc.core.data;

import java.net.InetSocketAddress;

/**
 * Created by 阿毛 on 2016/6/27.
 */
public class ServiceMeta {

    private String serviceName; // 服务名称
    private InetSocketAddress providerAddress;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public InetSocketAddress getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(InetSocketAddress providerAddress) {
        this.providerAddress = providerAddress;
    }

    @Override
    public String toString() {
        return "ServiceMeta{" +
                "serviceName='" + serviceName + '\'' +
                ", providerAddress=" + providerAddress +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceMeta)) return false;

        ServiceMeta that = (ServiceMeta) o;

        if (providerAddress != null ? !providerAddress.equals(that.providerAddress) : that.providerAddress != null)
            return false;
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = serviceName != null ? serviceName.hashCode() : 0;
        result = 31 * result + (providerAddress != null ? providerAddress.hashCode() : 0);
        return result;
    }
}