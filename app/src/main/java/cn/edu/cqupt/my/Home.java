package cn.edu.cqupt.my;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;


import java.util.ArrayList;
import java.util.List;

public class Home extends BaseFragment {
   // private static List<UserMessage> NewmsgList=new ArrayList<>();
    private static List<list_item> UnreadMsg=new ArrayList<>();
    private TextView titletext;
    private Button add;
    private static List<NewMsgUnread> unreadmsg=new ArrayList<>();
    private static RecyclerView NewmsgRecyclerview = null;
    private SwipeRefreshLayout Homeswipefresh;
    private static NewMsgAdapter adapter = null;
    private static TextView netset;
    @Override
    protected int attachLayoutRes() {
        return R.layout.home;
    }
    @Override
    protected void initViews() {
        titletext=findViewById(R.id.title_text);
        netset=findViewById(R.id.networkhome);
        Netset(false);
        netset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        titletext.setText("消息");
        titletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewmsgRecyclerview.scrollToPosition(0);
            }
        });
        RefreshUnreadMsg();
        ///***************************************
        add=findViewById(R.id.title_lift);
        //add.setText("QAQ");
        add.setBackgroundResource(R.drawable.setup);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.opencebianlan();
            }
        });

        //*******************************************
        Homeswipefresh=findViewById(R.id.Home_refresh);
        NewmsgRecyclerview=findViewById(R.id.Home_recycler);
        Homeswipefresh.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        Homeswipefresh.setColorSchemeResources(R.color.design_default_color_primary_dark,R.color.bottom_menu_color,R.color.colorAccent,R.color.radiobuttoncolor);
        Homeswipefresh.setProgressViewOffset(false,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,getResources().getDisplayMetrics()));
        Homeswipefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Homeswipefresh.setRefreshing(true);
                Bundle s=new Bundle();
                s.putString("Checkcode","RME");
                s.putString("data","");
                s.putString("TalkToid","");
                MainActivity.sendMessage(s);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        Homeswipefresh.setRefreshing(false);//设置为不可见
                        if(MainActivity.network)
                        Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(),"刷新失败",Toast.LENGTH_SHORT).show();
                    }
                },1200);
            }
        });
       NewmsgRecyclerview=findViewById(R.id.Home_recycler);
        LinearLayoutManager Layoutmanager=new LinearLayoutManager(getActivity());
        NewmsgRecyclerview.setLayoutManager(Layoutmanager);
        NewmsgRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adapter=new NewMsgAdapter(UnreadMsg);
        NewmsgRecyclerview.setAdapter(adapter);

    }
    ///********************************************************************
    private class NewMsgAdapter extends RecyclerView.Adapter<NewMsgAdapter.ViewHolder> {
        private java.util.List<list_item> List;

        class ViewHolder extends  RecyclerView.ViewHolder{
            // ImageView Image;
            TextView name;
            TextView content;
            public ViewHolder(View view){
                super(view);
                // Image=view.findViewById(R.id.image);
                content=view.findViewById(R.id.Home_msg_rightText);
                name=view.findViewById(R.id.Home_msg_center);
            }
        }
        public NewMsgAdapter(java.util.List<list_item> list)
        {
            List=list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.home_new_msg,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Bundle s=new Bundle();
                    s.putString("TalkToid",List.get(holder.getAdapterPosition()).getName());
                    s.putString("ID",MainActivity.GetID());
                   if(List.get(holder.getAdapterPosition()).getType().equals("MSG"))
                    {
                        TalkActivity.Actionstart(getActivity(),s);
                        AddNewUnreadMsg(List.get(holder.getAdapterPosition()).getName(),1,"MSG");
                        //we.save();
                        holder.content.setText("已读");
                    }
                    else
                    {
                        ReplyAddActivity.Actionstart(getActivity(),s);
                        List<NewMsgUnread> f=LitePal.where("UserId==?",List.get(holder.getAdapterPosition()).getName()).find(NewMsgUnread.class);
                        for (NewMsgUnread fd:f)
                        {
                            fd.delete();
                        }
                        UnreadMsg.remove(holder.getAdapterPosition());
                        adapter.notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
            });
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            list_item list_1=List.get(position);
            //holder.Image.setImageResource(list_1.getId());
            holder.name.setText(list_1.getName());
            if(list_1.getId()==0)
            {
                holder.content.setText("未读");
            }
            else
            {
                holder.content.setText("已读");
            }

        }
        @Override
        public int getItemCount(){
            return List.size();
        }
    }
    public static void RefreshUnreadMsg()
    {
        ClearNmsg();
        setUnreadMsg();
        for(NewMsgUnread d:unreadmsg)
        {
            if(d.getUserID().equals(MainActivity.GetID()))
            {
                list_item a=new list_item(d.getTalkToid(),d.getIsread(),d.getDataUnread());
                UnreadMsg.add(a);
                if(adapter!=null)
                {
                    adapter.notifyDataSetChanged();
                }
                //adapter.notifyItemInserted(0);
            }
        }

    }
    public static int AddNewUnreadMsg(String TalkToid,int type,String type2)
    {
        list_item a=new list_item(TalkToid,type,type2);
        List<NewMsgUnread> s =LitePal.where("UserID==?",MainActivity.GetID()).find(NewMsgUnread.class);
        int flag=0;
        for(NewMsgUnread f:s)
        {
            if(f.getTalkToid().equals(TalkToid))
            {
                f.setUnreadMsg(type2,MainActivity.GetID(),TalkToid,type);
                f.save();
                flag=1;
                break;
            }
        }
        if(flag==0)
        {
            NewMsgUnread d=new NewMsgUnread();
            d.setUnreadMsg(type2,MainActivity.GetID(),TalkToid,type);
            d.save();
        }
        //s.save();
        int i=0;
        for(list_item df:UnreadMsg)
        {
            if(df.getName().equals(TalkToid))
            {
                UnreadMsg.remove(i);
                //adapter.notifyItemRemoved(i);
                UnreadMsg.add(0,a);
                adapter.notifyItemChanged(0);
                return 1;
            }
            i++;
        }
            UnreadMsg.add(0,a);
        adapter.notifyItemInserted(0);
        return 0;
        //recyclerview.scrollToPosition(thing.size() - 1);
    }
    public static int  clearaddmsg(String s)
    {
        int i=0;
        for(list_item df:UnreadMsg)
        {
            if(df.getName().equals(s))
            {
                UnreadMsg.remove(i);
                adapter.notifyItemRemoved(i);
                return 1;
            }
            i++;
        }
        for(NewMsgUnread d:unreadmsg)
        {
            if(d.getUserID().equals(s))
            {
                d.delete();
                //adapter.notifyItemInserted(0);
            }
        }
        return 1;
    }
    public  static  void ClearNmsg()
    {
        UnreadMsg.clear();
    }
    public static void setUnreadMsg()
    {
        unreadmsg=LitePal.where("UserID==?",MainActivity.GetID()).find(NewMsgUnread.class);
    }
    public static void Netset(boolean r)
    {
        if(r)
            netset.setVisibility(View.VISIBLE);
        else
            netset.setVisibility(View.GONE);
    }
}
