package cn.edu.cqupt.my;


import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static cn.edu.cqupt.my.ActivityCollector.a;


public class UdpSocket implements Runnable{
    private Handler UdpHandler;
    public Handler recHandler;
    private DatagramSocket socket =null;
    private DatagramPacket packet=null;
    private DatagramPacket recpacket=null;
    private byte[] sendbuf=new byte[721];
    private byte[] recbuf= new byte[721];
    public UdpSocket(Handler handler) {
        UdpHandler=handler;
    }
    private boolean flag=true;
    private void CreateUdpSock()
    {
        try {
            socket=new DatagramSocket(35531);
            packet =new DatagramPacket(Struct.getBuf("UD1", MainActivity.GetID(), LoginConfig.getPWD(MainActivity.ctx), "", "", "",""),721,InetAddress.getByName("47.106.207.241"),3566);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void send(byte[] buf)
    {
        try {
            packet =new DatagramPacket(buf,buf.length,InetAddress.getByName("47.106.207.241"),3566);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        UdpStart();
    }
    private void UdpStart()
    {
        CreateUdpSock();
        //send(Struct.getBuf("PNG", MainActivity.GetID(), LoginConfig.getPWD(MainActivity.ctx), "", "", "",""));
        Message msg5=new Message();
        msg5.obj ="CON";
        //msg5.setData(a);
        new Thread() {
            @Override
            public void run() {
                super.run();
                /******/
                String str ;
                String content ;
                String data ;
                String info ;
                String Talktoid ;
                //String flag;
                Bundle a=new Bundle();
                int j=0;
                flag=true;
                while(flag)
                {
                    Message msg2=new Message();
                    recpacket=new DatagramPacket(recbuf,recbuf.length);
                    if(socket==null)
                    {
                        try {
                            socket=new DatagramSocket(35531);
                            j++;
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                        if(j>9)
                        {
                            try {
                                new Thread().sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            j=0;
                        }
                        else
                        {
                            try {
                                new Thread().sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        continue;
                    }
                    else{
                        try {
                            socket.receive(recpacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(recpacket==null)
                    {
                        Message msg3=new Message();
                        msg3.obj ="ERR";
                        msg3.setData(a);
                        UdpHandler.sendMessage(msg3);
                        continue;
                    }
                    else {
                        content= new String(recpacket.getData(),recpacket.getOffset(),recpacket.getLength());
                        if(content.length()>620) {
                            str = content.substring(0, 3);
                            data = content.substring(108, 620);
                            info=content.substring(75,77);
                            Talktoid = content.substring(63, 74);
                            //flag = content.substring(621, 623);
                        }
                        else
                        {
                            str = content.substring(0, 3);
                            data=content;
                            info="";
                            Talktoid = "";
                            //flag ="";
                        }
                        if(str.equals("PNG"))
                        {
                            msg2.obj ="PNG";
                            a.putString("data",data);
                            a.putString("TalkToid",Talktoid);
                            //a.putString("flag",flag);
                            a.putString("info",info);
                        }
                        else if(str.equals("UPD")){
                            Talktoid.equals("11111111111");
                            msg2.obj="UPD";
                            //a.putString("data",data);
                            a.putString("TalkToid",Talktoid);
                        }
                        else {
                            msg2.obj="BUG";
                            a.putString("data",data);
                            a.putString("TalkToid",Talktoid);
                            //a.putString("flag",flag);
                        }
                        msg2.setData(a);
                        UdpHandler.sendMessage(msg2);
                    }
                }
            }
        }.start();
        Looper.prepare();
        recHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                Bundle a=new Bundle();
                if (msg.what == 1) {
                    try {
                        send(Struct.getBuf(msg.getData().getString("Checkcode"),MainActivity.GetID(),"","","",msg.getData().getString("data"),""));
                    } catch (Exception e) {
                        Message msge=new Message();
                        msge.obj ="ERR";
                        msge.setData(a);
                        UdpHandler.sendMessage(msge);
                        e.printStackTrace();
                    }
                }
                else {
                    flag=false;
                    socket.close();
                }
            }
        };
        Looper.loop();
    }
}


