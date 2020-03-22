package cn.edu.cqupt.my;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private java.util.List<list_item> List;
    private  Context context;
    public ListAdapter(Context context, java.util.List<list_item> list)
    {
       this.List=list;
       this.context=context;
    }
     class ViewHolder extends  RecyclerView.ViewHolder{
       // ImageView Image;
        TextView name;
        public ViewHolder(View view){
            super(view);
           // Image=view.findViewById(R.id.image);
            name=view.findViewById(R.id.list_name);
        }
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
            }
        });
    return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        list_item list_1=List.get(position);
       // holder.Image.setImageResource(list_1.getId());
        holder.name.setText(list_1.getId());
    }
    @Override
    public int getItemCount(){
        return List.size();
    }
}
