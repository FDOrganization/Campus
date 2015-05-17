package lein.fydia.regist;

import lein.fydia.campus.MainActivity;
import me.maxwin.Util.MyToast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.fydia.campus.R;

public class SigninActivity extends Activity {
	TextView user_regist;
	EditText username, password;
	Button signBtn;
	MyUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		Bmob.initialize(this, "78849d348edb02f2736f49f1bdffbf66");
		ImageView user_regist = (ImageView) findViewById(R.id.logo);
		Button signBtn = (Button) findViewById(R.id.sign_in);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		TextView regist = (TextView) findViewById(R.id.regist);
		regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SigninActivity.this,
						RegistActivity.class));
			}
		});
		signBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String s_username = username.getText().toString();
				String s_password = password.getText().toString();

				//final BmobUser user = new BmobUser();
				user=MyUser.getMyUser();
				user.setUsername(s_username);
				user.setPassword(s_password);
				user.login(SigninActivity.this, new SaveListener() {
				    @Override
				    public void onSuccess() {
				        // TODO Auto-generated method stub
//				    	Login.flag=true;
				    	startActivity(new Intent(SigninActivity.this,
								MainActivity.class));
				        MyToast.show(SigninActivity.this, "µÇÂ¼³É¹¦:");
				        
				    }
				    @Override
				    public void onFailure(int code, String msg) {
				        // TODO Auto-generated method stub
				        MyToast.show(SigninActivity.this, "µÇÂ¼Ê§°Ü:"+msg);
				    }
				});
				/*user.setUsername(s_username);
				user.setPassword(s_password);
				user.login(SigninActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {
						startActivity(new Intent(SigninActivity.this,
								MainActivity.class));
						Toast.makeText(SigninActivity.this, "µÇÂ¼³É¹¦",
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(SigninActivity.this, "µÇÂ¼Ê§°Ü£º" + arg1,
								Toast.LENGTH_SHORT).show();
					}
				});*/

			}
		});
	}

}
