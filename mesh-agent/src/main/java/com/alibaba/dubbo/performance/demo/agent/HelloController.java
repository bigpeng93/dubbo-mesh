package com.alibaba.dubbo.performance.demo.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.service.ConsumerService;
import com.alibaba.dubbo.performance.demo.agent.dubbo.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ProviderService providerService;

    private Logger logger = LoggerFactory.getLogger(HelloController.class);
    


    /**
     *
     * @param interfaceName         拟调用的服务名。
     * @param method                拟调用的方法
     * @param parameterTypesString  同一个方法名可能会有重载的版本，所以需要指定参数类型来确定方法的签名。
     * @param parameter             传递给 hash 方法的参数值，是 Consumer 生成的一个随机的字符串。
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "")
    public Object invoke(@RequestParam("interface") String interfaceName,
                         @RequestParam("method") String method,
                         @RequestParam("parameterTypesString") String parameterTypesString,
                         @RequestParam("parameter") String parameter) throws Exception {
        String type = System.getProperty("type");   // 获取type参数
        if ("consumer".equals(type)){
            return consumerService.consumer(interfaceName,method,parameterTypesString,parameter);
        }
        else if ("provider".equals(type)){
            return providerService.provider(interfaceName,method,parameterTypesString,parameter);
        }else {
            return "Environment variable type is needed to set to provider or consumer.";
        }
    }
}
