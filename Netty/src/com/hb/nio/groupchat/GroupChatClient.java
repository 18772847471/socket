package com.hb.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private final  String HOST ="127.0.0.1";
    private final  int PORT =6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient()throws IOException {
        selector= Selector.open();
        socketChannel = socketChannel.open(new InetSocketAddress("127.0.0.1",PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"is ok...");
    }

    public  void sendInfo(String info){
        info = username+"说："+info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public  void readInfo(){
        try{
            int readChannels = selector.select();
            if(readChannels>0){
              Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
              while(iterator.hasNext()){
                  SelectionKey key = iterator.next();
                  if(key.isReadable()){
                     SocketChannel sc = (SocketChannel)key.channel();
                     ByteBuffer buffer =ByteBuffer.allocate(1024);
                     sc.read(buffer);
                     String msg = new String(buffer.array());
                     System.out.println(msg.trim());
                  }
                  iterator.remove();
              }
            }else{

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        GroupChatClient chatClient =new GroupChatClient();
        new Thread(){
            public void  run(){
                while (true){
                    chatClient.readInfo();
                    try{
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
