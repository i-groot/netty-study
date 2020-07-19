package com.abc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SomeServerHandle extends ChannelInboundHandlerAdapter {

    // 当Channel被激活后会执行该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("From Client: begin talking.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将来自于客户端的数据显示在服务端控制台
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        // 向客户端发送数据
        ctx.channel().writeAndFlush("From Server: " + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
