package cn.edu.cqupt.my;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    private EditText edittext;
    private Button btn;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        edittext=findViewById(R.id.add_contact);
        btn=findViewById(R.id.button_add);
        context=AddContactActivity.this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                Boolean s=false;
                List<Contact> g=LitePal.where("UserID==?",MainActivity.GetID()).find(Contact.class);
                for(Contact d:g)
                {
                    if(d.getTalktoId().equals(text))
                    {
                        s=true;
                        break;
                    }
                }
                if(text.length()==11&&!text.equals(MainActivity.GetID())&&s==false)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("TalkToid",text);
                    bundle.putString("data","");
                    bundle.putString("Checkcode","ADD");
                    MainActivity.sendMessage(bundle);
                    AddContactActivity.this.finish();
                }
                else if(text.equals(MainActivity.GetID())&&s==false)
                {
                    Toast.makeText(AddContactActivity.this,"怎么可以添加自己",Toast.LENGTH_SHORT).show();
                }
                else if(s==true)
                {
                    Toast.makeText(AddContactActivity.this,"已经是好友了咯",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddContactActivity.this,"账号格式错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void recaddinfo(final Bundle a)
    {
        if (a.getString("Chechcode").equals("Add"))
        {
            Toast.makeText(context,"不存在用户",Toast.LENGTH_SHORT).show();
        }
    }
}
