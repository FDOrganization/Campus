package lein.campus;

import lein.fydia.regist.MyUser;
import lein.fydia.regist.SigninActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.fydia.campus.R;

public class SplashActivity extends Activity {
	
	private final int DELAY_TIME = 1000;// —”≥Ÿ6√Î

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						lein.fydia.campus.MainActivity.class);
				if(isLogin()){
					Toast.makeText(SplashActivity.this,  getBmobUserName()+ "ª∂”≠ªÿ¿¥", 1000).show();
				} 
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}
		}, DELAY_TIME);
		
	}

	protected String getBmobUserName() {
		// TODO Auto-generated method stub
		return BmobUser.getCurrentUser(this, MyUser.class).getUsername();
	}

	protected boolean isLogin() {
		return BmobUser.getCurrentUser(this, MyUser.class) != null;
	}
	
}