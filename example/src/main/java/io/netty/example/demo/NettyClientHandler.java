package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyClientHandler extends ChannelHandlerAdapter {

    private ByteBuf requestBuffer;

    public NettyClientHandler() {
        byte[] requestBytes = "你好，发送的第一条消息".getBytes();
        requestBuffer = Unpooled.buffer(requestBytes.length);
        requestBuffer.writeBytes(requestBytes);
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(requestBuffer);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf responseBuffer = (ByteBuf) msg;

        byte[] responseBytes = new byte[responseBuffer.readableBytes()];

        String response = new String(responseBytes, "UTF-8");
        System.out.println("接收到响服务端的响应：" + response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
