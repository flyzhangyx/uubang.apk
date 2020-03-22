package cn.edu.cqupt.my;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReplyAddActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private TextView text;
    public static void Actionstart(Context context,Bundle a)
    {
        Intent intent=new Intent(context,ReplyAddActivity.class);
        intent.putExtras(a);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_add);
        btn1=findViewById(R.id.buttonagree);
        btn2=findViewById(R.id.buttonrefuse);
        text=findViewById(R.id.textadd);
        text.setText(getIntent().getExtras().getString("TalkToid"));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("TalkToid",getIntent().getExtras().getString("TalkToid"));
                bundle.putString("data","");
                bundle.putString("Checkcode","ADS");

                MainActivity.sendMessage(bundle);
                        Toast.makeText(ReplyAddActivity.this,"同意添加对方好友！即将返回主界面",Toast.LENGTH_SHORT).show();

                ReplyAddActivity.this.finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.clearaddmsg(getIntent().getExtras().getString("TalkToid"));

                        Toast.makeText(ReplyAddActivity.this,"拒绝添加对方好友！即将返回主界面",Toast.LENGTH_SHORT).show();

                ReplyAddActivity.this.finish();
            }
        });
    }
}
