package cn.huangyongyi;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author huangyongyi.
 * @date 2018/4/14
 * @desc:
 */
public class TomcatTest2 {




    /**
     * 改进版本
     * 拦截url，返回你要获取的资源
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
                    Reuqest reuqest = new Reuqest(inputStream);
                    OutputStream outputStream = socket.getOutputStream();
                    Response response=new Response(outputStream);
                    String url = reuqest.url;
                    if(url.endsWith(".html")||url.endsWith(".gif")){
                        response.writeFile(url);
                    }
                    //用户发起的请求是请求业务方法
                    //基本的实现原理是这样，但是每次都要写一个if是不合理的，所以的一般都是在xml定义映射关系，读取xml的配置信息，利用java 的反射，实例化对应的类
                    //eg：
//                    <xml-action>login.action</xml-action>
//                    <xml-action-ref>TomcatTest2.Login.loginAction</xml-action-ref>

                    else if(url.endsWith(".action")){
                        if(url.equals("login.action")){
                            Login login=new Login();
                            login.loginAction(reuqest,response);

                        }
                    }
                    outputStream.close();
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 请求的封装类
     */
    static class Reuqest{

        private String url;



        public Reuqest (InputStream inputStream) throws IOException {
            byte[] b=new byte[1024];
            int length = inputStream.read(b);
            if(length>0) {
                String req=new String(b, 0, length);
                System.out.println("请求的信息：" +req );
                url=req.substring(req.indexOf("/"),req.indexOf("HTTP/1.1")).trim().substring(1);
                System.out.println("请求地址为:"+url);
            }
        }
    }


    /**
     * 响应的封装类
     */
    static class Response{


        OutputStream os=null;

        public Response(OutputStream outputStream) throws IOException {
            this.os=outputStream;



        }

        /**
         * 统一返回
         * @param html
         */
        public void printRes(String html){
            try {
                PrintStream writer = new PrintStream(os, true);
                writer.println("HTTP/1.0 200 OK");
                writer.println("Content-Type:text/html;charset=utf-8");
                writer.println();
                writer.println(html);
                writer.println();
                writer.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * 返回的是文件
         * @param path
         */
        public void returnFileString(String path){
            String fileByPath = FileUtils.getFileByPath(path);
            this.printRes(fileByPath);
        }



        public void writeFile(String path){
            try {
                FileInputStream fileInputStream=new FileInputStream(path);
                byte[] b=new byte[512];
                int length=0;
                while ((length=fileInputStream.read(b))!=-1){
                    os.write(b,0,length);
                }
                fileInputStream.close();
                os.flush();
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static class Login{
        //Reuqest和response是否熟悉？？？？
        public void loginAction(Reuqest reuqest,Response response){
            response.printRes("我的登录方法");
        }
    }




    }
