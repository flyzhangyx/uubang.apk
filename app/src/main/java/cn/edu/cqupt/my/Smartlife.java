package cn.edu.cqupt.my;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Smartlife extends AppCompatActivity {
        private Button button;
        private CreateUserDialog createUserDialog;
        private boolean s=false;

    private final Timer timer = new Timer();
    private TimerTask task;
    private  static IoTAdapter adapter;
    private static List<IoTdevice> context;
    private static List<list_item> Talktoid=new ArrayList<>();
    private TextView titletext;
    private Button exit;
    private Button o;
    @Override public void onBackPressed()
    {
        timer.cancel();
        this.finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ClearContact();
            UsercontactRefresh();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initList();
                }
            },2000);

            super.handleMessage(msg);
        }
    };
        public static void Actionstart(Context ctx, Bundle a)
    {
        Intent intent=new Intent(ctx,Smartlife.class);
        intent.putExtras(a);
        ctx.startActivity(intent);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.button_add:
                    String name = createUserDialog.text_name.getText().toString();
                    String pin = createUserDialog.text_pin.getText().toString();
                    //System.out.println(name+"——"+mobile);
                    //List<IoTdevice> g=LitePal.where("UserID==?",MainActivity.GetID()).find(Contact.class);
                    for(IoTdevice d:context)
                    {
                        Toast.makeText(Smartlife.this,name,Toast.LENGTH_SHORT).show();
                        if(name.equals(d.getTalktoId()))
                        {
                            s=true;
                            //Toast.makeText(Smartlife.this,d.getTalktoId()+name,Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if(name.length()*pin.length()!=0&&!s) {
                        //Toast.makeText(Smartlife.this,name+mobile, Toast.LENGTH_SHORT).show();
                        Bundle bundle=new Bundle();
                        bundle.putString("TalkToid",name);
                        bundle.putString("data",MainActivity.GetID());
                        bundle.putString("Checkcode","ADDI");
                        MainActivity.sendMessage(bundle);
                        createUserDialog.cancel();
                        final ProgressDialog builder = new ProgressDialog(Smartlife.this);
                        builder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        builder.setTitle("添加设备");
                        builder.setMax(100);
                        builder.setMessage("请在5s内按下物联设备的闪烁按钮");
                        builder.setCancelable(false);
                        builder.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int i = 0;
                                while (i < 100) {
                                    try {
                                        Thread.sleep(50);
                                        builder.incrementProgressBy(1);
                                        i++;
                                    } catch (Exception e) {
                                        Toast.makeText(Smartlife.this, "BUG FLY", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                builder.dismiss();
                            }
                        }).start();
                    }
                    else if(!s)
                    {
                        Toast.makeText(Smartlife.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Smartlife.this,"已存在于本地，若无请刷新",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_iotdevice);
       button=findViewById(R.id.iotdevice_add);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showEditDialog(view );
           }
       });
        titletext=findViewById(R.id.title_text);
        exit=findViewById(R.id.title_lift);
        o=findViewById(R.id.title_right);
        titletext.setText("互联设备列表");
        exit.setBackgroundResource(R.drawable.exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                finish();
            }
        });
        /**timer**/

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 5000, 6000);
        /*****/
        o.setBackgroundResource(R.drawable.updated);

       o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearContact();
                UsercontactRefresh();
                Toast.makeText(Smartlife.this,"刷新中...",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                    }
                },2000);

            }
        });
       /*****************************************************************/
        RecyclerView recyclerview=findViewById(R.id.recycler_iotview);
        LinearLayoutManager Layoutmanager=new LinearLayoutManager(Smartlife.this);
        recyclerview.setLayoutManager(Layoutmanager);
        recyclerview.addItemDecoration(new DividerItemDecoration(Smartlife.this,DividerItemDecoration.VERTICAL));
        adapter=new IoTAdapter(Talktoid);
        recyclerview.setAdapter(adapter);
        initList();
    }
    /****************************************************************************/

    public void showEditDialog(View view) {
        createUserDialog = new CreateUserDialog(this,R.layout.add_iotdevice,onClickListener);
        createUserDialog.show();
    }

    /***************************************************************************/
    class IoTAdapter extends RecyclerView.Adapter<IoTAdapter.ViewHolder> {
        private java.util.List<list_item> List;

        class ViewHolder extends  RecyclerView.ViewHolder{
            ImageView Image;
            TextView name;
            public ViewHolder(View view){
                super(view);
                 Image=view.findViewById(R.id.image);
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
            final ViewHolder holder=new Smartlife.IoTAdapter.ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(holder.getAdapterPosition()>=0&&holder.getAdapterPosition()<List.size())
                    {
                        //if(holder.Image.geti==R.drawable.out)
                        //dialog = new DatePickerDialog(Smartlife.this, dateListener, Calendar.YEAR, Calendar.MONTH,Calendar.DAY_OF_MONTH);
                        Bundle a=new Bundle();
                        a.putString("IoTID",holder.name.getText().toString());
                        IoTcmdActivity.Actionstart(Smartlife.this,a);
                    }
                }
            });
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            list_item list_1=List.get(position);
           holder.Image.setImageResource(list_1.getId());
            holder.name.setText(list_1.getName());
        }
        @Override
        public int getItemCount(){
            return List.size();
        }
    }

    private void  initList()
    {
        setContextList();
        //Toast.makeText(getActivity(),MainActivity.GetID(),Toast.LENGTH_SHORT).show();
        for(IoTdevice a:context)
        {
            if(a.Get_ID().equals(MainActivity.GetID()))
            {
                if(a.getstate()==1)
                    AddNewContext(a.getTalktoId(),R.drawable.online);
                else
                    AddNewContext(a.getTalktoId(),R.drawable.outline);
                adapter.notifyItemInserted(Talktoid.size()-1);
            }
        }
        adapter.notifyDataSetChanged();
    }
   /* public static void upiot(String talktoid,int id)
    {
        int i=-1;
        for(list_item df:Talktoid)
        {
            if(df.getName().equals(talktoid)&&(id==1)&&df.getId()!=R.drawable.online)
            {
                list_item a=new list_item(talktoid,R.drawable.online,"");
                Talktoid.remove(i);
                //adapter.notifyItemRemoved(i);
                Talktoid.add(i,a);
                adapter.notifyItemChanged(i);
                return;

            }
            else if(df.getName().equals(talktoid)&&(id!=1)&&df.getId()==R.drawable.online)
            {
                list_item a=new list_item(talktoid,R.drawable.outline,"");
                Talktoid.remove(i);
                //adapter.notifyItemRemoved(i);
                Talktoid.add(i,a);
                adapter.notifyItemChanged(i);
                return;
            }

            i++;
        }
        if(id==1)
        AddNewContext(talktoid,R.drawable.online);
        else
            AddNewContext(talktoid,R.drawable.online);


    }*/

    public void setContextList()
    {
        context= LitePal.where("User_ID==?",MainActivity.GetID()).find(IoTdevice.class);

    }


    public static void AddNewContext(String talkToid,int id)
    {
            for(list_item df:Talktoid)
            {
                if(df.getName().equals(talkToid))
                {
                    return;
                }
            }
            list_item a=new list_item(talkToid,id,"");
            Talktoid.add(a);
            if (adapter!=null)
            {
                adapter.notifyItemInserted(Talktoid.size()-1);
            }

        }

    //recyclerview.scrollToPosition(thing.size() - 1);

    public static void ClearContact()
    {
        Talktoid.clear();
        LitePal.deleteAll(IoTdevice.class);
    }
    public static void UsercontactRefresh()
    {
        LitePal.deleteAll(Contact.class,"UserID==?",MainActivity.GetID());
        Bundle sd=new Bundle();
        sd.putString("TalkToid","");
        sd.putString("Checkcode","RCO");
        sd.putString("data","");
        MainActivity.sendMessage(sd);
    }

}
