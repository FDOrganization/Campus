package sail.ui;
/**
 * �α�������Activity
 * @ClassName schedule
 * @author sail
 * @date 2014.12.11 ����10:23
 * --------------------------------
 * @date 2014.12.21 ����4:57 �Ķ�selectButtonѡ�з�ʽ
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
	public static int flag = 1;    				//����ť�������
	public static OperateData operatedata;
	private HorizontalListView horizontalListView;
	private myOnItemOnclick itemOnclick ;
	private HorListBaseAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �õ���ǰ��ʱ��
		getTime();
		// �õ���ʼ��������
		getWeek();
		Init();
		set_Ssize();
		setContentView(ScheduleView);
		onListener();
		do_horizontalList();
		/*
		 * ����ѡ��֪ͨhandler
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
	 * ��ʼ��
	 * @author sail
	 * */
	private void Init(){
		// ���Fragment
		addFragment();
		// �õ�LayoutInflater���� 
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
	 * ����������ѧ��
	 * @author sail
	 * */
	private void setDay(){
		curText.setText("��ǰ \n ��"+LocalInforM.curWeek+"��");
	}
	
	
	/* 
	 * ����View
	 * @author sail
	 * */
	private void set_Ssize(){
		// ����RelativeLayout.layoutparams
		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
				,(LocalInforM.Ssize.heightPixels /15));
		// title�������
		LocalInforM.TitleSize += layoutParams1.height; 
		// ȡ�ÿؼ�
		RelativeLayout relativeLayout1 = (RelativeLayout)ScheduleView.findViewById(R.id.oTableRela);
		RelativeLayout relativeLayout2 = (RelativeLayout)ScheduleView.findViewById(R.id.sTableRela);
		// ����
		relativeLayout1.setLayoutParams(layoutParams1);
		horListView.setVisibility(View.GONE);
		relativeLayout2.addView(horListView);

	}


	/*
	 * horizontalList�¼�����
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
	 * item�¼�������
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
	 * һ��button�����¼�
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
				// ��������ѡ��view���ٴε��������
				flag++;
				if(flag % 2 == 0){
					getWeek();
					horListView.setVisibility(View.VISIBLE);
					horizontalListView.scrollTo(LocalInforM.curWeek*120);
				}
				else
				{	
					adapter.selectIndex = LocalInforM.curWeek; // ����ѡ��
					getWeek();
					position = adapter.selectIndex;
					if(position != LocalInforM.curWeek){ //�жϵ�ǰѡ�е�����������ȷ�����Ƿ�һ��
						LocalInforM.curWeek = position;
						selectText.setText("ѡ�е�"+position+"��\n"+"��˷���");
						selectText.setTextSize(10);
						selectText.setTextColor(Color.RED);
						selectText.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								getWeek();             // ������ȷ����
								selectText.setText(null);
								adapter.selectIndex = LocalInforM.curWeek; // ����ѡ��
								adapter.notifyDataSetChanged();         // ���ݸ���
								refreFragment(true);
							}
						});
					}
					// һ��ʱ��Ҫ�κβ���
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
	 * dialog �¼�����
	 * @MethodName dialog
	 * @author sail
	 * */
	protected void dialog(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
		builder.setMessage("��Ϊ����");
		builder.setNegativeButton("ȡ��", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("ȷ��", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				LocalInforM.curWeek = position+1;
				// ��������
				curText.setText("��ǰ \n ��"+LocalInforM.curWeek+"��");
				// ��������
				dataSave();
				// ����list
				adapter.notifyDataSetChanged();
				refreFragment(true);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	

	/*
	 * ���һ���µ�Fragment
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
	 * ˢ��Frament
	 * @author sail
	 * */
	private void refreFragment(boolean flag){
		FragmentManager fragmentManager = getSupportFragmentManager() ;
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		TableFragment fragement2 = new TableFragment();
		// ���ö���
		if(flag)
			fragmentTransaction.setCustomAnimations(R.anim.fly_enter, R.anim.fly_out);
		fragmentTransaction.replace(R.id.TableFrameLayout, fragement2);
		fragmentTransaction.commit();
	}
	
	
	/*
	 *  ���ʱ��
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
	 * ����������ݵ�����
	 * @author sail
	 * */
	private void dataSave(){
		// ȡ�û��preferences����
		SharedPreferences preferences = getPreferences(0);
		// ȡ�ñ༭����
		SharedPreferences.Editor editor = preferences.edit();
		// ������ȷ��
		editor.putInt("curWeekDay", LocalInforM.curWeekDay);
		editor.putInt("curWeek",LocalInforM.curWeek);
		editor.putInt("year", LocalInforM.year);
		editor.putInt("month", LocalInforM.month);
		editor.putInt("day", LocalInforM.day);
		// ����
		editor.commit();
	}
	
	/*
	 * ����һ����ʼ�ܲ����ݵ�ǰ�����ж������ǵڼ���
	 * @author sail
	 * */
	private void getWeek(){
		SharedPreferences sPreferences = getPreferences(Activity.MODE_PRIVATE);
		myCurWeekDay = sPreferences.getInt("curWeekDay", 1);
		int myWeek = sPreferences.getInt("curWeek", 1);
		int myYear = sPreferences.getInt("year", 2015);
		int myMonth = sPreferences.getInt("month",2);
		int myDay = sPreferences.getInt("day",2);
		//ת��Ϊʱ���
		String time1 = myYear+"��"+myMonth+"��"+myDay+"��";
		String time2 = LocalInforM.year+"��"+LocalInforM.month+"��"+LocalInforM.day+"��";
		// ���7������
			LocalInforM.curWeek = ((int)((getNumDay(time1, time2)+myCurWeekDay) / 7)+myWeek);
	}
	
	/*
	 * �����������
	 * @author sail
	 * */
	public long getNumDay(String time1,String time2) {
		long re_time = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
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

