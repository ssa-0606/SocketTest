package myTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<MyChannel> all = new ArrayList<>();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(9988);
            while(true){
                Socket client = serverSocket.accept();
                MyChannel myChannel = new MyChannel(client);
                all.add(myChannel);
                new Thread(myChannel).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class MyChannel implements Runnable {

        private BufferedReader br ;
        private PrintStream ps;
        private Boolean isRunning = true;
        private String name ;

        public MyChannel(Socket client){
            try {
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                ps = new PrintStream(client.getOutputStream());
                this.name = br.readLine();
                this.send("欢迎来到聊天室");
                sendOthers(this.name+"进入到了聊天室",true);
            } catch (IOException e) {
                e.printStackTrace();
                CloseUtil.closeAll(ps,br);
            }
        }

        public void send(String msg){
            if(null != msg || !msg.equals("")){
               ps.println(msg);
            }
        }

        public String receive(){
            String msg = "";
            try {
                msg = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                isRunning = false;
                //关闭流
                CloseUtil.closeAll(br);
                all.remove(this);
            }
            return msg;
        }

        public void sendOthers(String msg ,Boolean sys){
            if(msg.startsWith("@")&& msg.indexOf(":")>-1){
                String name = msg.substring(1,msg.indexOf(":"));
                String content = msg.substring(msg.indexOf(":")+1);
                for (MyChannel myChannel : all) {
                    if(myChannel.name.equals(name)){
                        myChannel.send(this.name+"对你私聊："+content);
                    }
                }
            }else{
                for (MyChannel myChannel : all) {
                    if(myChannel == this){
                        continue;
                    }
                    if(sys){
                        myChannel.send("系统消息："+msg);
                    }else{
                        myChannel.send(this.name+"对所有人说："+msg);
                    }

                }
            }
        }


        @Override
        public void run() {
            while(isRunning){
                sendOthers(receive(),false);
            }
        }
    }



}
