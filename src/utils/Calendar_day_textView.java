package utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 
 * @author 轩
 *今天的美化，将9改为⑨
 */
public class Calendar_day_textView extends TextView {
	
	public boolean isToday=false;
	private Paint paint=new Paint();//初始化paint
	
	public Calendar_day_textView(Context context) {
		super(context);
	}

	public Calendar_day_textView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControl();
	}

	public Calendar_day_textView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}


	private void initControl() {
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.parseColor("#ff0000"));//描边
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//圆圈绘制
		if (isToday) {
			canvas.translate(getWidth()/2, getHeight()/2);
			canvas.drawCircle(0, 0, getWidth()/2, paint);
		}
	}
}
