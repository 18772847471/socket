import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadServer {
    public static void main(String []args) throws Exception{
        ServerSocket  serverSocket =new ServerSocket(10001);
        ExecutorService newCachedThreadPool =  Executors.newCachedThreadPool();
        System.out.println("服务器端启动了");
        while(true){
            System.out.println("线程信息id="+Thread.currentThread().getId()+"名字="+Thread.currentThread().getName());
            System.out.println("等待连接");
            final Socket socket = serverSocket.accept();
            newCachedThreadPool.execute(()->{
                try {
                    handle(socket);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
    }

    public static void handle(Socket socket) throws Exception{
        byte[] bytes = new byte[1024];
        InputStream inputStream=socket.getInputStream();
        while(true){
            //如果管道内没数据会阻塞
            System.out.println("阻塞："+Thread.currentThread().getName());
            int length=inputStream.read(bytes);
            System.out.println(new String(bytes,0,length));
        }
    }
}
