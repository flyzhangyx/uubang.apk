package cn.edu.cqupt.my;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

public class          TalkThread implements Runnable {
    private Socket mSocket=null;
    private BufferedReader mBufferedReader = null;
    private OutputStream mOutputStream = null;
    private Handler mHandler;
    public Handler revHandler;
    public String tag;
    public boolean flag=true;
    private Bundle a;
    private Timer timer = new Timer();
    private TimerTask task;
    //public boolean isExit=false;
    public TalkThread(Handler handler) {
        mHandler = handler;
    }
    @Override
    public void run() {
       Reconnect();///封装成一个方法
    }
    private  void Reconnect()
    {
        try {
            a=new Bundle();
            mSocket=msocket.getsocket(1);
            Log.d("a","connect success");
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOutputStream = mSocket.getOutputStream();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    flag=true;
                    String str = null;
                    String content =null;
                    String data = null;
                    String Talktoid = null;
                    /*try {
                        mSocket.setSoTimeout(6000);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }*/
                    while (flag) {
                        if (mSocket!=null) {
                            /************************************/
                            Message msg = new Message();
                            try {
                                if(mBufferedReader!=null) {
                                    content = mBufferedReader.readLine();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    Reset(0);
                                    if(mSocket==null&&MainActivity.network)
                                    {
                                        Message msge=new Message();
                                        msge.obj ="ERR";
                                        msge.setData(a);
                                        mHandler.sendMessage(msge);
                                        continue;
                                    }
                                    else if(MainActivity.network&& mOutputStream!=null){
                                        mOutputStream.write(Struct.getBuf("SI", MainActivity.GetID(), LoginConfig.getPWD(MainActivity.ctx), "", "", "", null));
                                        mOutputStream.flush();
                                    }
                                    else
                                    {
                                        new Thread().sleep(2000);
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                continue;
                            }
                            /*********************/
                            if(content!=null)
                            {
                                if(content.length()>=620) {
                                    str = content.substring(0, 3);
                                    data = content.substring(108, 620).replaceAll("<HC>", "\n");
                                    Talktoid = content.substring(63, 74);
                                }
                                else if(content.length()>=10||content.length()<=12)
                                {
                                    continue;/****检测online**/
                                }
                                else {
                                    str = "ERR";
                                    data = "BUG";
                                    Talktoid = " ";
                                }

                            }
                            else
                            {
                                str="ERR";
                                data="BUG";
                                Talktoid=" ";
                            }
                            if(str==null)
                            {
                                continue;
                            }
                            if (str.equals("TAS")) {//在线
                                msg.obj ="TAS";
                                a.putString("data"," ");
                                a.putString("TalkToid",Talktoid);
                            }
                            else if (str.equals("TAT")) {
                                msg.obj ="TAT";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if (str.equals("TAN")) {
                                msg.obj ="TAN";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if (str.equals("JPG")) {
                                msg.obj ="JPG";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("TAI")){
                                msg.obj ="TAI";
                                a.putString("temp",data.substring(0,5));
                                a.putString("water",data.substring(6,8));
                                a.putString("Talktoid",Talktoid);
                            }
                            else if(str.equals("ONL")){
                                msg.obj ="ONL";
                                a.putString("data",data);
                                a.putString("temp",data.substring(0,5));
                                a.putString("water",data.substring(6,8));
                                a.putString("Talktoid",Talktoid);
                            }
                            else if(str.equals("OUT")){
                                msg.obj ="OUT";
                                a.putString("data",data);
                                a.putString("temp",data.substring(0,5));
                                a.putString("water",data.substring(6,8));
                                a.putString("Talktoid",Talktoid);
                            }
                            else if (str.equals("Taa")) {
                                msg.obj ="Taa";
                                a.putString("data"," ");
                                a.putString("TalkToid"," ");
                            }
                            else if (str.equals("RCO")) {
                                msg.obj ="RCO";
                                a.putString("data"," ");
                                a.putString("TalkToid",Talktoid);
                            }
                            else if (str.equals("ADS")) {
                                msg.obj ="ADS";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("HB"))
                            {
                                flag=false;
                                msg.obj = "HB";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("ADD")||str.equals("ADN"))
                            {
                                msg.obj = "ADD";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("NME"))
                            {
                                msg.obj = "NME";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("NCA"))
                            {
                                msg.obj = "NCA";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("ADT"))
                            {
                                msg.obj = "ADT";
                                a.putString("data",data);
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("STO"))
                            {
                                msg.obj = "STO";
                                flag=false;
                            }
                            else if(str.equals("Add"))
                            {
                                msg.obj = "Add";
                            }
                            else if(str.equals("TNI"))
                            {
                                msg.obj = "TNI";
                                a.putString("data"," ");
                                a.putString("TalkToid",Talktoid);
                            }
                            else if(str.equals("TAi"))
                            {
                                msg.obj = "TAi";
                            }
                            else if(str.equals("BT"))
                            {
                                msg.obj = "BTT";
                            }
                            else if(str.equals("PNG"))
                            {
                                msg.obj = "PNG";
                            }
                            /*else if(str.equals("XTB"))
                            {
                                continue;
                            }*/
                            else if(str.equals("UPD")){
                                String version=data.substring(0, 3);
                                if(!version.equals(MainActivity.version))
                                {
                                    msg.obj="UPD";
                                }
                                else {
                                    msg.obj="BTT";
                                }
                            }
                            else {
                                msg.obj = "ERR";
                                a.putString("data",data);
                                a.putString("TalkToid"," ");
                            }
                            msg.setData(a);
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(MainActivity.ctx,"BUG2",Toast.LENGTH_SHORT).show();
                            Message msg1 = new Message();
                            a.putString("data","BUG");
                            a.putString("TalkToid"," ");
                            msg1.obj = "ER";
                            msg1.setData(a);
                            mHandler.sendMessage(msg1);
                            try {
                               Reset(0);
                                if(mSocket==null&&MainActivity.network)
                                {
                                    Message msge=new Message();
                                    msge.obj ="ERR";
                                    msge.setData(a);
                                    mHandler.sendMessage(msge);
                                    continue;
                                }
                                else if(MainActivity.network&& mOutputStream!=null){
                                    mOutputStream.write(Struct.getBuf("SI", MainActivity.GetID(), LoginConfig.getPWD(MainActivity.ctx), "", "", "", null));
                                    mOutputStream.flush();
                                }
                                else
                                {
                                    new Thread().sleep(2000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }.start();
            Looper.prepare();

            /****************************/
            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(mSocket!=null) {
                        if (msg.what == 1) {
                            tag=msg.getData().getString("Checkcode");
                            try {
                                mOutputStream.write(Struct.getBuf(msg.getData().getString("Checkcode"), msg.getData().getString("ID"), msg.getData().getString("PWD"), msg.getData().getString("TalkToid"),msg.getData().getString("REPWD"), msg.getData().getString("data"),null));
                                mOutputStream.flush();
                            } catch (Exception e) {
                                //ReSet();
                                Message msge=new Message();
                                msge.obj ="ERR";
                                msge.setData(a);
                                mHandler.sendMessage(msge);
                                Reset(0);
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        try {
                            mSocket=msocket.getsocket(0);
                            new Thread().sleep(2000);
                            if (mSocket!=null) {
                                if (mOutputStream!=null) {
                                    mOutputStream.write(Struct.getBuf("SI", MainActivity.GetID(), LoginConfig.getPWD(MainActivity.ctx), "", "", "", null));
                                    mOutputStream.flush();
                                }
                            }
                            else
                            {
                                Message msge=new Message();
                                msge.obj ="ERR";
                                msge.setData(a);
                                mHandler.sendMessage(msge);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //if(msg.arg2==10)
                    // isExit=true;
                }
            };
            Looper.loop();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    private  void Reset(int time)
    {
        try {
        if(mBufferedReader!=null)
        {
                mBufferedReader.close();
        }
        if(mOutputStream!=null)
        {
            mOutputStream.close();
        }
        if(mSocket!=null)
        {
            mSocket.close();
        }
        mOutputStream=null;
        mBufferedReader=null;
        mSocket=null;
        mSocket=msocket.getsocket(0);
        if(time>=5) {
            new Thread().sleep(10000);
            time=0;
        }
            else {
            new Thread().sleep(3000);
            time++;
        }
        if(mSocket!=null) {
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOutputStream = mSocket.getOutputStream();
        }
        else
        {
            Reset(time);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }
}
