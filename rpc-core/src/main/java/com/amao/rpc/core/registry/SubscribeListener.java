package com.amao.rpc.core.registry;

import com.amao.rpc.core.data.ServiceMeta;

/**
 * Created by 阿毛 on 2016/6/27.
 */
public interface SubscribeListener {
    
    void onServiceOnLine(ServiceMeta serviceMeta);

    void onServiceOffLine(ServiceMeta serviceMeta);
}
