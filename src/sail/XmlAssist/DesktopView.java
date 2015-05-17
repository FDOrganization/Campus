package sail.XmlAssist;
/**
 * 桌面部件view类
 * @ClassName DesktopView
 * @author sail
 * @date 
 * */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.fydia.campus.R;
public class DesktopView extends View {
	
	private Context context;
	private Paint mPaint;
	private static int eachBoxH = 120; 
	private static int eachBoxW = 120;
	private static int sideW = 10;
	private static int sideH = 100;
	private int dayTotal = 7;
	private int classTotal = 12;
	private String[] weekdays;
	private int startX = 0;
	private int startY = 0;
	public static final int contentBg = Color.argb(255, 255, 255, 255);
	public static final int barBg = Color.argb(255, 225, 225, 225);
	public static final int barText = Color.argb(255, 150, 150, 150);
	public static final int barBgHrLine = Color.argb(240, 0, 0, 0);
	public static final int classBorder = Color.argb(180, 150, 150, 150);
	public static final int markerBorder = Color.argb(100, 150, 150, 150);
	public static final int curDayColor = Color.argb(250, 65, 105, 225);
	public static final int[] classBgColors = { Color.argb(200, 71, 154, 199),
			Color.argb(200, 230, 91, 62), Color.argb(200, 50, 178, 93),
			Color.argb(200, 255, 225, 0), Color.argb(200, 102, 204, 204),
			Color.argb(200, 51, 102, 153), Color.argb(200, 102, 153, 204)};
	// 构造
	public DesktopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		mPaint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		eachBoxW = (getWidth() - sideW) / 7;
		eachBoxH = (getHeight() - getHeight() / 6) / 12;
		weekdays = getResources().getStringArray(R.array.weekdays);
		DrawTitle(canvas);
	}
	// 画出title
	private void DrawTitle(Canvas canvas){
		mPaint.setColor(barBg);            // 预设背景
		mPaint.setStyle(Style.FILL);
		canvas.drawRect(sideW, 0, eachBoxW * dayTotal + startX ,sideW ,mPaint);
	}

}
