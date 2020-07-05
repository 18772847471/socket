package com.hb.nio.groupchat;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupchatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT =6667;

    public GroupchatServer(){
        try{
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector,SelectionKey.OP_ACCEPT);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void  listen(){
        try{
            while(true){
                int count = selector.select();
                if(count>0){
                   Iterator<SelectionKey> iterator= selector.selectedKeys().iterator();
                   while(iterator.hasNext()){
                       SelectionKey key = iterator.next();
                       if(key.isAcceptable()){
                           SocketChannel sc = listenChannel.accept();
                          sc.configureBlocking(false);
                           sc.register(selector,SelectionKey.OP_READ);
                           System.out.println(sc.getRemoteAddress()+"上线");
                       }
                       if(key.isReadable()){
                           readData(key);
                       }
                       iterator.remove();
                   }
                }else{
                    System.out.println("等待中。。。");
                }
            }
        }catch (Exception e){

        }
    }

    public void readData(SelectionKey key){
        SocketChannel channel =null;
        try{
            channel =(SocketChannel)key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            if (count>0){
               String msg =  new String(byteBuffer.array());
               System.out.println("from 客户端：" + msg);
               sennInfoToOther(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了。。");
                key.cancel();
                channel.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }

        }
    }

    public void sennInfoToOther(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息");
        for(SelectionKey key:selector.keys()){
           Channel targetChannel =  key.channel();
           if(targetChannel instanceof SocketChannel &&targetChannel != self){
               SocketChannel dest = (SocketChannel)targetChannel;
               ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
               dest.write(buffer);
           }
        }
    }

    public static void main(String[] args){
        GroupchatServer groupchatServer = new GroupchatServer();
        groupchatServer.listen();
    }
}
