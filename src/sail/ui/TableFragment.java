package sail.ui;
/**
 * 课表界面fragment
 * @author sail
 * */
import sail.XmlAssist.ClassInfo;
import sail.XmlAssist.myScheduleView;
import sail.XmlAssist.myScheduleView.OnItemClassClickListener;
import sail.data.OperateData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.fydia.campus.R;

public class TableFragment extends Fragment{
	private View view;
	private myScheduleView myScheduleView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =inflater.inflate(R.layout.activity_table,container,false);
//		view.setBackgroundResource(R.drawable.a1);
		view.setBackgroundColor(Color.argb(255, 220, 220,220));
//		view.setAlpha(1);
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		in_It();
		addDate();
		onListener();
//		setGestrue();
	}
	/*
	 * 初始化*/
	private void in_It(){
		myScheduleView = (myScheduleView)view.findViewById(R.id.scheduleView);
	}
	/*
	 * 加载数据*/
	private void addDate(){
		OperateData operatedata = new OperateData(getActivity());
		// 数据库为空
		if(!operatedata.getEmpty("schedule")){
//			// 建表
			operatedata.CreatSql();
			// 设置不可touch
			myScheduleView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
						return false;
					}});
			}
		else{
			myScheduleView.setClassList(operatedata.getClassInfo_from_db());
		}
	}
	
	/*
	 * 绑定监听*/
	private void onListener(){
		myScheduleView.setOnItemClassClickListener(new OnItemClassClickListener() {
			
			@Override
			public void onClick(ClassInfo classInfo) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}
}
