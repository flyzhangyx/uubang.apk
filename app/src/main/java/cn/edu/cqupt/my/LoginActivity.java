package cn.edu.cqupt.my;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class LoginActivity extends ActivityCollector {
    private EditText mEtusername;
    private EditText mEtpassword;
    private String namestring;
    private String passwordstring;
    private Handler qHandler;
    private ClientThread qClientThread;
    private Bundle bundle;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkchanggereceiver;
    private Button mBtnLogin;
    private int f;
    private TextView mBtnRegist;
    private  TextView mBtnRPWD;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionbar=getSupportActionBar();
        if (actionbar!=null) {
            context=LoginActivity.this;
            actionbar.hide();
        }
        ActivityCollector.acontext=getApplicationContext();
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkchanggereceiver=new NetworkChangeReceiver();
        registerReceiver(networkchanggereceiver,intentFilter);
        mEtusername = findViewById(R.id.name2);
        mEtpassword = findViewById(R.id.textpassword);
        mBtnLogin = findViewById(R.id.button_login);
        mBtnRPWD=findViewById(R.id.fgpwd);
        mBtnRegist = findViewById(R.id.Textregist);
        bundle = new Bundle();
        qHandler = new Handler() {
            @Override
            public void handleMessage(Message Loginmsg) {
               if(Loginmsg.obj.toString().equals("SI"))
               {
                   Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                   if (f==1)
                   {
                       ProgressDialog builder=new ProgressDialog(LoginActivity.this);
                       builder.setTitle("登录");
                       builder.setMessage("登录中...");
                       builder.setCancelable(false);
                       builder.show();
                      // LoginConfig.saveBoolean(LoginActivity.this,"isLogin",true);
                       LoginConfig.saveUser(LoginActivity.this,namestring,MD5Util.stringtoMD5(passwordstring));
                       //LoginConfig.saveUser(LoginActivity.this,namestring,MD5Util.stringtoMD5(passwordstring));

                   }
                  //Toast.makeText(LoginActivity.this, LoginConfig.getPWD(LoginActivity.this), Toast.LENGTH_SHORT).show();
                   Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                   LoginActivity.this.startActivity(intent);
                   LoginActivity.this.finish();
               }
               else if(Loginmsg.obj.toString().equals("ER")){
                 //  Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                   //LoginConfig.saveBoolean(LoginActivity.this,"isLogin",false);
               }
               else if(Loginmsg.obj.toString().equals("Si"))
               {
                   Toast.makeText(LoginActivity.this, "登录失败,账号或密码错误", Toast.LENGTH_SHORT).show();
                   //LoginConfig.saveBoolean(LoginActivity.this,"isLogin",false);
               }
               else if(Loginmsg.obj.toString().equals("HB"))
               {
                   LoginActivity.this.finish();
               }
               else if(Loginmsg.obj.toString().equals("BT"))
               {
                  // Toast.makeText(LoginActivity.this, "心跳包", Toast.LENGTH_SHORT).show();
               }
               else if(Loginmsg.obj.toString().equals("TC"))
               {
                   Toast.makeText(LoginActivity.this, "服务器炸了", Toast.LENGTH_SHORT).show();
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           finish();
                       }
                   },2000) ;
               }
            }
        };
        qClientThread = new ClientThread(qHandler);
        new Thread(qClientThread).start();
        //********************判断是否自动登录**************************
        Reconnect();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namestring = mEtusername.getText().toString();
                passwordstring = mEtpassword.getText().toString();
                if (namestring.length() <= 11&&namestring.length()>=9) {
                    if (passwordstring.length() >= 6 && passwordstring.length() <= 18) {
                        Message SendLoginmsg = new Message();
                        try {
                            SendLoginmsg.what = 1;
                            bundle.putString("ID", namestring);
                            bundle.putString("Checkcode", "SI");
                            bundle.putString("TalkToid", "NULL");
                            bundle.putString("data","NULL");
                            bundle.putString("PWD", MD5Util.stringtoMD5(passwordstring));
                            bundle.putString("REPWD", "NULL");
                            SendLoginmsg.setData(bundle);
                            f=1;
                            qClientThread.revHandler.sendMessage(SendLoginmsg);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "密码格式错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "账户名格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
            mBtnRegist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(LoginActivity.this,RegistActivity.class);
                    Bundle a=new Bundle();
                    Message sendLoginmsg = new Message();
                    try {
                        sendLoginmsg.what = 1;
                        bundle.putString("ID","00000000");
                        bundle.putString("TalkToid", "NULL");
                        bundle.putString("data","NULL");
                        bundle.putString("PWD", "000000");
                        bundle.putString("Checkcode", "HB");
                        bundle.putString("REPWD", "NULL");
                        sendLoginmsg.setData(bundle);
                        sendLoginmsg.setData(bundle);
                        qClientThread.revHandler.sendMessage(sendLoginmsg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    a.putString("ID",mEtusername.getText().toString());
                    a.putString("PWD",mEtpassword.getText().toString());
                    intent.putExtras(a);
                    startActivity(intent);
                }
            });
            mBtnRPWD.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent =new Intent(LoginActivity.this,REPWD.class);
                    Bundle a=new Bundle();
                    Message sendLoginmsg = new Message();
                    try {
                        sendLoginmsg.what = 1;
                        bundle.putString("ID","00000000");
                        bundle.putString("TalkToid", "NULL");
                        bundle.putString("data","NULL");
                        bundle.putString("PWD", "000000");
                        bundle.putString("Checkcode", "HB");
                        bundle.putString("REPWD","NULL");
                        sendLoginmsg.setData(bundle);
                        sendLoginmsg.setData(bundle);
                        qClientThread.revHandler.sendMessage(sendLoginmsg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    a.putString("ID",mEtusername.getText().toString());
                    a.putString("PWD",mEtpassword.getText().toString());
                    intent.putExtras(a);
                    startActivity(intent);
                }
            });

        }
        @Override
        protected void onDestroy()
        {
            super.onDestroy();
            unregisterReceiver(networkchanggereceiver);
        }
        //判断网络是否发生改变
        class  NetworkChangeReceiver extends BroadcastReceiver{
        @Override
            public void onReceive(Context context,Intent intent){
                ConnectivityManager connectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectionManager.getActiveNetworkInfo();
                if(networkInfo!=null &&networkInfo.isAvailable()){
                    //Toast.makeText(context,"network is available",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(LoginActivity.this,"网络错误,即将退出",Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2000);

                }
        }
        }
        public static boolean autoSignclear()
    {
        LoginConfig.saveUser(context,"","");
       // LoginConfig.saveBoolean(context,"isLogin",false);
        return true;
    }
    public  void Reconnect()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!LoginConfig.getID(LoginActivity.this).equals("")) {
                    Message sendLoginmsg = new Message();
                    try {
                        sendLoginmsg.what = 1;
                        namestring=LoginConfig.getID(LoginActivity.this);
                        passwordstring=LoginConfig.getPWD(LoginActivity.this);
                        // mEtusername.setText(namestring);
                        bundle.putString("ID", namestring);
                        bundle.putString("Checkcode", "SI");
                        bundle.putString("TalkToid", "0000");
                        bundle.putString("data","NULL");
                        bundle.putString("PWD", passwordstring);
                        bundle.putString("REPWD", "NULL");
                        sendLoginmsg.setData(bundle);
                        f=0;
                        qClientThread.revHandler.sendMessage(sendLoginmsg);
                        //Toast.makeText(LoginActivity.this,"1", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ProgressDialog builder=new ProgressDialog(LoginActivity.this);
                    builder.setTitle("登录");
                    builder.setMessage("自动登录中...");
                    builder.setCancelable(true);
                    builder.show();
                }
            }
        }, 100);
    }
   /* public static Context GetLoginActivvitycontext()
    {
        return context;
    }*/
}


