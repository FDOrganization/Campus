package sail.XmlAssist;
/**
 * 课表界面类
 * @ClassName myScheuleView
 * @author yxw
 * @mender sail
 * @date 2014.12.14 晚上8:30
 * */
import java.util.List;

import sail.data.LocalInforM;
import sail.ui.ScheduleActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.fydia.campus.R;

public class myScheduleView extends View implements OnTouchListener{
	private Paint mPaint; 
	private int curDay;
	private int startX = 0;
	private int startY = 0;
	private static final int sidewidte = 50;
	private static final int eachBoxH = 120;
	private static int eachBoxW = 120;
	private int focusX = -1;
	private int focusY = -1;
	private static int classTotal = 12;
	private static int dayTotal = 7;
	private String[] weekdays;
	private boolean isMove = false; 
	
	@SuppressWarnings("unused")
	private Context context;

	private OnItemClassClickListener onItemClassClickListener;

	private List<ClassInfo> classList;
	// 设置颜色
	public static final int contentBg = Color.argb(255, 255, 255, 255);
	public static final int barBg = Color.argb(255, 255, 255, 255);
	public static final int barText = Color.argb(255, 150, 150, 150);
	public static final int barBgHrLine = Color.argb(240, 0, 0, 0);
	public static final int classBorder = Color.argb(180, 150, 150, 150);
	public static final int markerBorder = Color.argb(100, 150, 150, 150);
	public static final int curDayColor = Color.argb(250, 65, 105, 225);
	public static final int[] classBgColors = { Color.argb(200, 71, 154, 199),
			Color.argb(200, 230, 91, 62), Color.argb(200, 50, 178, 93),
			Color.argb(200, 255, 225, 0), Color.argb(200, 102, 204, 204),
			Color.argb(200, 51, 102, 153), Color.argb(200, 102, 153, 204)

	};
	// 构造函数
	public myScheduleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.curDay = LocalInforM.curWeekDay;
		weekdays = context.getResources().getStringArray(R.array.weekdays);
		mPaint = new Paint();
		setOnTouchListener(this);
	}
	// 手动在canvas上做图
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		eachBoxW = (getWidth() - sidewidte) / 7;
		printMarker(canvas);
		printContent(canvas);
		printTopBar(canvas);
		printLeftBar(canvas);
	}

	/**
	 * 
	 * @param canvas
	 */
	private void printMarker(Canvas canvas) {
		mPaint.setColor(markerBorder);
		for (int i = 0; i < dayTotal - 1; i++) {
			for (int j = 0; j < classTotal - 1; j++) {
				mPaint.setStyle(Style.STROKE);
				mPaint.setAlpha(1);
				canvas.drawRect(startX + sidewidte + eachBoxW * (i + 1)
						- eachBoxW / 20, startY + sidewidte + eachBoxH
						* (j + 1) - 1, startX + sidewidte + eachBoxW * (i + 1)
						+ eachBoxW / 20, startY + sidewidte + eachBoxH
						* (j + 1), mPaint);
				canvas.drawRect(
						startX + sidewidte + eachBoxW * (i + 1) - 1,
						startY + sidewidte + eachBoxH * (j + 1) - eachBoxW / 20,
						startX + sidewidte + eachBoxW * (i + 1), startY
								+ sidewidte + eachBoxH * (j + 1) + eachBoxW
								/ 20, mPaint);
			}
		}
	}

	/**
	 * 
	 * @param canvas
	 */
	private void printContent(Canvas canvas) {
		if (classList != null && classList.size() > 0) {
			mPaint.setTextSize(19);
			ClassInfo classInfo;
			for (int i = 0; i < classList.size(); i++) {
				classInfo = classList.get(i);
				int fromX = startX + sidewidte + eachBoxW
						* (classInfo.getWeekday() - 1);
				int fromY = startY + sidewidte + eachBoxH
						* (classInfo.getFromClassNum() - 1);
				int toX = startX + sidewidte + eachBoxW
						* classInfo.getWeekday();
				int toY = startY
						+ sidewidte
						+ eachBoxH
						* (classInfo.getFromClassNum()
								+ classInfo.getClassNumLen() - 1);
				classInfo.setPoint(fromX, fromY, toX, toY);
				mPaint.setStyle(Style.FILL);
				mPaint.setColor(classBgColors[i % classBgColors.length]);
				// 设置背景透明度
//				mPaint.setAlpha(10);
				canvas.drawRect(fromX, fromY, toX - 2, toY - 2, mPaint);
				mPaint.setColor(Color.WHITE);
//				mPaint.setAlpha(50);
				String className = classInfo.getClassname() + "@"
						+ classInfo.getClassRoom();
				
				Rect textRect1 = new Rect();
				mPaint.getTextBounds(className, 0, className.length(),
						textRect1);
				int th = textRect1.bottom - textRect1.top;
				int tw = textRect1.right - textRect1.left;
				int row = (int) ((tw + 30) / eachBoxW + 1) +1;
				int length = className.length() / row ; 
				for (int j = 0; j < row - 1; j++) {
					canvas.drawText(className, length * j, length * (j + 1),
							fromX + 10, fromY + 10 + th * (j + 1), mPaint);
				}
				canvas.drawText(className, length * (row - 1),
						className.length(), fromX + 10, fromY + 10 + th * row,
						mPaint);
				mPaint.setColor(classBorder);
				mPaint.setAlpha(10);
				mPaint.setStyle(Style.STROKE);
				canvas.drawRect(fromX, fromY, toX - 2, toY - 2, mPaint);
			}
		}
	}

	/**
	 * 
	 * @param canvas
	 */
	private void printLeftBar(Canvas canvas) {
		mPaint.setColor(barBg);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(30);
		canvas.drawRect(0, startY + sidewidte, sidewidte, sidewidte + startY
				+ eachBoxH * classTotal, mPaint);
		mPaint.setColor(barBgHrLine);
		canvas.drawRect(0, startY + sidewidte + eachBoxH - 1, sidewidte, startY
				+ eachBoxH + sidewidte, mPaint);
		Rect textRect1 = new Rect();
		mPaint.getTextBounds("1", 0, 1, textRect1);
		int th = textRect1.bottom - textRect1.top;
		int tw1 = textRect1.right - textRect1.left;
		mPaint.getTextBounds("10", 0, 2, textRect1);
		int tw2 = textRect1.right - textRect1.left;
		canvas.drawText("1", sidewidte / 2 - tw1, startY + sidewidte + eachBoxH
				/ 2 + th / 2, mPaint);
		for (int i = 2; i < classTotal + 1; i++) {
			canvas.drawRect(0, startY + sidewidte + eachBoxH * i - 1,
					sidewidte, startY + eachBoxH * i + sidewidte, mPaint);
			int tw = tw1 * 2 + (tw2 - tw1) * (i / 10);
			canvas.drawText(i + "", sidewidte / 2 - tw / 2, startY + sidewidte
					+ eachBoxH * (i - 1) + eachBoxH / 2 + th / 2, mPaint);
		}
		canvas.drawRect(0, 0, sidewidte, sidewidte, mPaint);
	}

	/**
	 * 
	 * @param canvas
	 */
	private void printTopBar(Canvas canvas) {
		mPaint.setColor(barBg);
		mPaint.setStyle(Style.FILL);
		canvas.drawRect(startX + sidewidte, 0, sidewidte + startX + eachBoxW
				* dayTotal, sidewidte, mPaint);
		mPaint.setColor(barBgHrLine);
		mPaint.setTextSize(25);
		canvas.drawRect(startX + sidewidte + eachBoxW - 1, 0, startX + eachBoxW
				+ sidewidte, sidewidte, mPaint);
		canvas.drawLine(startX, sidewidte, startX 
				+ sidewidte+ eachBoxW*dayTotal, sidewidte, mPaint);
		canvas.drawLine(startX, 0, startX 
				+ sidewidte+ eachBoxW*dayTotal, 0, mPaint);
		Rect textBounds = new Rect();
		mPaint.getTextBounds(weekdays[0], 0, weekdays[0].length(), textBounds);
		int textHeight = textBounds.bottom - textBounds.top;
		int textWidth = textBounds.right - textBounds.left;
		if(curDay == 1)
			mPaint.setColor(curDayColor);
		else
			mPaint.setColor(barBgHrLine);
		canvas.drawText(weekdays[0], startX + sidewidte + eachBoxW / 2
				- textWidth / 2, sidewidte / 2 + textHeight / 2, mPaint);
		for (int i = 2; i < dayTotal + 1; i++) {
			
			canvas.drawRect(startX + sidewidte + eachBoxW * i - 1, 0, startX
					+ eachBoxW * i + sidewidte, sidewidte, mPaint);
			if(i == curDay)
				mPaint.setColor(curDayColor);
			else
				mPaint.setColor(barBgHrLine);
			canvas.drawText(weekdays[i - 1], startX + sidewidte + eachBoxW
					* (i - 1) + eachBoxW / 2 - textWidth / 2, sidewidte / 2
					+ textHeight / 2, mPaint);
		
		}
	}
	/**
	 * 复写onTouch方法
	 * */
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			focusX = (int) event.getX();
			focusY = (int) event.getY();
			isMove = false;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int dx = (int) (event.getX() - focusX);
			int dy = (int) (event.getY() - focusY);
