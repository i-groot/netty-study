package com.abc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class StartServer {
    public static void main(String[] args) {
        // 用于处理客户端连接请求，将请求发送给childGroup中的eventLoop
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 用于处理客户端请求
        EventLoopGroup childGroup = new NioEventLoopGroup();

        // 用户启动ServerChannel
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(parentGroup, childGroup) // 指定eventLoopGroup
                .channel(NioServerSocketChannel.class) // 指定使用NIO进行通信
                // 管道初始化器
                .childHandler(new SomeChannelInitializer());

        try {
            // 指定当前服务器所监听的端口号
            // bind()方法执行是异步的
            // sync()方法会使bind()操作与后续的代码执行由异步变成同步
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("Server start success. Port is 8888.");
            // 关闭Channel
            // closeFuture()方法是异步的。
            // 当Channel调用了close()方法并关闭成功才会触发closeFuture()方法的执行
            future.channel().closeFuture().sync(); // 设置同步执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
