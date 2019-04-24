package cn.szlee.mail.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <b><code>SocketUtil</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-24 14:43.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
public class SocketUtil {

    public static void sender() throws IOException {
        Socket socket = new Socket("localhost", 6324);

        //获取输出流，向服务器端发送信息
        //字节输出流
        OutputStream os = socket.getOutputStream();
        //将输出流包装为打印流
        PrintWriter pw = new PrintWriter(os);
        pw.write("我是Java服务器");
        pw.flush();
        socket.shutdownOutput();//关闭输出流

        socket.close();
    }

    public static void main(String[] args) throws IOException {
        sender();
    }
}
