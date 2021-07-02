package myTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveMethod implements Runnable {

    private BufferedReader br ;
    private Boolean isRunning = true;

    public ReceiveMethod(Socket client){
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            CloseUtil.closeAll(br);
        }
    }

    public String receive(){
        String getMsg = "";
        try {
            getMsg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            CloseUtil.closeAll(br);
            //关闭流
        }
        return getMsg;
    }


    @Override
    public void run() {
        while(isRunning){
            System.out.println(receive());
        }
    }
}
