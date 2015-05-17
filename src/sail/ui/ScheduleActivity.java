package sail.ui;
/**
 * 课表界面操作Activity
 * @ClassName schedule
 * @author sail
 * @date 2014.12.11 上午10:23
 * --------------------------------
 * @date 2014.12.21 下午4:57 改动selectButton选中方式
 * @mender sail
 * */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import sail.XmlAssist.HorListBaseAdapter;
import sail.XmlAssist.HorizontalListView;
import sail.data.LocalInforM;
import sail.data.OperateData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fydia.campus.R;

public class ScheduleActivity extends FragmentActivity {
	private View ScheduleView;
	private int myCurWeekDay;
	private TextView curText,selectText;
	private View horListView;
	private Button selectButton;
	public static Handler handler;
	private static TableFragment  fragement;
	public static int flag = 1;    				//　按钮点击计数
	public static OperateData operatedata;
	private HorizontalListView horizontalListView;
	private myOnItemOnclick itemOnclick ;
	private HorListBaseAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 得到当前的时间
		getTime();
		// 得到初始化的周数
		getWeek();
		Init();
		set_Ssize();
		setContentView(ScheduleView);
		onListener();
		do_horizontalList();
		/*
		 * 周数选择通知handler
		 * @author sail
		 * */
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 1){
					selectButton.performClick();
				}
			}
		};
	}


	/* 
	 * 初始化
	 * @author sail
	 * */
	private void Init(){
		// 添加Fragment
		addFragment();
		// 得到LayoutInflater对象 
		LayoutInflater inflater = getLayoutInflater();
		horListView = inflater.inflate(R.layout.activity_item, null);
		ScheduleView = inflater.inflate(R.layout.activity_schedule, null);
		selectButton = (Button)ScheduleView.findViewById(R.id.scheduleButton);
		curText = (TextView)ScheduleView.findViewById(R.id.curWeekText);
		selectText = (TextView)ScheduleView.findViewById(R.id.selectWeek_sail);
		horizontalListView = (HorizontalListView)horListView.findViewById(R.id.SelecteWeek);
		
		setDay();
	}
	/*
	 * 设置周数及学期
	 * @author sail
	 * */
	private void setDay(){
		curText.setText("当前 \n 第"+LocalInforM.curWeek+"周");
	}
	
	
	/* 
	 * 绘制View
	 * @author sail
	 * */
	private void set_Ssize(){
		// 构造RelativeLayout.layoutparams
		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
				,(LocalInforM.Ssize.heightPixels /15));
		// title宽度增加
		LocalInforM.TitleSize += layoutParams1.height; 
		// 取得控件
		RelativeLayout relativeLayout1 = (RelativeLayout)ScheduleView.findViewById(R.id.oTableRela);
		RelativeLayout relativeLayout2 = (RelativeLayout)ScheduleView.findViewById(R.id.sTableRela);
		// 设置
		relativeLayout1.setLayoutParams(layoutParams1);
		horListView.setVisibility(View.GONE);
		relativeLayout2.addView(horListView);

	}


	/*
	 * horizontalList事件处理
	 * @author sail
	 * */
	private void do_horizontalList(){

		int i;
		String a[] = new String[24];
		for(i = 0 ; i < 24 ; i++){
			a[i] = Integer.toString(i+1);
		}
		adapter = new HorListBaseAdapter(this,a);
		itemOnclick = new myOnItemOnclick(adapter);
		horizontalListView.setAdapter(adapter);
		horizontalListView.setOnItemClickListener(itemOnclick);
		horizontalListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == LocalInforM.curWeek-1)
					dialog(position);
				return false;
			}
		});
	}
	/**
	 * item事件监听类
	 * @ClaaName myOnItemOnclick
	 * @author sail
	 * */
	class myOnItemOnclick implements OnItemClickListener{
		private HorListBaseAdapter adapter;
		public myOnItemOnclick(HorListBaseAdapter adapter) {
			// TODO Auto-generated constructor stub
			this.adapter = adapter;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			adapter.selectIndex = position+1;
			adapter.notifyDataSetChanged();
			LocalInforM.curWeek = position+1;
			refreFragment(true);
		}


	}


	/* 
	 * 一般button监听事件
	 * @author sail
	 * */
	private void onListener(){
		selectButton.setOnClickListener(new myOnclickListener());
	}

	class myOnclickListener implements OnClickListener{
		int position;
		public myOnclickListener (int position){
			// TODO Auto-generated method stub
			this.position = position;
		}
		public myOnclickListener(){}

		//button event
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.scheduleButton:
				// 下拉弹出选择view，再次点击则隐藏
				flag++;
				if(flag % 2 == 0){
					getWeek();
					horListView.setVisibility(View.VISIBLE);
					horizontalListView.scrollTo(LocalInforM.curWeek*120);
				}
				else
				{	
					adapter.selectIndex = LocalInforM.curWeek; // 重置选中
					getWeek();
					position = adapter.selectIndex;
					if(position != LocalInforM.curWeek){ //判断当前选中的周数，与正确周数是否一致
						LocalInforM.curWeek = position;
						selectText.setText("选中第"+position+"周\n"+"点此返回");
						selectText.setTextSize(10);
						selectText.setTextColor(Color.RED);
						selectText.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								getWeek();             // 重置正确周数
								selectText.setText(null);
								adapter.selectIndex = LocalInforM.curWeek; // 重置选中
								adapter.notifyDataSetChanged();         // 数据跟新
								refreFragment(true);
							}
						});
					}
					// 一致时需要任何操作
					adapter.selectIndex = LocalInforM.curWeek;
					adapter.notifyDataSetChanged();
					refreFragment(false);
					horListView.setVisibility(View.GONE);
					
				}

				break;

			default:
				break;
			}
		}

	}
	/*
	 * dialog 事件方法
	 * @MethodName dialog
	 * @author sail
	 * */
	protected void dialog(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
		builder.setMessage("设为本周");
		builder.setNegativeButton("取消", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确定", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				LocalInforM.curWeek = position+1;
				// 更新周数
				curText.setText("当前 \n 第"+LocalInforM.curWeek+"周");
				// 储存周数
				dataSave();
				// 更新list
				adapter.notifyDataSetChanged();
				refreFragment(true);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	

	/*
	 * 添加一个新的Fragment
	 * @author sail
	 * */
	private void addFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager() ;
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragement = new TableFragment();
		fragmentTransaction.add(R.id.TableFrameLayout, fragement);
		fragmentTransaction.commit();
	}
	/*
	 * 刷新Frament
	 * @author sail
	 * */
	private void refreFragment(boolean flag){
		FragmentManager fragmentManager = getSupportFragmentManager() ;
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		TableFragment fragement2 = new TableFragment();
		// 设置动画
		if(flag)
			fragmentTransaction.setCustomAnimations(R.anim.fly_enter, R.anim.fly_out);
		fragmentTransaction.replace(R.id.TableFrameLayout, fragement2);
		fragmentTransaction.commit();
	}
	
	
	/*
	 *  获得时间
	 * @author sail
	 * */
	private void getTime(){
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		LocalInforM.year = c.get(Calendar.YEAR);
		LocalInforM.month = c.get(Calendar.MONTH);
		LocalInforM.day = c.get(Calendar.DAY_OF_MONTH);
		if(1 == c.get(Calendar.DAY_OF_WEEK))
			LocalInforM.curWeekDay = 7;
		else
			LocalInforM.curWeekDay = c.get(Calendar.DAY_OF_WEEK)-1;
	}
	
	/*
	 * 保存相关数据到本地
	 * @author sail
	 * */
	private void dataSave(){
		// 取得活动的preferences对象
		SharedPreferences preferences = getPreferences(0);
		// 取得编辑对象
		SharedPreferences.Editor editor = preferences.edit();
		// 储存正确周
		editor.putInt("curWeekDay", LocalInforM.curWeekDay);
		editor.putInt("curWeek",LocalInforM.curWeek);
		editor.putInt("year", LocalInforM.year);
		editor.putInt("month", LocalInforM.month);
		editor.putInt("day", LocalInforM.day);
		// 保存
		editor.commit();
	}
	
	/*
	 * 读出一个初始周并根据当前日期判断现在是第几周
	 * @author sail
	 * */
	private void getWeek(){
		SharedPreferences sPreferences = getPreferences(Activity.MODE_PRIVATE);
		myCurWeekDay = sPreferences.getInt("curWeekDay", 1);
		int myWeek = sPreferences.getInt("curWeek", 1);
		int myYear = sPreferences.getInt("year", 2015);
		int myMonth = sPreferences.getInt("month",2);
		int myDay = sPreferences.getInt("day",2);
		//转换为时间戳
		String time1 = myYear+"年"+myMonth+"月"+myDay+"日";
		String time2 = LocalInforM.year+"年"+LocalInforM.month+"月"+LocalInforM.day+"日";
		// 相差7天以上
			LocalInforM.curWeek = ((int)((getNumDay(time1, time2)+myCurWeekDay) / 7)+myWeek);
	}
	
	/*
	 * 求出相差的天数
	 * @author sail
	 * */
	public long getNumDay(String time1,String time2) {
		long re_time = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date d1;
		Date d2;
		try {
			d1 = sdf.parse(time1);
			d2 = sdf.parse(time2);
			re_time = d2.getTime() - d1.getTime();
			re_time = re_time /(1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

}

