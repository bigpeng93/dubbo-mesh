package com.alibaba.dubbo.performance.demo.agent.dubbo.Message;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.HostHolder;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndPointUtil;
import com.alibaba.dubbo.performance.demo.agent.dubbo.util.EndpointTuber;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


public class ConMsgHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Autowired
    EndPointUtil endPointUtil;

    @Autowired
    HostHolder hostHolder;

    private Endpoint endpoint;
    Map<Endpoint,Long> endpointLongMap = new ConcurrentHashMap<>();

    public ConMsgHandler(Endpoint endpoint, Map<Endpoint, Long> endpointLongMap) {
        this.endpoint = endpoint;
        this.endpointLongMap = endpointLongMap;
    }

    private Long sendTime;
    private Logger logger = LoggerFactory.getLogger(ConMsgHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendTime = System.currentTimeMillis();
        ctx.writeAndFlush(sendTime);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String getMsg = msg.toString(CharsetUtil.UTF_8);

        Long getSendTime = Long.parseLong(getMsg);

        Long currentTime = System.currentTimeMillis();
        Long time=  currentTime - getSendTime;

        EndpointTuber endpointTuber = new EndpointTuber(endpoint,time);
        if (time<endPointUtil.bestEndpointer.getTime()||endPointUtil.bestEndpointer==null)
            hostHolder.setEndpoints(endpointTuber.getEndpoint());

        endpointLongMap.put(endpoint,time);
        ctx.executor().schedule(new HeartBeatTask(ctx),5,TimeUnit.SECONDS);
        //System.out.println(val);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private class HeartBeatTask implements Runnable{
        private ChannelHandlerContext ctx;
        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            Long sendTime = System.currentTimeMillis();
            ctx.writeAndFlush(sendTime);
        }
    }
}
