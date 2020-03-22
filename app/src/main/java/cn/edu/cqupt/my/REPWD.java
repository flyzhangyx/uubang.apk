package cn.edu.cqupt.my;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class REPWD extends AppCompatActivity {
    private EditText mEtid;
    private EditText mEtpassword1;
    private EditText mEtpassword2;
    private Bundle bundle;
    private String id;
    private String passwordstring;
    private String password;
    private Button mBtnRegist;
    private  Handler aHandler;
    private ClientRPWDThread mClientThread;
    @Override
    public void onBackPressed(){
        Message sendLoginmsg = new Message();
        try {
            sendLoginmsg.what = 1;
            bundle.putString("ID","00000000000" );
            bundle.putString("TalkToid", "NULL");
            bundle.putString("data", "NULL");
            bundle.putString("PWD", "000000");
            bundle.putString("Checkcode", "HB");
            bundle.putString("REPWD", "NULL");
            sendLoginmsg.setData(bundle);
            mClientThread.RegistrevHandler.sendMessage(sendLoginmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent= new Intent(REPWD.this,LoginActivity.class);
        REPWD.this.startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repwd);
        ActionBar actionbar=getSupportActionBar();
        if (actionbar!=null) {
            actionbar.hide();
        }
        mBtnRegist=findViewById(R.id.RPWDbtn_regist);
        mEtid=findViewById(R.id.REPWDid);
        mEtpassword1=findViewById(R.id.RPWDpassword);
        mEtpassword2=findViewById(R.id.REPWDpwd);
        id=getIntent().getExtras().getString("ID");
        password=getIntent().getExtras().getString("PWD");
        mEtpassword1.setText(password);
        //mEtpassword2.setText(password);
        mEtid.setText(id);
        bundle = new Bundle();
        aHandler = new Handler() {
            @Override
            public void handleMessage(Message Registmsg) {
                if(Registmsg.obj.toString().equals("RP"))
                {
                    Toast.makeText(REPWD.this, "改密成功", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(REPWD.this,LoginActivity.class);
                    REPWD.this.startActivity(intent);
                    finish();
                }
                else if(Registmsg.obj.toString().equals("ER")){
                    Toast.makeText(REPWD.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
                else if(Registmsg.obj.toString().equals("Rp"))
                {
                    Toast.makeText(REPWD.this, "改密失败", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    finish();
                }
            }
        };
        mBtnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = mEtid.getText().toString();
                password = mEtpassword1.getText().toString();
                passwordstring = mEtpassword2.getText().toString();
                if (id.length() == 11) {
                    if (password.length() >= 6 && passwordstring.length() <= 18) {
                        if (passwordstring.length()>= 6 && passwordstring.length() <= 18) {
                            Message sendRegismsg1 = new Message();
                            try {
                                sendRegismsg1.what = 1;
                                bundle.putString("ID",id );
                                bundle.putString("Checkcode", "RP");
                                bundle.putString("TalkToid", "NULL");
                                bundle.putString("data", "NULL");
                                bundle.putString("REPWD", MD5Util.stringtoMD5(passwordstring));
                                bundle.putString("PWD",MD5Util.stringtoMD5(password));
                                sendRegismsg1.setData(bundle);
                                mClientThread.RegistrevHandler.sendMessage(sendRegismsg1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toast.makeText(REPWD.this, "新密码格式错误（6~18位）", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(REPWD.this, "旧密码格式错误（6~18位）", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(REPWD.this, "账户名格式错误(11位手机号）", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mClientThread = new ClientRPWDThread(aHandler);
        new Thread(mClientThread).start();
    }
}