package myTest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.print("请输入昵称：");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String nickName = input.readLine();
        if(nickName.equals("")){
            return;
        }
        //创建客户端的发送消息与接收消息而且都是线程
        Socket client = new Socket("localhost",9988);
        new Thread(new SendMethod(client,nickName)).start();
        new Thread(new ReceiveMethod(client)).start();
    }
}
