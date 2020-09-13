package io.netty.example.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class NettyServer {

    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();   // 线程组 -> acceptor 线程
        EventLoopGroup childGroup = new NioEventLoopGroup();    // 线程组 -> processor 线程
        try {
            // Netty 服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(parentGroup, childGroup) // 设置两个线程组

                    // 初始化 NioServerSocketChannel 的工厂，之后会通过反射来使用工厂创建一个 NioServerSocketChannel
                    // 这个 NioServerSocketChannel 其实就是处理 客户端 连接请求的
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024) // 设置一些网络相关的参数
                    .childHandler(new ChildChannelHandler());   // 业务逻辑处理的链条，自己实现的

            ChannelFuture bindFuture = serverBootstrap.bind(50070).sync();  // 同步等待启动服务器监控端口

            bindFuture.channel().closeFuture().sync();  // 同步等待关闭服务器启动的结果
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    /**
     * 用来代表处理每个客户端连接的 SocketChannel
     */
    private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
//            socketChannel.pipeline().addLast(new NettyServerHandler()); // 针对网络请求的处理逻辑
            socketChannel.pipeline().addLast(new InBoundHandlerA());
            socketChannel.pipeline().addLast(new InBoundHandlerB());
            socketChannel.pipeline().addLast(new InBoundHandlerC());
        }
    }
}
