package com.alibaba.dubbo.performance.demo.agent.dubbo.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ConMsgHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Logger logger = LoggerFactory.getLogger(ConMsgHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String dateFormat =new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date);
        ctx.writeAndFlush(dateFormat);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String preCalDate = msg.toString(CharsetUtil.UTF_8);
        SimpleDateFormat preSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS");
        Date preDate = preSimpleDateFormat.parse(preCalDate);

        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();

        long val=  nowDate.getTime() - preDate.getTime();

        //System.out.println(val);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
