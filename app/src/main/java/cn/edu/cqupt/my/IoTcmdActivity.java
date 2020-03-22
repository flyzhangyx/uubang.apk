package cn.edu.cqupt.my;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class IoTcmdActivity extends Activity implements View.OnClickListener {

    private TextView mTvSelectedDate, mTvSelectedTime;
    private ImageView imageView;
    private CustomDatePicker mTimerPicker;
    private EditText temp;
    private Switch sw,on_off;
    //private  static IoTAdapter adapter;
    private String swflag;
    private boolean swon_off;
    private boolean swq;
    private final Timer timer = new Timer();
    private TimerTask task;
    private static List<list_item> Talktoid=new ArrayList<>();
    private List<iotRec> iotrec;
    private static final int MSG_DATA_CHANGE = 0x11;
    private LineView mLineView;
    //private Handler mHandler;
    private int mX = 0;
    public static String Id;
    private List<IoTdevice> ioTdevices;
    RecyclerView recyclerview=null;
    public static void Actionstart(Context ctx, Bundle a)
    {
        Intent intent=new Intent(ctx,IoTcmdActivity.class);
        intent.putExtras(a);
        ctx.startActivity(intent);
    }
    @Override public void onBackPressed()
    {
        timer.cancel();
        this.finish();
        //Talktoid.clear();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ioTdevices.clear();
            ioTdevices=LitePal.where("Talk_ToId=?",getIntent().getExtras().getString("IoTID")).find(IoTdevice.class);
           initDatePicker();
            //tempq=ioTdevices.get(0).gettenp().toCharArray();
           // mLineView.setLinePoint(mX,(tempq[1]-48)*10+(tempq[2]));
           // mX+=30;
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iotcmd);
        //setRec();
        Id=getIntent().getExtras().getString("IoTID");
        //recyclerview=findViewById(R.id.iotcmd);
        ioTdevices=LitePal.where("Talk_ToId=?",getIntent().getExtras().getString("IoTID")).find(IoTdevice.class);
        //tempq=ioTdevices.get(0).gettenp().toCharArray();
        temp=findViewById(R.id.input_temp);
        mLineView=findViewById(R.id.line);
       // mLineView.setLinePoint(mX,(tempq[1]-48)*10+(tempq[2]));
        /*imageView=findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.peito);
            }
        });*/
        on_off=findViewById(R.id.on_off_iot);
        sw=findViewById(R.id.switchiotcmd);
        sw.setChecked(false);
        on_off.setChecked(false);
        swon_off=false;
        swq=false;
        if(swflag==null)
        {
            swflag="0";
        }
        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swon_off=b;
            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
           swq=b;
            }
        });
        /*****************************************************************/

       /* LinearLayoutManager Layoutmanager=new LinearLayoutManager(IoTcmdActivity.this);
        recyclerview.setLayoutManager(Layoutmanager);
        recyclerview.addItemDecoration(new DividerItemDecoration(IoTcmdActivity.this,DividerItemDecoration.VERTICAL));
        adapter=new IoTAdapter(Talktoid);
        recyclerview.setAdapter(adapter);
        initList();*/
        /***********************************************************************/
        findViewById(R.id.button_cmd1).setOnClickListener(this);
        findViewById(R.id.ll_date).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date);
        initDatePicker();

        /**timer**/

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);

            }
        };
        timer.schedule(task, 0, 5000);
        /*****/

        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        initTimerPicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                // 日期格式为yyyy-MM-dd
               // mDatePicker.show(mTvSelectedDate.getText().toString());
                break;

            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            case R.id.button_cmd1:
                {
                    if (temp.getText().toString().length()<=0||temp.getText().toString().length()>2)
                    {
                        Toast.makeText(IoTcmdActivity.this,"温度设置错误", Toast.LENGTH_SHORT).show();
                        break;
                    }
                Bundle bundle = new Bundle();
                bundle.putString("TalkToid", getIntent().getExtras().getString("IoTID"));
                if(!swon_off)
                    swflag="0";
                else if(swq&&swon_off)
                    swflag="2";
                else if(!swq&&swon_off)
                    swflag="1";
                else
                    swflag="0";
                    if ( DateFormatUtils.long2Str(System.currentTimeMillis(), true).compareTo(mTvSelectedTime.getText().toString())>0)
                    {
                        Toast.makeText(IoTcmdActivity.this,"时间设置错误", Toast.LENGTH_SHORT).show();
                        break;
                    }
                bundle.putString("data", mTvSelectedTime.getText().toString()+temp.getText().toString()+swflag );
                bundle.putString("Checkcode", "TAI");
                MainActivity.sendMessage(bundle);
                /*addRectolocal( " 执行时间："+mTvSelectedTime.getText().toString()+" | 温度："+temp.getText().toString());
                AddNewContext(" 执行时间："+mTvSelectedTime.getText().toString()+" | 温度："+temp.getText().toString(),0);
                recyclerview.scrollToPosition(0);*/
                Toast.makeText(IoTcmdActivity.this,getIntent().getExtras().getString("IoTID")+"将在"+ mTvSelectedTime.getText().toString()+"执行" , Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mDatePicker.onDestroy();
    }
    public void initDatePicker() {
        String water=null;
        if(ioTdevices.size()<=0)
            return;
        if(ioTdevices.get(0).getwater().equals("00"))
            water="未加热";
        else if(ioTdevices.get(0).getwater().equals("01"))
            water="加热中";
        else if(ioTdevices.get(0).getwater().equals("02"))
            water="保 温";
        else
            water="错 误";
        if(ioTdevices.size()!=0&&ioTdevices.get(0).gettenp().length()>1&&!ioTdevices.get(0).gettenp().equals("NUL"))
            mTvSelectedDate.setText("温度： "+ioTdevices.get(0).gettenp().substring(0,3)+"."+ioTdevices.get(0).gettenp().substring(4,5)+"  |  状态："+water);
        else if(ioTdevices.get(0).gettenp().equals("NUL"))
            mTvSelectedDate.setText("温度： "+"N/A"+"  |  状态："+"N/A");
     /* long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mTvSelectedDate.setText(getIntent().getExtras().getString("IoTID"));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);*/
    }
    /*private void  initList()
    {
        setRec();
        //Toast.makeText(getActivity(),MainActivity.GetID(),Toast.LENGTH_SHORT).show();
        for(iotRec a:iotrec)
        {
            if(a.GetID().equals(MainActivity.GetID()))
            {
                //if(a.getstate()==1)
                    AddNewContext(a.gettenp(),1);
               // else
                    //AddNewContext(a.gettenp(),1);
                adapter.notifyItemInserted(0);
            }
        }
        adapter.notifyDataSetChanged();
    }*/
    private void initTimerPicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        mTvSelectedTime.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, "2030-10-17 18:00");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    /****/
   /* class IoTAdapter extends RecyclerView.Adapter<IoTAdapter.ViewHolder> {
        private java.util.List<list_item> List;

        class ViewHolder extends  RecyclerView.ViewHolder{
            //ImageView Image;
            TextView name;
            public ViewHolder(View view){
                super(view);
                //Image=view.findViewById(R.id.image);
                name=view.findViewById(R.id.list_name);
            }
        }
        public IoTAdapter(java.util.List<list_item> list)
        {
            List=list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            final ViewHolder holder=new IoTcmdActivity.IoTAdapter.ViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            list_item list_1=List.get(position);
            //holder.Image.setImageResource(list_1.getId());
            holder.name.setText(list_1.getName());
        }
        @Override
        public int getItemCount(){
            return List.size();
        }
    }*/
    /*public static void AddNewContext(String talkToid,int id)
    {
        /*for(list_item df:Talktoid)
        {
            if(!df.getName().equals(talkToid))
            {
                return;
            }
        }*/
      /*  list_item a=new list_item(talkToid,id,"");

        Talktoid.add(0,a);
        if (adapter!=null)
        {
            adapter.notifyItemInserted(0);
        }
       /* if(Talktoid.size()>10)
        {
            Talktoid.remove(Talktoid.size()-1);
            adapter.notifyItemInserted(Talktoid.size()-1);
        }*/


  /*  }

    private void addRectolocal(String waht)
    {
        iotRec a=new iotRec();
        a.SetContact(MainActivity.GetID(),getIntent().getExtras().getString("IoTID"),waht,"NUL",iotrec.size());
        a.save();
    }
    private void setRec()
    {
        iotrec=LitePal.where("UserID==?",MainActivity.GetID()).limit(10).order("state asc").limit(10).find(iotRec.class);
    }

    public static void recclear()
    {
        LitePal.deleteAll(iotRec.class);

    }*/

}

