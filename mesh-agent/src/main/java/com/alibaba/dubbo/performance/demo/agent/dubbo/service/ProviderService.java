package com.alibaba.dubbo.performance.demo.agent.dubbo.service;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import org.springframework.stereotype.Service;

/**
 * 用于处理provider的事务处理
 */
@Service
public class ProviderService {

    public byte[] provider (RpcClient rpcClient, String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {

        Object result = rpcClient.invoke(interfaceName,method,parameterTypesString,parameter);
        return (byte[]) result;
    }
}
