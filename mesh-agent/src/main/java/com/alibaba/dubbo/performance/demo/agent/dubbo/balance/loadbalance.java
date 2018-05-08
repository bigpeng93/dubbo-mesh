package com.alibaba.dubbo.performance.demo.agent.dubbo.balance;

import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;

import java.util.List;

public interface loadbalance {
    Endpoint getEndPoint(List<Endpoint>endpoints);
}
