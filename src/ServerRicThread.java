import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerRicThread extends Thread {
    private Socket socket;
    private TextArea textArea;

    ServerRicThread(Socket client, TextArea textArea) {
        this.socket = client;
        this.textArea = textArea;
    }
    public void run() {
        try {
            //获取用户IP
            String UserIp = socket.getLocalAddress().getHostAddress();
            ServerTools.sendMsg("欢迎 "+UserIp+" 加入聊天室");
            textArea.append("欢迎 "+UserIp+" 加入聊天室"+"\r\n");
            //获取网络输入缓冲区
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line=in.readLine())!=null) {
                String msg = UserIp+"："+line;
                ServerTools.sendMsg(msg);
                textArea.append(msg+"\r\n");
            }
        } catch (IOException e) {
            System.out.println("读取客户端消息失败"+e);
            e.printStackTrace();
        }
    }
}
