package com.alibaba.dubbo.performance.demo.agent.dubbo.util;

import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * 创建EndPoint的全局变量，用于缓存EndPoint
 */
public class EndPointUtil {
    static Object lock = new Object();
    static IRegistry registry = new EtcdRegistry(PropertyUtil.ETCD);
    public static volatile EndpointTuber bestEndpointer;
    public static List<Endpoint> endpoints= new LinkedList<>();
    public static void getEndpoints(){
        if (null == endpoints) {
            synchronized (lock) {
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
