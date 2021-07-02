package myTest;

import java.io.*;
import java.net.Socket;

public class SendMethod implements Runnable {

    private final BufferedReader input ;
    private PrintStream ps ;
    private Boolean isRunning = true;

    //构造器里面实例化我们的流和其他数据

    public SendMethod(){
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public SendMethod(Socket client ,String msg){
        this();
        try {
            ps = new PrintStream(client.getOutputStream());
            send(msg);
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            CloseUtil.closeAll(ps);
            // TODO: 2021/7/2 如果出啦异常我要在这里关闭流,因考虑到之后还有很多关闭流的操作，所以计划写一个工具类专门关闭流
        }

    }

    public String sendMsgToServer(){
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            CloseUtil.closeAll(input);
        }
        return "";
    }


    /**
     * 这里就是从客户端发送信息到服务端
     * @param msg
     */
    public void send(String msg){
        if(null!=msg && !"".equals(msg)){
            ps.println(msg);
        }
    }

    @Override
    public void run() {
        while(isRunning){
            send(sendMsgToServer());
        }
    }
}
