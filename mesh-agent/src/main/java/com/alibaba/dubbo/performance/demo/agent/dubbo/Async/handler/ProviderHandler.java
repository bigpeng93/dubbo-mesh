package com.alibaba.dubbo.performance.demo.agent.dubbo.Async.handler;


import com.alibaba.dubbo.performance.demo.agent.dubbo.Async.EventHandler;
import com.alibaba.dubbo.performance.demo.agent.dubbo.Message.ProMsgServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 创建provider的异步处理，用于处理register的invoke方法
 */
@Component
public class ProviderHandler implements EventHandler {

    ProMsgServer proMsgServer = new ProMsgServer();

    @Override
    public void doHandle() {
        proMsgServer.run();
    }
}
