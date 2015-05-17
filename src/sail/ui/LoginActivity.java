package sail.ui;

/**
 * һ������activity
 * @ClassName LoginAvtivity
 * @author sail
 * @date 2014.12.10 ���� 8:20
 * */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import sail.internet.GetGrade;
import sail.internet.GetSchdule;
import sail.internet.NetManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fydia.campus.R;
@SuppressWarnings("unused")
public class LoginActivity extends Activity {
	
	private String TAG = MainActivity.class.getName();
	private ImageView imageView;
	private Bitmap bitmap;
	public static NetManager netManager;
	public static String id;
	private Button loginBtn;
	private EditText idEdit;
	private EditText pwdEdit;
	private EditText codeEdit;
	private TextView text;
	private String state;
	private final int GET_CODE_SUCCESS = 1;
	private final int GET_CODE_ERROR = 2;
	private final int LOGIN_SUCCESS = 3;
	private final int LOGIN_ERROR = 4;
	private final String LOGIN_URL = "http://jw.hzau.edu.cn/"; // ��½��ָ������
	public static String result = null;
	/*
	 * ������֤���ȡ״̬handler
	 * @author sail
	 * */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int tag = msg.what;
			switch (tag) {
			case GET_CODE_SUCCESS:
				imageView.setImageBitmap(bitmap);
				break;
			case GET_CODE_ERROR:
				
				Toast.makeText(LoginActivity.this, "get code error",
						Toast.LENGTH_SHORT).show();
				break;
			case LOGIN_SUCCESS:
				Toast.makeText(LoginActivity.this, "login success",
						Toast.LENGTH_SHORT).show();
				toShowSchedule();
				break;
			case LOGIN_ERROR:
				new GetCodeThread().start();
				Toast.makeText(LoginActivity.this, "login error",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		init();
		new GetCodeThread().start();
	}
	
	/*
	 * ��ʼ������
	 * @author sail
	 * */
	private void init() {
		netManager = NetManager.getNetManager();
		imageView = (ImageView) findViewById(R.id.login_image);
		loginBtn = (Button) findViewById(R.id.user_login_btn);
		idEdit = (EditText) findViewById(R.id.user_id);
		pwdEdit = (EditText) findViewById(R.id.user_pwd);
		codeEdit = (EditText) findViewById(R.id.user_code);
		text = (TextView) findViewById(R.id.myTextView);
		loginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 id = idEdit.getText().toString().trim();             //���õ���½�������Ϣ
				String pwd = pwdEdit.getText().toString().trim();
				String code = codeEdit.getText().toString().trim();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("__VIEWSTATE",state));
//				params.add(new BasicNameValuePair("txtUserName", "2013310200505"));1
//				params.add(new BasicNameValuePair("TextBox2", "luo5262718"));
				params.add(new BasicNameValuePair("txtUserName", id));
				params.add(new BasicNameValuePair("TextBox2", pwd));
				params.add(new BasicNameValuePair("txtSecretCode", code));
				params.add(new BasicNameValuePair("Button1", ""));
				params.add(new BasicNameValuePair("lbLanguage", ""));
				params.add(new BasicNameValuePair("hidPdrs", ""));
				params.add(new BasicNameValuePair("hidsc", ""));
				login(LOGIN_URL, params);
			}
		});
		// ����֤��ͼƬ�󶨼���
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �ٴ���ȡ��֤��
				new GetCodeThread().start();
			}
		});
	}
	/**
	 * �����֤��
	 * @ClassName GetCodeThread
	 * @author sail
	 * */
	private class GetCodeThread extends Thread {
		@Override
		public void run() {
			Message msg = new Message();
			try {
				
				state = netManager.getViewState(LOGIN_URL,false);
				bitmap = netManager.getcode();
				msg.what = GET_CODE_SUCCESS;
			} catch (Exception e) {
				msg.what = GET_CODE_ERROR;
				e.printStackTrace();
			}
			handler.sendMessage(msg);
		}
	}
	
	/*
	 * ��½�߳�
	 * @author sail
	 * */
	private void login(final String url, final List<NameValuePair> params) {
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				try {
					result = netManager.getWebWithPost(url, params);
				    if(netManager.getName(result))
					// Log.d(TAG, result);
				    	msg.what = LOGIN_SUCCESS;
				    else
				    	msg.what = LOGIN_ERROR;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = LOGIN_ERROR;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	
	
	/**
	 * ��½�ɹ���ת����
	 * @author sail
	 * */
	private void toShowSchedule() {
			String str;
			Intent intent = new Intent();
			// �õ��ϼ�Activity��������תָ��
			Bundle bundle = getIntent().getExtras();
			try{
				str = bundle.get("schedule").toString();
			}catch(Exception e){
				str = "no";
			}
			if(str.equals("yes"))
				// Ϊ
				intent.setClass(LoginActivity.this, GetSchdule.class);
			else
				intent.setClass(LoginActivity.this, GetGrade.class);
			// �������뷨�����
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)). 
			hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS); 
			startActivity(intent);
			LoginActivity.this.finish();
	}
	

}
