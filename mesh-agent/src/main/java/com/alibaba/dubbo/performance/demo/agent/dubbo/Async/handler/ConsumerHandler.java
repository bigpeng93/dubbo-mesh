package com.alibaba.dubbo.performance.demo.agent.dubbo.Async.handler;

import com.alibaba.dubbo.performance.demo.agent.dubbo.Async.EventHandler;
import com.alibaba.dubbo.performance.demo.agent.dubbo.Message.ConMsgClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.SpringContextUtil;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现Consumer端的负载均衡
 *
 */

@Component
public class ConsumerHandler implements EventHandler {
    Map<Endpoint,Long> endpointMap = new ConcurrentHashMap<>();
    private ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextUtil.getBean("threadPoolTaskExecutor");
    @Override
    public void doHandle() {
        for (Endpoint endpoint:EndPointUtil.endpoints){
            ConMsgClient conMsgClient = new ConMsgClient(endpoint,endpointMap);
            executor.execute(conMsgClient);
        }
    }
}
