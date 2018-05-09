package com.alibaba.dubbo.performance.demo.agent.dubbo.Async;

import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.PropertyUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.SpringContextUtil;
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

public class EventConsumer implements InitializingBean{
    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private final String rootPath = "dubbomesh";
    private final String serviceName = "com.alibaba.dubbo.performance.demo.provider.IHelloService";
    private EventHandler handler;
    @Override
    public void afterPropertiesSet() throws Exception {
        //缓存endpoint
        EndPointUtil.getEndpoints();
        //根据配置信息，调用相应的handler
        if (PropertyUtil.TYPE.equals("consumer")){
            handler = (EventHandler) SpringContextUtil.getBean("ConsumerHandler.class");
        }else if (PropertyUtil.TYPE.equals("provider")){
            handler = (EventHandler) SpringContextUtil.getBean("ProviderHandler.class");
        }
        if (handler == null){
            throw new RuntimeException("no type or handler");
        }else {

            handler.doHandle();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Client client = Client.builder().endpoints(PropertyUtil.TYPE).build();
                        String strKey = MessageFormat.format("/{0}/{1}",rootPath,serviceName);
                        ByteSequence key  = ByteSequence.fromString(strKey);
                        Watch.Watcher watcher = client.getWatchClient().watch(key);
                        try {
                            List<WatchEvent> event = watcher.listen().getEvents();
                            if (event!=null){
                                EndPointUtil.getEndpoints();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
    }
}
