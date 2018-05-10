package com.alibaba.dubbo.performance.demo.agent.dubbo.Async.handler;

import com.alibaba.dubbo.performance.demo.agent.dubbo.Async.EventHandler;
import com.alibaba.dubbo.performance.demo.agent.dubbo.Message.ConMsgClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.HostHolder;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.SpringContextUtil;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现Consumer端的负载均衡
 *
 */

@Component
public class ConsumerHandler{

    @Autowired
    HostHolder hostHolder;

    Map<Endpoint,Long> endpointMap = new ConcurrentHashMap<>();
   // private ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextUtil.getBean("threadPoolTaskExecutor");

    public void doHandle(EndPointUtil endPointUtil) {
        if (endPointUtil.endpoints!=null){
            for (Endpoint endpoint:endPointUtil.endpoints){
                ConMsgClient conMsgClient = new ConMsgClient(endpoint,endpointMap);
            }
        }
    }
}
