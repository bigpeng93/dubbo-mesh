package com.alibaba.dubbo.performance.demo.agent.dubbo.util;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.HostHolder;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 创建EndPoint的全局变量，用于缓存EndPoint
 */

@Service
public class EndPointUtil {

    @Autowired
    HostHolder hostHolder;

    IRegistry registry = new EtcdRegistry(PropertyUtil.ETCD);
    public volatile EndpointTuber bestEndpointer;
    public List<Endpoint> endpoints= new LinkedList<>();
    public void getEndpoints(){
        if (null == endpoints) {
            synchronized (EndPointUtil.class) {
                if (null == endpoints) {
                    try {
                        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
