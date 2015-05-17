package banner.Load;

import lein.fydia.campus.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.fydia.campus.R;

public class LoadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Òþ²Ø×´Ì¬À¸È«ÆÁ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR,
				WindowManager.LayoutParams.TYPE_STATUS_BAR);
		//
		setContentView(R.layout.loading);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoadActivity.this, MainActivity.class);
				startActivity(intent);
				LoadActivity.this.finish();
			}
		}, 300);
	}

}
