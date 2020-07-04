import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyClient {
    public static  void  main(String []args) throws Exception{
        //创建套接字
        Socket  socket =new Socket("127.0.0.1",10001);
        DataInputStream dis =  new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((System.in)));
        String info;
        String str;
        Boolean flag= true;
        str = dis.readUTF();
        System.out.println("server:"+str);
        while(flag){
            info = bufferedReader.readLine();
            System.out.println("client:"+info);
            dos.writeUTF(info);
            dos.flush();
            str = dis.readUTF();
            System.out.println("server:"+str);
        }
        dis.close();
        dos.close();
        socket.close();
    }
}
