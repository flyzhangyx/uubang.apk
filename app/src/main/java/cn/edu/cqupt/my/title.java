package cn.edu.cqupt.my;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class title extends LinearLayout {
    public title(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.title1,this);
        Button titleleft=findViewById(R.id.title_lift);
        Button titleright=findViewById(R.id.title_right);
        TextView titletext=findViewById(R.id.title_text);
        titleleft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ( (Activity)getContext()).finish();
            }
        });
    }
}
