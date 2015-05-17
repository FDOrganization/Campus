package sail.ui;
/**
 * tab界面构造
 * @ClassName MainActivity
 * @author sail
 * @date 2014.12.9 上午8:09
 * */

import sail.data.LocalInforM;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;

import com.fydia.campus.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnTouchListener,OnGestureListener {
	private final static String SCHEDULE = "schedule_tab";
	private final String MORE = "more_tab";
	private final String INFOR = "information_tab"; 
	private Intent mSchedule;
	private Intent mMore;
	private Intent mInfor;
	private TabHost mTabHost;
	private View Main_View;
	private RadioButton mSRadios;
	private RadioButton mMRadios;
	private RadioButton mIRadios;
	private GestureDetector mDetector;
	private FrameLayout mFrameLayout2;
	private int verticalMinDistance = 20;  
	private int minVelocity         = 0; 
	private Spinner userSpinner;
	private RadioButton[] Radio = new RadioButton[3];
	public static Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Init_Show();
		super.onCreate(savedInstanceState);
		setContentView(Main_View);
		doit();
//		doOnGesture();
	}
	/**
	 * 初始化
	 * @author sail*/
	private void Init_Show(){
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 获得当前手机屏幕大小
		LocalInforM.Ssize = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(LocalInforM.Ssize);
		// 获得需要设置的控件
		LayoutInflater inflater = getLayoutInflater();
		Main_View = inflater.inflate(R.layout.activity_main, null);
		FrameLayout mFrameLayout1 = (FrameLayout)Main_View.findViewById(R.id.SheduleTitle);
		userSpinner = (Spinner)Main_View.findViewById(R.id.UserSpinner);
		mFrameLayout2 = (FrameLayout)Main_View.findViewById(android.R.id.tabcontent);
		LayoutParams frameSetter1 = new LayoutParams(LayoutParams.WRAP_CONTENT
								,LayoutParams.WRAP_CONTENT);
		LayoutParams frameSetter2 = new LayoutParams(LayoutParams.MATCH_PARENT
				,LayoutParams.MATCH_PARENT);
		// 设置宽高
		frameSetter1.height = LocalInforM.Ssize.heightPixels / 13;
		// 赋值标题宽度到存储类,并初始化储存类的相关数据
	
		LocalInforM.TitleSize = frameSetter1.height;
		frameSetter1.width = LocalInforM.Ssize.widthPixels;
		frameSetter2.topMargin = LocalInforM.Ssize.heightPixels / 13;
		mFrameLayout1.setLayoutParams(frameSetter1);
		mFrameLayout2.setLayoutParams(frameSetter2);
		
	}
	/**
	 * 具体操作方法
	 * @author sail*/
	private void doit(){
		mTabHost = getTabHost();
//		 初始化Intent
		Init_Intents();
//		 创建tab
		Step_Intents();
		// 处理Radios
//		Init_Radios();
		// 用户选择回应方法
		doUser();
		
	}
	/**
	 * 	 用户选择回应方法
	 * @author sail*/
	private void doUser(){
		// 获取下拉列表数据源
		String[] strings = getResources().getStringArray(R.array.userspinnerdata);
		// 建立adapter并绑定数据源
		ArrayAdapter<String> m_adataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,strings);
		userSpinner.setAdapter(m_adataAdapter);
		userSpinner.setOnItemSelectedListener(new myOnItemSelectedListener());
	}
	
	/**
	 * spinner下拉列表功能实现类
	 * @ClassName myOnItemSelectedListener
	 * @author sail
	 * */
	private class myOnItemSelectedListener implements OnItemSelectedListener{
	
		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			if (parent.getItemAtPosition(position).toString().equals("导入课表")) {
//				LocalInforM.flag = 121603;
				userSpinner.setSelection(0);
				Intent intent = new Intent(MainActivity.this,LoginActivity.class);
				intent.putExtra("schedule", "yes");
				startActivity(intent);
				System.gc();
			}
			else if(parent.getItemAtPosition(position).toString().equals("查询成绩")){
				userSpinner.setSelection(0);
				Intent intent = new Intent(MainActivity.this,GradeActivity.class);
				startActivity(intent);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	} 
	/** 
	 * 绑定跳转
	 * @author sail
	 * */
	private void Init_Intents(){
		mSchedule = new Intent(this, ScheduleActivity.class);
//		mInfor = new Intent(this, InforActivity.class);
//		mMore = new Intent(this, MoreActivity.class);
	}
	/**
	 * 创建tab
	 * @author sail
	 * */
	private void Step_Intents(){
		((RadioButton) findViewById(R.id.SheduleButton)).setChecked(true);
		mTabHost.addTab(buildTabSpec(SCHEDULE, mSchedule));
//		mTabHost.addTab(buildTabSpec(MORE, mMore));
//		mTabHost.addTab(buildTabSpec(INFOR, mInfor));
		mTabHost.setCurrentTabByTag(SCHEDULE);
		
	}
	/**
	 * 初始化Radios
	 * @author sail*/
	private void Init_Radios(){
		mSRadios = (RadioButton)findViewById(R.id.SheduleButton);
//		mMRadios = (RadioButton)findViewById(R.id.SheduleMore);
//		mIRadios = (RadioButton)findViewById(R.id.SheduleInfor);
		mSRadios.setOnCheckedChangeListener(new myListener());
		mMRadios.setOnCheckedChangeListener(new myListener());
		mIRadios.setOnCheckedChangeListener(new myListener());
		Radio[0] = mSRadios;
//		Radio[1] = mMRadios;
//		Radio[2] = mIRadios;
		// 当前选中标签
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 1){
					Radio[LocalInforM.tabFlag].setChecked(true);
				}
			}
		
		};
	}
	
	/**
	 * 单选监听监听类
	 * @ClassName myListener
	 * @author sail
	 * */
	class myListener implements OnCheckedChangeListener{
		public void onCheckedChanged(CompoundButton v, boolean g) {
			// TODO Auto-generated method stub
			if(g){
				switch (v.getId()) {
				case R.id.SheduleButton:
					mTabHost.setCurrentTabByTag(SCHEDULE);
					break;
				case R.id.SheduleMore:
					mTabHost.setCurrentTabByTag(MORE);
					break;
				case R.id.SheduleInfor:
					mTabHost.setCurrentTabByTag(INFOR);
					break;
				default:
					break;
				}
			}
		}
	}
	
	
	/**
	 * tabSpec对象获得
	 * @author sail*/
		private TabHost.TabSpec buildTabSpec(String tag, Intent intent) {
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
			tabSpec.setContent(intent).setIndicator("",
					getResources().getDrawable(R.drawable.ic_launcher));
			return tabSpec;
		}
		
		
	/**
	 * 手势
	 * @author sail
	 * @date 2014.12.10  下午5:30*/
		
	/*
	 * 手势实现方法
	 * @author sail*/
	private void doOnGesture(){
		mDetector = new GestureDetector((OnGestureListener)this);
		mFrameLayout2.setOnTouchListener(this);
		mFrameLayout2.setLongClickable(true);
		
	}
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
				 if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
					  	if(LocalInforM.tabFlag == 2)
					  		LocalInforM.tabFlag = 0;
					  	else
					  		LocalInforM.tabFlag +=1;
					  	// 切换到当前标签
					  	Radio[LocalInforM.tabFlag].setChecked(true);
//				        Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();  
				    } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
				    	if(LocalInforM.tabFlag == 0)
				    		LocalInforM.tabFlag = 2;
				    	else
				    		LocalInforM.tabFlag -= 1;
				    	Radio[LocalInforM.tabFlag].setChecked(true);
//				        Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();  
				    }  
				return false;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	// 重载onTouch方法
	@Override
	public boolean onTouch(View v, MotionEvent event) {  
	    return mDetector.onTouchEvent(event);  
	} 

}
