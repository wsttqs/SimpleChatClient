import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread extends Thread {
    private int port;
    private TextArea textArea;

    ServerThread(int port, TextArea textArea) {
        this.port = port;
        this.textArea = textArea;
    }
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket client = ss.accept();
                new ServerRicThread(client,textArea).start();
                ServerTools.addList(client);
            }
        } catch (IOException e) {
            System.out.println("客户端连接失败");
            e.printStackTrace();
        }
    }
}
