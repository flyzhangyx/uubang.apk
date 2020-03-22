package cn.edu.cqupt.my;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import org.litepal.LitePal;

public class LineView extends View{
	private int XPoint = 60;
	private int YPoint = 1250;
	private int XScale = 50;  //刻度长度
	private int YScale = 12;
	private int XLength = 950;
	private int YLength = 1200;
    private char[] temp;
	private int MaxDataSize = XLength / XScale;

	private List<Integer> data = new ArrayList<Integer>();



	private String[] YLabel = new String[YLength / YScale];

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x1234){
				LineView.this.invalidate();
			}
		};
	};
	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		for(int i=0; i<YLabel.length; i++){
			YLabel[i] = (i + 1) + "℃";
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(data.size() >= MaxDataSize){
						data.remove(0);
					}
					if(LitePal.where("Talk_ToId=?",IoTcmdActivity.Id).find(IoTdevice.class).size()>0) {
                        temp = LitePal.where("Talk_ToId=?", IoTcmdActivity.Id).find(IoTdevice.class).get(0).gettenp().toCharArray();
                       if((temp[1] - 48) * 10 + temp[2]-48<=100)
                        data.add((temp[1] - 48) * 10 + temp[2]-48);
                    }
					handler.sendEmptyMessage(0x1234);
				}
			}
		}).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true); //去锯齿
		paint.setColor(Color.BLUE);

		//画Y轴
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);

		//Y轴箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint-YLength + 6, paint);  //箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint-YLength + 6 ,paint);

		//添加刻度和文字
		for(int i=0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, paint);  //刻度

			canvas.drawText(YLabel[i], XPoint - 50, YPoint - i * YScale, paint);//文字
		}

		//画X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
		System.out.println("Data.size = " + data.size());
		if(data.size() > 1){
			for(int i=1; i<data.size(); i++){
				canvas.drawLine(XPoint + (i-1) * XScale, YPoint - data.get(i-1) * YScale,
						XPoint + i * XScale, YPoint - data.get(i) * YScale, paint);
			}
		}
	}
}