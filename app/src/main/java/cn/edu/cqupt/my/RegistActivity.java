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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity {
    private EditText mEtid;
    private EditText mEtpassword1;
    private EditText mEtpassword2;
    private Bundle bundle;
    private String id;
    private String passwordstring;
    private String password;
    private Button mBtnRegist;
    private  Handler aHandler;
    private ClientRegistThread mClientThread;
    private final OkHttpClient client=new OkHttpClient();
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
        Intent intent= new Intent(RegistActivity.this,LoginActivity.class);
        RegistActivity.this.startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ActionBar actionbar=getSupportActionBar();
        if (actionbar!=null) {
            actionbar.hide();
        }
        mBtnRegist=findViewById(R.id.btn_regist);
        mEtid=findViewById(R.id.Registid);
        mEtpassword1=findViewById(R.id.textpassword1);
        mEtpassword2=findViewById(R.id.Confirmpwd);
        id=getIntent().getExtras().getString("ID");
        password=getIntent().getExtras().getString("PWD");
        mEtpassword1.setText(password);
        mEtpassword2.setText(password);
        mEtid.setText(id);
        bundle = new Bundle();
        aHandler = new Handler() {
            @Override
            public void handleMessage(Message Registmsg) {
                if(Registmsg.obj.toString().equals("RE"))
                {
                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(RegistActivity.this,LoginActivity.class);
                    RegistActivity.this.startActivity(intent);
                    finish();
                }
                else if(Registmsg.obj.toString().equals("ER")){
                    Toast.makeText(RegistActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
                else if(Registmsg.obj.toString().equals("Re"))
                {
                    Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
                        if (password.length() >= 6 && id.length() <= 18) {
                            if (password.equals(passwordstring)) {
                                Message sendRegismsg1 = new Message();
                                try {
                                    sendRegismsg1.what = 1;
                                    bundle.putString("ID",id );
                                    bundle.putString("Checkcode", "RE");
                                    bundle.putString("TalkToid", "NULL");
                                    bundle.putString("data", "NULL");
                                    bundle.putString("PWD", MD5Util.stringtoMD5(passwordstring));
                                    bundle.putString("REPWD", "NULL");
                                    sendRegismsg1.setData(bundle);
                                    mClientThread.RegistrevHandler.sendMessage(sendRegismsg1);
                                    Regist_Sql();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(RegistActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistActivity.this, "密码格式错误（6~18位）", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    Toast.makeText(RegistActivity.this, "账户名格式错误(11位手机号）", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        mClientThread = new ClientRegistThread(aHandler);
        new Thread(mClientThread).start();
    }
    public void Regist_Sql() throws Exception {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user",id)
                .addFormDataPart("password",passwordstring)
                //.addFormDataPart("method","login")
                //.addFormDataPart("key","*******")
                .build();
        Request request=new Request.Builder()
                .url("http://lostandfound.flyzhangyx.com/join.php")
                .post(requestBody)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    final  String content=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.e("TAG",content);
                            //postTextView.setText(content);
                        }
                    });
                }
            }
        });
    }

}

