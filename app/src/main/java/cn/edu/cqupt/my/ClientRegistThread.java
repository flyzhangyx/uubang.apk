package cn.edu.cqupt.my;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientRegistThread implements Runnable {
        private Socket mSocket=null;
        private BufferedReader mBufferedReader = null;
        private OutputStream mOutputStream = null;
        private Handler mHandler;
        public Handler RegistrevHandler;
        public String tag;
        public boolean flag=true;
        public ClientRegistThread(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void run() {
            try {
                mSocket=msocket.getsocket(1);
                Log.d("a","connect success");
                mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mOutputStream = mSocket.getOutputStream();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String str;
                        String content;
                        flag=true;
                        while (flag) {
                           if (!mSocket.isClosed()) {
                                Message msg = new Message();
                                try {
                                    content=mBufferedReader.readLine();
                                    if(content!=null)
                                    {
                                        str= content.substring(0,2);
                                    }
                                    else
                                    {
                                        str="ER";
                                    }
                                    if (str.equals("RE")) {
                                        msg.obj="RE";
                                        flag=false;
                                    }
                                    else if(str.equals("Re")) {
                                        msg.obj="Re";
                                    }
                                    else if(str.equals("HB"))
                                    {
                                        flag=false;
                                        msg.obj="HB";
                                    }
                                    else
                                    {
                                        msg.obj = "ER";
                                    }

                                    mHandler.sendMessage(msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Message msg1 = new Message();
                                msg1.obj = "ER";
                                mHandler.sendMessage(msg1);
                            }
                       }
                    }
                }.start();
                Looper.prepare();
                RegistrevHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if(!mSocket.isClosed()) {
                            if (msg.what == 1) {
                                tag=msg.getData().getString("Checkcode");
                                try {
                                    mOutputStream.write(Struct.getBuf(msg.getData().getString("Checkcode"), msg.getData().getString("ID"), msg.getData().getString("PWD"), msg.getData().getString("TalkToid"),msg.getData().getString("REPWD"), msg.getData().getString("data"), null));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            try {
                                mSocket=msocket.getsocket(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Looper.loop();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("a","");
            }
        }
    }
