package banner.Web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import banner.Data.Person;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.fydia.campus.R;
public class WebActivity extends Activity {

	private String Url;
	private WebView mWebView;
	//得到控件
	private void getGroup() {
		mWebView = (WebView)findViewById(R.id.WebView);
	}
	//操作方法
	private void doit() {
		getGroup();
		//设置支持的js脚本
		WebSettings mSettings = mWebView.getSettings();
		mSettings.setJavaScriptEnabled(true);
		//设置可访问文件
		mSettings.setAllowFileAccess(true);
		//设置支持缩放
		mSettings.setBuiltInZoomControls(true);
		//设置webViewClient
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		//初始化Bmob
		Bmob.initialize(this, "78849d348edb02f2736f49f1bdffbf66");
		
		doit();
		//查询单条数据
		BmobQuery<Person> query = new BmobQuery<Person>();
		final ProgressDialog dialog = ProgressDialog.show(this,"","正在加载请稍候。。。" );
		query.getObject(this,"a8a3094fdc", new GetListener<Person>() {
			
			@Override
			public void onSuccess(Person arg0) {
				// TODO Auto-generated method stub
				Url = arg0.getaddress();
				dialog.dismiss();
				if(URLUtil.isNetworkUrl(Url)) {
					
					mWebView.loadUrl(Url);
					}
								
				else{
					DisPlay("链接错误");
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				DisPlay("获取数据失败");
			}
		});
			
	}
	public void DisPlay(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

}
