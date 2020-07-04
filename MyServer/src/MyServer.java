import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static  void  main(String []args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(10000);
        Socket  socket  = serverSocket.accept();
        DataInputStream dis =  new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((System.in)));
        dos.writeUTF(socket.getInetAddress()+"已经成功连接到服务器");
        String info;
        String str;
        Boolean flag= true;
        while(flag){
            str = dis.readUTF();
            System.out.println("client:"+str);
            info = bufferedReader.readLine();
            dos.writeUTF(info);
            System.out.println("server:"+info);
        }
        dos.close();
        dis.close();
        socket.close();
        serverSocket.close();
    }
}
