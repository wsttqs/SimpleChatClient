import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

class SimpleChatClient {
    private Frame frame;
    private Button sendBut;
    private Dialog dialog;
    private TextArea textArea;
    private TextField textField;

    private BufferedReader in;
    private PrintStream out;

    private SimpleChatClient() {
        initGUI();
        initNET();
    }
    public static void main(String[] args) {
        new SimpleChatClient();
    }
    private void initGUI() {
        //建立并初始化框架和组件
        frame = new Frame("聊天室");
        frame.setLayout(new FlowLayout());
        frame.setBounds(500,300,800,600);
        textArea = new TextArea(21,70);
        textArea.setFont(new Font("黑体",Font.PLAIN,18));
        textArea.isEditable();
        textField = new TextField(65);
        textField.setFont(new Font("黑体",Font.PLAIN,18));
        sendBut = new Button("发送");

        //将组件添加至框架
        frame.add(textArea);
        frame.add(textField);
        frame.add(sendBut);

        //开启事件监听
        frameEvent();

        //显示框架
        frame.setVisible(true);
        System.out.println("GUI初始化完成");
    }
    //框架事件监听方法
    private void frameEvent() {
        //窗口监听，点X时退出程序
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //键盘监听：按下ESC键退出程序
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });
        //键盘监听：按下回车键发送消息
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                    sendMsg();
            }
        });
        //Action监听：点击发送按钮发送消息
        sendBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMsg();
            }
        });
    }
    //弹出对话框方法(可以放init中初始化，这里会慢点)
    private void showDialog(String msg) {
        //初始化对话框
        dialog = new Dialog(frame,"提示",false);
        dialog.setBounds(600,780,250,130);
        dialog.setLayout(new FlowLayout());
        //创建并初始化对话框中组件
        Label label = new Label("提示："+msg);
        label.setFont(new Font("黑体",Font.PLAIN,20));
        Button okBut = new Button("确定");

        //添加组件到对话框
        dialog.add(label);
        dialog.add(okBut);

        //初始化对话框事件监听
        //窗体监听：点X退出对话框
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });
        //Action监听：点击确定按钮退出对话框
        okBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        //键盘监听：按ESC退出对话框
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
                    dialog.dispose();
            }
        });

        //显示对话框
        dialog.setVisible(true);
    }
    //网络连接初始化
    private void initNET() {
        try {
            //新建客户端Socket服务
            Socket socket = new Socket("127.0.0.1",10000);
            //获取网络输入缓冲区
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //获取网络输出缓冲区
            out = new PrintStream(socket.getOutputStream(),true);
            //开启读取网络数据线程
            new Thread(new readThread()).start();
            System.out.println("网络初始化成功");
        } catch (IOException e) {
            System.out.println("网络初始化失败"+e);
            e.printStackTrace();
        }
    }
    //发送网络数据方法
    private void sendMsg() {
        String msg = textField.getText();
        if (msg.equals(""))
            showDialog("发送消息不能为空");
        else {
            out.println(msg);
            textField.setText("");
        }
    }
    //读取网络数据线程内部类
    class readThread implements Runnable {
        public void run() {
            try {
                String line;
                while ((line=in.readLine())!=null) {
                    textArea.append(line+"\r\n");
                }
            } catch (IOException e) {
                System.out.println("读取网络数据失败"+e);
                e.printStackTrace();
            }
        }
    }
}