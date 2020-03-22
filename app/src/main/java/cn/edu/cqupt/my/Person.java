package cn.edu.cqupt.my;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Person extends  BaseFragment{
    private List<list_item> thing=new ArrayList<>();
    private Button exit;
    private Button add;
    private TextView titletext;
    private TextView textme;
    @Override
    protected int attachLayoutRes() {
        return R.layout.person;
    }

    @Override
    protected void initViews() {
        titletext=findViewById(R.id.title_text);
        //exit=findViewById(R.id.zhuxiao);
        titletext.setText("更多");
        add=findViewById(R.id.title_lift);
        //add.setText("QAQ");
        add.setBackgroundResource(R.drawable.app);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"敬请期待",Toast.LENGTH_SHORT).show();
            }
        });

        //textme=findViewById(R.id.textme);
       // textme.setText(MainActivity.GetID());
       // add.setText("设置 ");
        /*add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
            }
        });*/
       /* exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle s=new Bundle();
                s.putString("TalkToid"," ");
                s.putString("Checkcode","STO");
                s.putString("data"," ");
                s.putString("ID","00000000000" );
                s.putString("PWD", "000000");
                s.putString("REPWD", "NULL");
                ProgressDialog builder=new ProgressDialog(getActivity());
                builder.setTitle("注销登录");
                builder.setMessage("跳转中...");
                builder.setCancelable(false);
                builder.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 500);
                UserContext.ClearContact();
                MainActivity.sendMessage(s);
            }
        });*/
        initList();
    }

    private void  initList()
    {

            list_item a=new list_item("智慧生活",R.drawable.paper_plane,"");
            thing.add(a);
            list_item b=new list_item("失物帮",R.drawable.onlinegame,"");
            thing.add(b);
            list_item d=new list_item("队友帮",R.drawable.newss,"");
            thing.add(d);
            list_item e=new list_item("悬赏帮",R.drawable.onlinegame,"");
            thing.add(e);
            list_item c=new list_item("举报反馈",R.drawable.newss,"");
            thing.add(c);

            RecyclerView recyclerView=findViewById(R.id.recycler_view);
            LinearLayoutManager Layoutmanager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(Layoutmanager);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
            meAdapter adapter=new meAdapter(thing);
            recyclerView.setAdapter(adapter);
    }

///*****************************************************************************************
    class meAdapter extends RecyclerView.Adapter<meAdapter.ViewHolder> {
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
        public meAdapter(java.util.List<list_item> list)
        {
            List=list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
             final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    list_item a=List.get(holder.getAdapterPosition());
                    if(a.getName().equals("智慧生活"))
                    {
                        Bundle s=new Bundle();
                        s.putString("DestUrl","http://h.4399.com/wap/");
                        Smartlife.Actionstart(getActivity(),s);
                        Smartlife.UsercontactRefresh();
                    }
                    else if(a.getName().equals("失物帮"))
                    {
                        Bundle s=new Bundle();
                        s.putString("DestUrl","http://lostandfound.flyzhangyx.com");
                        s.putString("POST","POST");
                        WebviewActivity.Actionstart(getActivity(),s);
                    }
                    else if(a.getName().equals("队友帮"))
                    {
                        Bundle s=new Bundle();
                        s.putString("DestUrl","http://uubang.flyzhangyx.com/");
                        s.putString("POST","POST");
                        WebviewActivity.Actionstart(getActivity(),s);
                    }
                    else if(a.getName().equals("悬赏帮"))
                    {
                        Bundle s=new Bundle();
                        s.putString("DestUrl","http://reward.flyzhangyx.com/");
                        s.putString("POST","POST");
                        WebviewActivity.Actionstart(getActivity(),s);
                    }
                   else if(a.getName().equals("举报反馈"))
                    {
                        Uri uri = Uri.parse("http://uubang.flyzhangyx.com/report.php");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
//                        Bundle s=new Bundle();
//                        s.putString("DestUrl","http://uubang.flyzhangyx.com/report.php");
//                        //s.putString("POST","POST");
//                        WebviewActivity.Actionstart(getActivity(),s);
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
}
