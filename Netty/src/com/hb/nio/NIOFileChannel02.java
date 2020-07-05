package com.hb.nio;


import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String []args) throws Exception{
        File file = new File("D:\\WorkProject\\socket\\file01.txt");
        FileInputStream  fileInputStream =new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        fileChannel.read(byteBuffer);
        System.out.print(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
