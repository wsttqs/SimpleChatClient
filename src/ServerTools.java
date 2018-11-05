import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

class ServerTools {
    static ArrayList<Socket> ClientList = new ArrayList<>();
    //不需要创建改类对象，构造函数私有
    private ServerTools(){}
    //将客户端socket添加到list方法
    static void addList(Socket client) {
        ClientList.add(client);
    }
    //发送服务器广播消息
    static void sendMsg(String msg) {
        try {
            for (Socket socket : ClientList) {
                //获取网络输出缓冲区
                PrintStream out = new PrintStream(socket.getOutputStream(),true);
                out.println(msg);
            }
        } catch (IOException e) {
            System.out.println("发送广播消息异常"+e);
            e.printStackTrace();
        }
    }
}
