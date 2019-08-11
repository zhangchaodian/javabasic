package com.interview.javabasic.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AIOPlainEchoServer {

    public static void main(String[] args) {
        AIOPlainEchoServer server = new AIOPlainEchoServer();
        try {
            server.server(9696);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 最大的不同是没有用到selector，而是直接通过ServerChannel
    public void server(int port) throws IOException {
        System.out.println("Listening for connections on port" + port);
        // AsynchronousServerSocketChannel对应于NIO的SErverSocketChannel
        final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(port);
        // 将ServerSocket绑定到指定的端口
        serverChannel.bind(address);
        final CountDownLatch latch = new CountDownLatch(1);
        // 开始接收新的客户端请求，一旦一个客户端请求接收，CompletionHandler就会被调用
        // 关键在于指定CompletionHandler的回调接口，在channel的read、accept和write关键节点通过事件机制去调用
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            // 这里的AsynchronousSocketChannel对应NIO的客户端的SocketChannel
            @Override
            public void completed(final AsynchronousSocketChannel channel, Object attachment) {
                // 一旦完成处理，即客户端ready，再次接收新的客户端请求
                serverChannel.accept(null, this);
                System.out.println("Accepted connetion from" + channel);
                ByteBuffer buffer = ByteBuffer.allocate(100);
                // 在channel里植入一个读操作EchoCompletionHandler，一旦buffer有数据写入，EchoCompletionHandler就会被唤醒调用
                // 类似回调函数
                channel.read(buffer, buffer, new EchoCompletionHandler(channel));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                try {
                    serverChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private final class EchoCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private final AsynchronousSocketChannel channel;

        EchoCompletionHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            buffer.flip();
            channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (attachment.hasRemaining()) {
                        // 如果buffer还有内容，则再次触发写入操作将buffer里的内容写入channel
                        channel.write(buffer, buffer, this);
                    } else {
                        // 将之前已经写入Channel里的内容清除下
                        buffer.compact();
                        // 如果channel里还有内容需要读入到buffer里，则再次触发写入操作将channel里的内容读入buffer
                        channel.read(buffer, buffer, EchoCompletionHandler.this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                    }
                }
            });
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                channel.close();
            } catch (IOException e) {
            }
        }
    }

}
