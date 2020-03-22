package cn.edu.cqupt.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class video extends AppCompatActivity  implements View.OnTouchListener {
        private VideoView videoView;
        private String uri="http://flyzhangyx.com:8090";
    public static void Actionstart(Context context, Bundle a)
    {
        Intent intent=new Intent(context,video.class);
        intent.putExtras(a);
        context.startActivity(intent);
    }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_video);//导入一个布局
            videoView = (VideoView) findViewById(R.id.videoView);
            //findViewById()获取在布局中定义了的元素，返回的是一个View对象，需要向下转型
            MediaController mediaController = new MediaController(this);
            videoView.setVideoURI(Uri.parse(uri));
            videoView.setMediaController(mediaController);
            videoView.start();
            videoView.setOnTouchListener(this);
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            if(videoView!=null){
                videoView.suspend();
            }
        }
        public boolean onTouch(View view, MotionEvent motionEvent) {//实现onTouch接口
            switch (view.getId()){
                case R.id.videoView:
                    Toast.makeText(video.this,"溜了",Toast.LENGTH_LONG).show();
                    this.finish();
                    break;
            }
            return false;
        }
    }

