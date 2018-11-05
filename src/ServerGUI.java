import java.awt.*;
import java.awt.event.*;

class ServerGUI {
    private Frame frame;
    private Button sendBut;
    private Dialog dialog;
    private TextArea textArea;
    private TextField textField;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem start;
    private MenuItem loginList;
    private MenuItem closeServer;
    private MenuItem close;

    private ServerGUI() {
        init();
    }
    public static void main(String[] args) {
        new ServerGUI();
    }
    //GUI初始化
    private void init() {
        //建立并初始化框架和组件
        frame = new Frame("服务端");
        frame.setLayout(new FlowLayout());
        frame.setBounds(500,300,800,600);
        textArea = new TextArea(21,70);
        textArea.setFont(new Font("黑体",Font.PLAIN,18));
        textArea.isEditable();
        textField = new TextField(65);
        textField.setFont(new Font("黑体",Font.PLAIN,18));
        sendBut = new Button("发送");
        menuBar = new MenuBar();
        menu = new Menu("菜单");
        start = new MenuItem("开启服务器");
        loginList = new MenuItem("已连接用户");
        closeServer = new MenuItem("关闭服务器");
        close = new MenuItem("退出系统");


        //将组件添加至框架
        frame.add(textArea);
        frame.add(textField);
        frame.add(sendBut);
        frame.setMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(start);
        menu.add(loginList);
        menu.add(closeServer);
        menu.add(close);

        //开启事件监听
        frameEvent();

        //显示框架
        frame.setVisible(true);
        System.out.println("GUI初始化完成");
    }
    //事件监听方法
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
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    String msg = textField.getText();
                    //如果输入栏没有内容，弹出提示框
                    if (msg.equals(""))
                        showDialog("发送的消息不能为空");
                    else {
                        ServerTools.sendMsg("服务器："+msg);
                        textArea.append(msg+"\r\n");
                        textField.setText("");
                    }
                }
            }
        });
        //Action监听：点击发送按钮发送消息
        sendBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = textField.getText();
                //如果输入栏没有内容，弹出提示框
                if (msg.equals(""))
                    showDialog("发送的消息不能为空");
                else {
                    ServerTools.sendMsg("服务器："+msg);
                    textArea.append(msg+"\r\n");
                    textField.setText("");
                }
            }
        });

        //Action监听：菜单项对应的处理方式
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                if (s.equals("开始")) {
                    new ServerThread(10000,textArea).start();
                }
                if (s.equals("登陆")) {}
                if (s.equals("登出")) {}
                if (s.equals("关闭")) {
                    System.exit(0);
                }
            }
        };

        start.addActionListener(al);
        loginList.addActionListener(al);
        closeServer.addActionListener(al);
        close.addActionListener(al);
    }
    //弹出对话框方法(可以放init中初始化，这里会慢点)
    private void showDialog(String msg) {
        //初始化对话框
        dialog = new Dialog(frame,"提示",false);
        dialog.setBounds(780,500,250,130);
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
}