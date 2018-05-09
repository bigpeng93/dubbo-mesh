package com.alibaba.dubbo.performance.demo.agent.dubbo.service;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.PropertyUtil;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import org.springframework.stereotype.Service;

/**
 * 用于处理provider的事务处理
 */
@Service
public class ProviderService {

    private IRegistry registry = new EtcdRegistry(PropertyUtil.ETCD);

    private RpcClient rpcClient = new RpcClient(registry);

    public byte[] provider(String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {

        Object result = rpcClient.invoke(interfaceName,method,parameterTypesString,parameter);
        return (byte[]) result;
    }
}
