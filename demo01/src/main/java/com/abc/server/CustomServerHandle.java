package com.abc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

// 自定义服务器处理器
// 需求：用户体骄傲一个请求后，在浏览器上看到hello netty world
public class CustomServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
//        System.out.println("msg: " + msg.getClass());
//        System.out.println("Client Address: " + ctx.channel().remoteAddress());
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.printf("Method: %s \n", request.method().name());
            System.out.printf("Uri: %s \n", request.uri());

            if (request.uri().endsWith(".ico")) {
                System.out.println(".ico结尾的uri不处理");
                return;
            }

            // 构造response的响应体
            ByteBuf data = Unpooled.copiedBuffer("hello netty world", CharsetUtil.UTF_8);
            // 生成响应对象
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, data);
            // 获取response的头部后进行初始化设置
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
            headers.set(HttpHeaderNames.CONTENT_LENGTH, data.readableBytes());
//            ctx.write(response);
//            ctx.flush();
//            ctx.writeAndFlush(response);
            ctx.writeAndFlush(response)
                    // 添加监听器，响应体发送完毕后直接将Channel关闭
                    .addListener(ChannelFutureListener.CLOSE);

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        // 关闭Channel
        ctx.close();
    }
}
