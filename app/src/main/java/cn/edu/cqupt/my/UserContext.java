package cn.edu.cqupt.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;


import java.util.ArrayList;
import java.util.List;

public class UserContext extends BaseFragment {
    private Button exit;
    private SwipeRefreshLayout Homeswipefresh;
    private Button add;
    private  static ContextAdapter adapter;
    private TextView titletext;
    private static List<Contact> context;
    private static List<list_item> Talktoid=new ArrayList<>();
   // private static ContextAdapter adapter;
    @Override
    protected int attachLayoutRes() {
        return R.layout.usercontext;
    }
    @Override
    protected void initViews() {
        titletext=findViewById(R.id.title_text);
        add=findViewById(R.id.title_right);
        titletext.setText("联系人");
        add.setBackgroundResource(R.drawable.addperson);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),AddContactActivity.class);
                    startActivity(intent);
            }
        });
       exit=findViewById(R.id.title_lift);
        //add.setText("QAQ");
        exit.setBackgroundResource(R.drawable.setup);

       exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.opencebianlan();
            }
        });

        RecyclerView recyclerview=findViewById(R.id.Recycler_context);
        LinearLayoutManager Layoutmanager=new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(Layoutmanager);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adapter=new ContextAdapter(Talktoid);
        recyclerview.setAdapter(adapter);
        initList();
        Homeswipefresh=findViewById(R.id.Contact_refresh);
        Homeswipefresh.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        Homeswipefresh.setColorSchemeResources(R.color.design_default_color_primary_dark,R.color.bottom_menu_color,R.color.colorAccent,R.color.radiobuttoncolor);
        Homeswipefresh.setProgressViewOffset(false,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,getResources().getDisplayMetrics()));
        Homeswipefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Homeswipefresh.setRefreshing(true);
                UsercontactRefresh();
                //Smartlife.UsercontactRefresh();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Homeswipefresh.setRefreshing(false);

                        if(MainActivity.network)
                            Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(),"刷新失败",Toast.LENGTH_SHORT).show();
                    }
                },3000);
                initList();
            }
        });
    }
    private void  initList()
    {
        setContextList();
        //Toast.makeText(getActivity(),MainActivity.GetID(),Toast.LENGTH_SHORT).show();
        for(Contact a:context)
        {
            if(a.GetID().equals(MainActivity.GetID()))
            {
                AddNewContext(a.getTalktoId());
                //adapter.notifyItemInserted(0);
            }
        }
        adapter.notifyDataSetChanged();
}


    class ContextAdapter extends RecyclerView.Adapter<ContextAdapter.ViewHolder> {
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
        public ContextAdapter(java.util.List<list_item> list)
        {
            List=list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            final ViewHolder holder=new UserContext.ContextAdapter.ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(holder.getAdapterPosition()>=0&&holder.getAdapterPosition()<List.size())
                    {
                        Bundle a=new Bundle();
                        a.putString("TalkToid",List.get(holder.getAdapterPosition()).getName());
                        List<NewMsgUnread> s =LitePal.where("UserID==?",MainActivity.GetID()).find(NewMsgUnread.class);
                        for(NewMsgUnread f:s)
                        {
                            if(f.getTalkToid().equals(a.getString("TalkToid")))
                            {
                                f.setUnreadMsg("MSG",MainActivity.GetID(),a.getString("TalkToid"),1);
                                f.save();
                                break;
                            }
                        }
                        Home.RefreshUnreadMsg();
                        a.putString("ID",MainActivity.GetID());
                        TalkActivity.Actionstart(getActivity(),a);
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


    public void setContextList()
    {

        context= LitePal.where("UserID==?",MainActivity.GetID()).find(Contact.class);
    }

    public static void AddNewContext(String TalkToid)
    {
        for(list_item df:Talktoid)
        {
            if(df.getName().equals(TalkToid))
            {
                return;
            }
        }
        list_item a=new list_item(TalkToid,R.drawable.person1,"");
        Talktoid.add(0,a);
        if (adapter!=null)
        {
            adapter.notifyItemInserted(0);
        }

    }
        //recyclerview.scrollToPosition(thing.size() - 1);

    public static void ClearContact()
    {
        Talktoid.clear();
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
