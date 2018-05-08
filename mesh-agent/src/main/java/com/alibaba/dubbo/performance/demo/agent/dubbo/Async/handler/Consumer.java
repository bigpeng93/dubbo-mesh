package com.alibaba.dubbo.performance.demo.agent.dubbo.Async.handler;

import com.alibaba.dubbo.performance.demo.agent.dubbo.Async.EventHandler;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import org.springframework.stereotype.Component;

/**
 * 实现Consumer端的负载均衡
 *
 */

@Component
public class Consumer implements EventHandler {

    @Override
    public void doHandle() {

    }
}
