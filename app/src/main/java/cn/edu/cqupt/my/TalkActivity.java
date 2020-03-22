package cn.edu.cqupt.my;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

public class TalkActivity extends AppCompatActivity {

    private static  List<UserMessage> MsgDB = new ArrayList<>();
    private static List<Msg> msgList = new ArrayList<>();
    private EditText input;
    private Context context=null;
    private static RecyclerView msgRecyclerview = null;
    private static MsgAdapter adapter = null;
    private Bundle bundle;
    private Button mBtnsend;
    private int flag;
    private  static Context b;
    private Button exit;
    private Button add;
    private static Intent s;
    private TextView titletext;
    public static void Actionstart(Context context,Bundle a)
    {
        Intent intent=new Intent(context,TalkActivity.class);
        intent.putExtras(a);
        context.startActivity(intent);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.deleteActivity(this);
        MainActivity.Isalive(0);
        msgList.clear();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        titletext=findViewById(R.id.title_text);
        Intent intent = getIntent();
        if(intent.getAction()!=null) {
            String action = intent.getAction();
            if (Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent.getData();
                if (uri != null) {
                    String id = uri.getQueryParameter("id");
                    //Toast.makeText(this, id, Toast.LENGTH_LONG).show();
                    s=getIntent();
                    s.putExtra("TalkToid",id);
                    s.putExtra("ID",MainActivity.GetID());
                }
            }
        }
        else {
            s = intent;
        }
        add=findViewById(R.id.title_lift);
        titletext.setText(s.getExtras().getString("TalkToid"));
        add.setBackgroundResource(R.drawable.exit1);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        flag=0;
        setMessageList();
        if(flag==0) {
            int i=0;
            for(UserMessage a:MsgDB)
            {
               if(a.getTalktoId().equals(s.getExtras().getString("TalkToid"))&&a.getUserId().equals(MainActivity.GetID())) {
                    msgList.add(new Msg(a.getMessageContent(), a.getMessageType()));
                }
                i++;
               if(i>30)
                   break;
            }
            flag=1;
        }

        ActionBar actionbar=getSupportActionBar();
        if (actionbar!=null) {
            actionbar.hide();
        }
        context=getApplicationContext();
        ActivityCollector.addActivity(this);
        MainActivity.Isalive(1);
        b=TalkActivity.this;
        //******************************************************
        msgRecyclerview = findViewById(R.id.recycler_talkview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        msgRecyclerview.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        input=findViewById(R.id.input_text);
        msgRecyclerview.setAdapter(adapter);
        mBtnsend=findViewById(R.id.send_button);
        bundle = new Bundle();
        //*******************************************************
        msgRecyclerview.scrollToPosition(msgList.size() - 1);
        mBtnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg msg = new Msg(input.getText().toString(), Msg.Type_sendc);
                if (input.getText().toString().length() > 0&&input.getText().toString().length()<300){
                    bundle.putString("TalkToid",s.getExtras().getString("TalkToid"));
                    bundle.putString("data",input.getText().toString().replaceAll("（\r\n|\r|\n|\n\r","<HC>"));
                    bundle.putString("Checkcode","TA");
                    MainActivity.sendMessage(bundle);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    msgRecyclerview.scrollToPosition(msgList.size() - 1);
                    UserMessage mes=new UserMessage();
                    mes.setUser(getSharedPreferences("userconfig",MODE_PRIVATE).getString("ID",""),s.getExtras().getString("TalkToid"),input.getText().toString(),Msg.Type_sendc,Calendar.getInstance().get(Calendar.YEAR)+Calendar.getInstance().get(Calendar.YEAR)+Calendar.getInstance().get(Calendar.MONTH)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+"");
                    mes.save();
                    /*****进入监控*****/
                    if(input.getText().toString().equals("flyzhangyx_438"))
                    {
                        Toast.makeText(TalkActivity.this,"偷偷地进入",Toast.LENGTH_SHORT).show();
                        Bundle a=new Bundle();
                        video.Actionstart(TalkActivity.this,a);
                    }
                    input.setText("");
                    Home.AddNewUnreadMsg(s.getExtras().getString("TalkToid"),1,"MSG");
                }
                else if(input.getText().toString().length()>300)
                {
                    Toast.makeText(TalkActivity.this,"字数过多",Toast.LENGTH_SHORT).show();
                }
                else {

                }

            }
        });
    }
    public static void recvMessage(final Bundle a)
    {
        final Msg msg = new Msg(a.getString("data"), Msg.Type_recv);
        if(a.getString("Checkcode").equals("Taa"))
        {
            Toast.makeText(b,"发送失败！",Toast.LENGTH_SHORT).show();
        }
        else if(a.getString("Checkcode").equals("TAS")||a.getString("Checkcode").equals("TAN")){
            Toast.makeText(b,"发送成功！",Toast.LENGTH_SHORT).show();
        }
        else if(a.getString("Checkcode").equals("TAT"))
        {
            if(a.getString("TalkToid").equals(s.getExtras().getString("TalkToid")))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size() - 1);
                        msgRecyclerview.scrollToPosition(msgList.size() - 1);
                    }
                }, 100);
            }

        }
    }
  /*  public  Context getcontext()
    {
        return context;
    }*/
    public void setMessageList()
    {
        MsgDB= LitePal.where("UserID==?",getIntent().getExtras().getString("ID")).find(UserMessage.class);
    }
}

