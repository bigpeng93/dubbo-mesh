package com.alibaba.dubbo.performance.demo.agent.dubbo.Async;

import com.alibaba.dubbo.performance.demo.agent.dubbo.Async.handler.ConsumerHandler;
import com.alibaba.dubbo.performance.demo.agent.dubbo.Async.handler.ProviderHandler;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.PropertyUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.SpringContextUtil;
import com.coreos.jetcd.Client;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.watch.WatchEvent;
import com.sun.xml.internal.messaging.saaj.soap.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.MessageFormat;
import java.util.List;

/**
 * 用于Consumer端的获取etcd服务列表，并创建线程通过Watcher循环监听etcd
 */

@Service
public class EventConsumer implements InitializingBean{
    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private final String rootPath = "dubbomesh";
    private final String serviceName = "com.alibaba.dubbo.performance.demo.provider.IHelloService";

    @Autowired
    ConsumerHandler consumerHandler;

    @Autowired
    ProviderHandler providerHandler;

    @Autowired
    EndPointUtil endPointUtil;

    @Override
    public void afterPropertiesSet() throws Exception {
        //缓存endpoint
        endPointUtil.getEndpoints();
        //根据配置信息，调用相应的handler
        if (PropertyUtil.TYPE.equals("consumer")){
                consumerHandler.doHandle(endPointUtil);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Client client = Client.builder().endpoints(PropertyUtil.TYPE).build();
                            String strKey = MessageFormat.format("/{0}/{1}", rootPath, serviceName);
                            ByteSequence key = ByteSequence.fromString(strKey);
                            Watch.Watcher watcher = client.getWatchClient().watch(key);
                            try {
                                List<WatchEvent> event = watcher.listen().getEvents();
                                if (event != null) {
                                    endPointUtil.getEndpoints();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();

        }else if (PropertyUtil.TYPE.equals("provider")){
            providerHandler.doHandle();
        }
    }
}
