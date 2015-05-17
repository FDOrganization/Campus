package jx.push;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.fydia.campus.R;

@SuppressLint("NewApi")
public class WebViewActivity extends Activity implements View.OnClickListener{
	
	private WebView mWebView;
	private String  mUrl;
	private ImageButton mback;
	private ImageButton mforward;
	private ImageButton mrefresh;
	private final String url = "http://news.hzau.edu.cn/2014/1222/40937.shtml";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jx_activity_web);
		initView();
		initWeb();// ִ��WebView��ʼ������
		init();
	}

	private void init() {
		Intent mIntent = getIntent();
		if(mIntent != null){
			Bundle bundle = mIntent.getExtras();		
			if(bundle!=null){
				mUrl = bundle.getString("mcontentUrl");
				mWebView.loadUrl(mUrl);
				Log.d("url",mUrl);
			}
		}
	}



	private void initView() {
		// TODO Auto-generated method stub
		mWebView = (WebView) findViewById(R.id.myweb);
		mback = (ImageButton) findViewById(R.id.btn_back);
		mforward = (ImageButton) findViewById(R.id.btn_go);
		mrefresh = (ImageButton) findViewById(R.id.btn_refresh);
		
		mback.setOnClickListener(this);
		mforward.setOnClickListener(this);
		mrefresh.setOnClickListener(this);
		
	}
	

	private void initWeb() {
		// TODO Auto-generated method stub
		mWebView.setScrollBarStyle(0);// ���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true); // ����֧��javascript�ű�
		ws.setAllowFileAccess(true); // ��������ļ�
		ws.setBuiltInZoomControls(true); // ���ò���ʾ���Ű�ť
		ws.setSupportZoom(true); // ֧������
		ws.setDisplayZoomControls(false);
		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		});
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWebView.removeAllViews();
		mWebView.destroy();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back: // ����
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
			}
			break;
		case R.id.btn_go: // ǰ��
			if (mWebView != null && mWebView.canGoForward()) {
				mWebView.goForward();
			}
			break;
		case R.id.btn_refresh: // ˢ��
			if (mWebView != null) {
				mWebView.reload();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null
				&& mWebView.canGoBack()) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			WebViewActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
