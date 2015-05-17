package lein.fydia.regist;

import lxq.example.luntang.MainActivity;
import me.maxwin.Util.MyToast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.fydia.campus.R;

public class RegistActivity extends Activity {
	public EditText username, password;
	public Button regBtn;
	MyUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		Bmob.initialize(this, "78849d348edb02f2736f49f1bdffbf66");
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		Button regButton = (Button) findViewById(R.id.user_regist);
		ImageView user_regist = (ImageView) findViewById(R.id.logo);
		regButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String s_username = username.getText().toString();
				String s_password = password.getText().toString();
				user=new MyUser();
				
				user.setUsername(s_username);
				user.setPassword(s_password);

				user.signUp(RegistActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {

						Intent intent = new Intent(RegistActivity.this,
								SigninActivity.class);
						startActivity(intent);
						Toast.makeText(RegistActivity.this, "×¢²á³É¹¦",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(RegistActivity.this, "×¢²áÊ§°Ü£º" + arg1,
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
	}

}
