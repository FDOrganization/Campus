package jx.push;

import jx.push.fragment.NoticeFragment;
import jx.push.fragment.SettingFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;

import com.fydia.campus.R;

public class PushActivity extends FragmentActivity {
	
	public static final String apiKey = "471d37c8c8bc507aa17b3bf844501e68";

	private Button setButton;
	private Button noticeButton;
	
	private SettingFragment setFragment;
	private NoticeFragment noticeFragment;
	
	private BmobPushManager bmobPush;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jx_activity_main);
		
		Bmob.initialize(this,apiKey);
		bmobPush = new BmobPushManager(this);
		BmobInstallation.getCurrentInstallation(this).save();
		BmobPush.startWork(this, apiKey);
		
		initView();
		init();
		
	}

	

	private void initView() {
		setButton = (Button) findViewById(R.id.btn_set);
		noticeButton = (Button) findViewById(R.id.btn_notice_list);
		noticeButton.setSelected(true);
		
	}
	
	private void init() {
		
		setFragment = new SettingFragment();
		noticeFragment = new NoticeFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, setFragment)
		.add(R.id.fragment_container, noticeFragment).hide(setFragment).show(noticeFragment).commit();
		
		//启动实时数据监听Service
		startService(new Intent(this, NoticeService.class));
		
	}
	public void onTabSelect(View view) {
		FragmentTransaction trx = getSupportFragmentManager().beginTransaction();

		switch (view.getId()) {
		case R.id.btn_notice_list:
            
            	noticeButton.setSelected(true);
            	setButton.setSelected(false);
            	trx.hide(setFragment).show(noticeFragment).commit();
            	if (!noticeFragment.isAdded()) {
    				trx.add(R.id.fragment_container, noticeFragment);
    			}
           
			break;
		case R.id.btn_set:
			
				setButton.setSelected(true);
				noticeButton.setSelected(false);
            	trx.hide(noticeFragment).show(setFragment).commit();
            	if (!setFragment.isAdded()) {
    				trx.add(R.id.fragment_container, setFragment);
    			}
			
			break;
		}
		
	}
}
