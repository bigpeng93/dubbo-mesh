package com.alibaba.dubbo.performance.demo.agent.dubbo.model;

import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HostHolder {

    private static ThreadLocal<Endpoint> endpoints = new ThreadLocal<Endpoint>();

    public Endpoint getEndpoint() {
        return endpoints.get();
    }

    public void setEndpoints(Endpoint endpoint) {
        endpoints.set(endpoint);
    }

    public void clear(){
        endpoints.remove();
    }
}
