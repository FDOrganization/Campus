package sail.internet;
/**
 * 得到成绩类
 * @ClassName GetGrade
 * @author sail
 * @date 2014/12/10 20:21
 * */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import sail.data.LocalInforM;
import sail.data.OperateData;
import sail.ui.LoginActivity;
import sail.ui.MainActivity;
import sail.ui.ProgressDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

import com.fydia.campus.R;


public class GetGrade extends Activity{ 
	public static Handler handler;
	private String mstr=null;
	private String url;
	private String StateView;
	private String name ;
	private final int STATEVIEW_SUCCESS = 1;
	private final int STATEVIEW_FALSE = 0;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String[] time;
	private String[] num={"1","2"};
	// 需要自己抓包自己的连接

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ProgressDialog dialog = new ProgressDialog(this,"正在获取...",R.anim.loading);
		
		setTime();
		try {
			name = NetManager.getNetManager().getName();
			name = URLEncoder.encode(new String(name), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		url = "http://jw.hzau.edu.cn/xscjcx.aspx?xh="+LoginActivity.id+"&xm="+name+"&gnmkdm=N121605";
		LocalInforM.flag = LocalInforM.GETGRADE;
		doshow(url);
		dialog.show();
	}

	@SuppressLint("HandlerLeak")
	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case STATEVIEW_SUCCESS:
				getWeb(url);
				break;
			case STATEVIEW_FALSE:
				Toast.makeText(GetGrade.this, "get ViewState false",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		}
	};
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			// 完成下载跳转
			if (msg.what == 1){
				Intent intent = new Intent();
				intent.setClass(GetGrade.this, MainActivity.class);
				startActivity(intent);
				GetGrade.this.finish();
			}
		};
	};

	private void doshow(final String str) {
		new Thread(new Runnable() {

			public void run() {

				// TODO Auto-generated method stub
				Message msg = new Message();
				if (LoginActivity.netManager.getViewState(str, true) != null) {
					StateView = LoginActivity.netManager.getViewState(str, true);
					msg.what = STATEVIEW_SUCCESS;
				} else
					msg.what = STATEVIEW_FALSE;
				handler1.sendMessage(msg);
			}

		}).start();
	}

	private void getWeb(final String url) {
		new Thread(new Runnable() {
			public void run() {
				for(int i = 0 ; i < time.length ; i++){
					for(int n=0 ; n < 2 ; n++){
						params.add(new BasicNameValuePair("__EVENTTARGET",""));
						params.add(new BasicNameValuePair("ddlXN", time[i]));
						params.add(new BasicNameValuePair("ddlXQ", num[n]));
						params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
						params.add(new BasicNameValuePair("__VIEWSTATE", StateView));
						params.add(new BasicNameValuePair("btn_xq","学期成绩" ));
						mstr = LoginActivity.netManager.getWeb(url, params);
						mJSoup CourseSoup = new mJSoup(mstr);
						// 解析网页,将相关值放入LacalInforM.data之中
						LocalInforM.gradedata = CourseSoup.getGradeCourse();
						// 处理原有表
						@SuppressWarnings("unused")
						OperateData operatedata = new OperateData(GetGrade.this);
						params.clear();
					}
				}
				Message msg2 = new Message();
				msg2.what = 1;
				handler2.sendMessage(msg2);
			}
		}).start();
	}
	/* 给出post的时间
	 * */
	private void setTime(){
		int startYear = 0;                 // 入学年份
		// 得出入学年份
		try {
			startYear = Integer.parseInt(LoginActivity.id.substring(0,4));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int year = c.get(Calendar.YEAR);
		if(year - startYear ==0 )
			time = new String[1];
		else
			time = new String[year - startYear];
		for(int i=0 ; i <= year - startYear ; i++){
			time[i] = String.valueOf(startYear)+"-"+String.valueOf(startYear + 1);
			startYear++;
		}

	}

}


