package sail.internet;
/**
 * �α���ȡ����Activity
 * @ClassName ShowActivity
 * @author sail
 * @data 2014.12.14 ����9.30
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
		ProgressDialog dialog = new ProgressDialog(this,"���ڻ�ȡ...",R.anim.loading);
		dialog.show();
		try {
			name = NetManager.getNetManager().getName();
			name = URLEncoder.encode(new String(name), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		// ��ʼ��url
		url = "http://jw.hzau.edu.cn/xskbcx.aspx?xh=" + LoginActivity.id
						+ "&xm=" + name + "&gnmkdm=N121603";
//		url = "http://jw.hzau.edu.cn/xskb.aspx?xh="+LoginActivity.id+"&xhxx="+LoginActivity.id+"2014-20152";
		// �ñ�־Ϊ�α���ȡ
		LocalInforM.flag = LocalInforM.GETSCHDE;
		doshow(url);
		
		
	}
	/*
	 * �ȴ�stateview ��ȡ״̬handler
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
	 * �ȴ���ҳ����������
	 * @author sail
	 * */
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			
			if (msg.what == 1){
				mJSoup CourseSoup = new mJSoup(mstr);
				// ������ҳ,�����ֵ����LacalInforM.data֮��
				LocalInforM.scheduledata = CourseSoup.getScheduleCourse();
				// ����ԭ�б�
				@SuppressWarnings("unused")
				OperateData operatedata = new OperateData(GetSchdule.this);
				// ɾ��ԭ�б�
//				operatedata.DeleteTable(operatedata.SCTABLENAME);
				// �����¿ձ�
//				operatedata.CreatSql();
				Intent intent = new Intent();
				intent.setClass(GetSchdule.this, MainActivity.class);
				startActivity(intent);
				GetSchdule.this.finish();
			}
		};
	};
	/*
	 * ��ȡstateview�߳�
	 * @author sail
	 * @Description �������ﲻ��Ҫpost StateView ֱ�Ӹ�ֵmgs.whatΪSTATEVIEW_SUCCESS
	 * */
	private void doshow(final String str) {
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
//				if (LoginActivity.netManager.getViewState(str, true) != null) {
					// �õ�StateView��ֵ
//					StateView = LoginActivity.netManager.getViewState(str, true);
					msg.what = STATEVIEW_SUCCESS;
//				} else
//					msg.what = STATEVIEW_FALSE;
				handler1.sendMessage(msg);
			}
		}).start();
	}
	
	/*
	 * ��ȡ��ҳ���߳�
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
