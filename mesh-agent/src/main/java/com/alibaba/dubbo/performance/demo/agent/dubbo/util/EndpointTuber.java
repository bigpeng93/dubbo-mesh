package com.alibaba.dubbo.performance.demo.agent.dubbo.util;

import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;

/**
 * 用于绑定endpoint和time
 */
public class EndpointTuber {
    private Endpoint endpoint;
    private Long time;

    public EndpointTuber(Endpoint endpoint,Long time) {
        this.endpoint = endpoint;
        this.time = time;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
