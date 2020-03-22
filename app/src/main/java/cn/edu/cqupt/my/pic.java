package cn.edu.cqupt.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class pic {
    /**
     * pic类
     * 用来获取和发送图片到服务器
     */
    private static Bitmap getBitmap;
    public static boolean  pic_send()
    {
        return true;
    }
    public static Bitmap pic_get(String imgUrl)
    {
        OkHttpClient client = new OkHttpClient();//实例化
        Request request = new Request.Builder().url(imgUrl).build(); //传入图片网址，，URL为自己定义好的网址。
        client.newCall(request).enqueue(new Callback() {//实例化一个call的对象
            public void onFailure(Call call, IOException e) {
            }
            public void onResponse(Call call, Response response){
                if (response.isSuccessful()) {//成功
                    getBitmap=BitmapFactory.decodeStream(response.body().byteStream());
                } else {//失败
                    getBitmap=null;
                }
            }
        });
        return getBitmap;
    }
}
