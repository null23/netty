package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyServerHandler extends ChannelHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestBuffer = (ByteBuf) msg;
        byte[] requestBytes = new byte[requestBuffer.readableBytes()];
        requestBuffer.readBytes(requestBytes);

        String requestData = new String(requestBytes, "UTF-8");
        System.out.println("接收到的请求：" + requestData);

        String response = "收到你的请求，返回响应给你";
        ByteBuf responseBuffer = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseBuffer);

        // 类似对应着之前的 Processor 线程，负责读取请求，返回响应
        // 或者是 Handler，因为 Netty 其实自己就有 Processor 线程
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
