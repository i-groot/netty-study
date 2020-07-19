package com.abc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

// 管道（Channel）初始化器
public class SomeChannelInitializer extends ChannelInitializer<SocketChannel> {

    // 当Channel初始创建完毕后就会触发该方法的执行，用于初始化Channel
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 从Channel中获取pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 将HttpServerCodec处理器放入到pipeline的最后
        // HttpServerCodec 是 HttpRequestDecoder 和 HttpResponseEncoder 复合体
        // HttpRequestDecoder：http请求解码器，将channel中的ByteBuf数据解码为HttpRequest对象
        // HttpResponseEncoder：http响应编码器，将HttpResponse对象编码为将要在Channel中发送的ByteBuf数据
        // pipeline.addLast("name_handle", new HttpServerCodec());// 可以给处理器指定名称，后续可以根据名称操作该处理器
        pipeline.addLast(new HttpServerCodec());
        // 将自定义的处理器放入到pipeline的最后
        //指定childGroup中eventLoop所绑定的线程索要处理的处理器
        pipeline.addLast(new SomeServerHandle());
    }
}
