package cn.huangyongyi;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author huangyongyi.
 * @date 2018/4/14
 * @desc:
 */
public class TomcatTest1 {




    /**
     * 最基本的版本
     *
     * @param args
     */
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        int amount = 0;
        try {
            //1.创建一个绑定到1234端口的服务器套接字
            serverSocket = new ServerSocket(1234);
            System.out.println("==服务端已经开启啦，等待用户链接==");
            //侦听并接受到这个套接字的链接,死循环监听
            while (true) {
                socket = serverSocket.accept();
                if(socket!=null){
                    amount++;
                    System.out.println("有用户-》" + amount + "《-链接过来了");
                    //获取请求的信息
                    InputStream inputStream = socket.getInputStream();
                    byte[] b=new byte[1024];
                    int length = inputStream.read(b);
                    if(length>0){
                        System.out.println("请求的信息：" + new String(b,0,length));
                        //返回给用户响应信息
                        OutputStream outputStream = socket.getOutputStream();
                        PrintStream writer = new PrintStream(outputStream, true);
                        writer.println("HTTP/1.0 200 OK");
                        writer.println("Content-Type:text/html;charset=utf-8");
                        writer.println();
                        //禁止自动请求icon，会触发两次accept
                        writer.println("<html><head><link rel='icon' href='data:;base64,='></head><body>");
                        writer.println("我是响应体信息");
                        writer.println("</body></html>");
                        writer.println();
                        writer.close();
                        outputStream.close();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
