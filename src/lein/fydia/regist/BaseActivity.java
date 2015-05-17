package lein.fydia.regist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// ≥ı ºªØBmobSDK
		Bmob.initialize(this, "a9d42072f7370933b333731c0a9ff209");
		
	}
	
	
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
}
