package cn.edu.cqupt.my;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LitePal.getDatabase();
        Contact context=new Contact();
        context.SetContact("12345678901","12345678901");
        context.save();
        List<UserMessage> d=LitePal.findAll(UserMessage.class);
        if (d.size()==0)
        {
            Toast.makeText(TestActivity.this,"0",Toast.LENGTH_SHORT).show();
        }
        for (UserMessage s:d)
        {
            Toast.makeText(TestActivity.this,s.Getdate(),Toast.LENGTH_SHORT).show();
        }
    }
}
