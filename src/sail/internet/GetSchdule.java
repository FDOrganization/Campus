package sail.internet;
/**
 * 课表拉取操作Activity
 * @ClassName ShowActivity
 * @author sail
 * @data 2014.12.14 晚上9.30
 * */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import sail.data.LocalInforM;
import sail.data.OperateData;
import sail.ui.LoginActivity;
import sail.ui.MainActivity;
import sail.ui.ProgressDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

import com.fydia.campus.R;


public class GetSchdule extends Activity {
	public static Handler handler;
	private String mstr;
	private String url;
//	private String StateView;
	private String name ;
	private final int STATEVIEW_SUCCESS = 1;
	private final int STATEVIEW_FALSE = 0;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_schedule_main);
		ProgressDialog dialog = new ProgressDialog(this,"正在获取...",R.anim.loading);
		dialog.show();
		try {
			name = NetManager.getNetManager().getName();
			name = URLEncoder.encode(new String(name), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		// 初始化url
		url = "http://jw.hzau.edu.cn/xskbcx.aspx?xh=" + LoginActivity.id
						+ "&xm=" + name + "&gnmkdm=N121603";
//		url = "http://jw.hzau.edu.cn/xskb.aspx?xh="+LoginActivity.id+"&xhxx="+LoginActivity.id+"2014-20152";
		// 置标志为课表拉取
		LocalInforM.flag = LocalInforM.GETSCHDE;
		doshow(url);
		
		
	}
	/*
	 * 等待stateview 获取状态handler
	 * @author sail
	 * */
	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case STATEVIEW_SUCCESS:
				getWeb(url);
				break;
			case STATEVIEW_FALSE:
				Toast.makeText(GetSchdule.this, "get ViewState false",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		}
	};
	/*
	 * 等待网页数据流方法
	 * @author sail
	 * */
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			
			if (msg.what == 1){
				mJSoup CourseSoup = new mJSoup(mstr);
				// 解析网页,将相关值放入LacalInforM.data之中
				LocalInforM.scheduledata = CourseSoup.getScheduleCourse();
				// 处理原有表
				@SuppressWarnings("unused")
				OperateData operatedata = new OperateData(GetSchdule.this);
				// 删除原有表
//				operatedata.DeleteTable(operatedata.SCTABLENAME);
				// 建立新空表
//				operatedata.CreatSql();
				Intent intent = new Intent();
				intent.setClass(GetSchdule.this, MainActivity.class);
				startActivity(intent);
				GetSchdule.this.finish();
			}
		};
	};
	/*
	 * 获取stateview线程
	 * @author sail
	 * @Description 由于这里不需要post StateView 直接赋值mgs.what为STATEVIEW_SUCCESS
	 * */
	private void doshow(final String str) {
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
//				if (LoginActivity.netManager.getViewState(str, true) != null) {
					// 得到StateView的值
//					StateView = LoginActivity.netManager.getViewState(str, true);
					msg.what = STATEVIEW_SUCCESS;
//				} else
//					msg.what = STATEVIEW_FALSE;
				handler1.sendMessage(msg);
			}
		}).start();
	}
	
	/*
	 * 获取网页流线程
	 * @author sail
	 * */
	private void getWeb(final String url) {
		new Thread(new Runnable() {
			public void run() {
				mstr = LoginActivity.netManager.getWeb(url, params);
				Message msg2 = new Message();
				msg2.what = 1;
				handler2.sendMessage(msg2);
			}
		}).start();
	}


}
