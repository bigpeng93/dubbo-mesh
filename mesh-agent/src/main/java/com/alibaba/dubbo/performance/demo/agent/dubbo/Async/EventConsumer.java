package com.alibaba.dubbo.performance.demo.agent.dubbo.Async;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.watch.WatchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.text.MessageFormat;
import java.util.List;

/**
 * 用于Consumer端的获取etcd服务列表，并创建线程通过Watcher循环监听etcd
 */

public class EventConsumer implements InitializingBean,ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;
    private final String rootPath = "dubbomesh";
    private final String serviceName = "com.alibaba.dubbo.performance.demo.provider.IHelloService";
    @Override
    public void afterPropertiesSet() throws Exception {
        String type = System.getProperty("type");
        EventHandler handler = (EventHandler) applicationContext.getBean(type+"handler.class");
        if (handler == null){
            throw new RuntimeException("no type or handler");
        }else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Client client = Client.builder().endpoints(System.getProperty("etcd.url")).build();
                        String strKey = MessageFormat.format("/{0}/{1}",rootPath,serviceName);
                        ByteSequence key  = ByteSequence.fromString(strKey);
                        Watch.Watcher watcher = client.getWatchClient().watch(key);
                        try {
                            List<WatchEvent> event = watcher.listen().getEvents();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.doHandle();
                    }
                }
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
