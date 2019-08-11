package com.interview.javabasic.io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOPlainEchoServer {

    public void server(int port) throws IOException {
        System.out.println("Listening for connections on port" + port);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 通过创建的channel去创建ServerSocket实例
        ServerSocket ss = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        // 将ServerSocket绑定到指定的端口里
        ss.bind(address);
        // 将serverChannel的阻塞状态设置为非阻塞状态，因此阻塞模式下，注册操作是不允许的，会抛出异常
        serverChannel.configureBlocking(false);
        // 创建一个selector, 作为一个类似调度员的角色
        Selector selector = Selector.open();
        // 将channel注册到Selector里，并说明让Selector关乎的点，这里是关注建立连接这个事件
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            try {
                // 阻塞等待就绪的Channel，即没有与客户端建立连接前就一直轮训
                // 类似自旋
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                //
                break;
            }

            // 注意前面每将一个channel注册到selector，就会产生一个selectedKey
            // 获取到Selector里所有就绪的SelectedKey实例，
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                // 将就绪的SelectedKey从Selector中移除，因为马上就要去处理它，防止重复执行
                iterator.remove();
                try {
                    // 若SelectedKey 处于Acceptable状态，即处于建立连接状态
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        // 就去接收客户端的连接
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connetion from" + client);
                        client.configureBlocking(false);
                        // 再次调用selector, 向selector注册socketChannel, 主要关注读写，并传入一个ByteBuffer实例供读写缓存
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, ByteBuffer.allocate(100));
                    }

                    // 若SelectedKey处于可读状态
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        // 从channel里读取数据存入到ByteBuffer里面
                        client.read(output);
                    }

                    // 若SelectedKey处于可写状态
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        // 反转，之前是被写入，现在是从ByteBuffer写出
                        output.flip();
                        // 将ByteBuffer里的数据写入到Channel里面去
                        client.write(output);
                        // 将已经编写完的数据从缓存中移出
                        output.compact();
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        cex.printStackTrace();
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        NIOPlainEchoServer server = new NIOPlainEchoServer();
        try {
            server.server(9696);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
