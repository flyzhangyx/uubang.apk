package cn.edu.cqupt.my;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class updateDialog extends Dialog {
        Activity context;

        private Button btn_upd;
    private Button btn_ignore;
        //public EditText text_name;
        public TextView updinfo;
        //public EditText text_pin;



        private View.OnClickListener mClickListener;

        public updateDialog(Activity context) {
            super(context);
            this.context = context;
        }

        public updateDialog(Activity context, int theme, View.OnClickListener clickListener) {
            super(context, theme);
            this.context = context;
            this.mClickListener = clickListener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 指定布局
            this.setContentView(R.layout.update_dialog);

//            text_name = findViewById(R.id.devicename);
//            text_pin =  findViewById(R.id.devicepin);
                updinfo=findViewById(R.id.updinfo);
            /*
             * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
             * 对象,这样这可以以同样的方式改变这个Activity的属性.
             */
            Window dialogWindow = this.getWindow();

            WindowManager m = context.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            Point q=new Point();
            d.getSize(q);
            p.width = (int) (q.x* 0.8); // 宽度设置为屏幕的0.8
            p.height=(int)(q.y*0.5);
            dialogWindow.setAttributes(p);

            // 根据id在布局中找到控件对象
            btn_upd=  findViewById(R.id.button_upd);
            btn_ignore=  findViewById(R.id.button_ignor);
            // 为按钮绑定点击事件监听器
            btn_ignore.setOnClickListener(mClickListener);
            btn_upd.setOnClickListener(mClickListener);
            this.setCancelable(true);
        }

}
