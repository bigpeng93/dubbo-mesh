package com.alibaba.dubbo.performance.demo.agent.dubbo.balance;

import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;

import java.util.List;

public class WeightRoundRobin implements loadbalance {

    @Override
    public Endpoint getEndPoint(List<Endpoint> endpoints) {
        return null;
    }
}