//			if(dx > 100 && dy < 100 ){
//				LocalInforM.tabFlag = 2;
//				Message msg = new Message();
//				msg.what = 1;
//				MainActivity.handler.sendMessage(msg);
//			}
//			if(dx < -100 && dy < 100){
//				LocalInforM.tabFlag = 1;
//				Message msg = new Message();
//				msg.what = 1;
//				MainActivity.handler.sendMessage(msg);
//			}
			
			if(dy < -80 && dx < 50 && ScheduleActivity.flag % 2 == 0){
				Message msg = new Message();
				msg.what = 1;
				ScheduleActivity.handler.sendMessage(msg);
			}
			
			
			if (!isMove && Math.abs(dx) < 5 && Math.abs(dy) < 5) {
				isMove = false;
				return false;
			}
			isMove = true;
			if (startX + dx < 0
					&& startX + dx + eachBoxW * dayTotal + sidewidte >= getWidth()) {
				startX += dx;
			}
			if (startY + dy < 0
					&& startY + dy + eachBoxH * classTotal + sidewidte >= getHeight()) {
				startY += dy;
			}
			focusX = (int) event.getX();
			focusY = (int) event.getY();
			invalidate();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (!isMove) {
				int focusX = (int) event.getX();
				int focusY = (int) event.getY();
				for (int i = 0; i < classList.size(); i++) {
					ClassInfo classInfo = classList.get(i);
					if (focusX > classInfo.getFromX()
							&& focusX < classInfo.getToX()
							&& focusY > classInfo.getFromY()
							&& focusY < classInfo.getToY()) {
						if (onItemClassClickListener != null) {
							onItemClassClickListener.onClick(classInfo);
						}
						break;
					}
				}
			}
		}
		

		return true;
		
	}

	public interface OnItemClassClickListener {
		public void onClick(ClassInfo classInfo);
	}

	public OnItemClassClickListener getOnItemClassClickListener() {
		return onItemClassClickListener;
	}

	public void setOnItemClassClickListener(
			OnItemClassClickListener onItemClassClickListener) {
		this.onItemClassClickListener = onItemClassClickListener;
	}

	public List<ClassInfo> getClassList() {
		return classList;
	}

	public void setClassList(List<ClassInfo> classList) {
		this.classList = classList;
		invalidate();
	}

}
