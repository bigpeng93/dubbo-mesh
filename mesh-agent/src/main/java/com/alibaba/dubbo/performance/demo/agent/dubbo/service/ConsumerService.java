package com.alibaba.dubbo.performance.demo.agent.dubbo.service;

import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 用于处理consumer的事务处理
 */
@Service
public class ConsumerService {

    private OkHttpClient httpClient = new OkHttpClient();

    public Integer consumer(String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {

        // 简单的负载均衡，随机取一个
        Endpoint endpoint = EndPointUtil.bestEndpointer.getEndpoint();

        String url =  "http://" + endpoint.getHost() + ":" + endpoint.getPort();

        RequestBody requestBody = new FormBody.Builder()
                .add("interface",interfaceName)
                .add("method",method)
                .add("parameterTypesString",parameterTypesString)
                .add("parameter",parameter)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            byte[] bytes = response.body().bytes();
            return JSON.parseObject(bytes, Integer.class);
        }
    }

}
