package lein.fydia.campus;

import jx.push.NoticeService;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;

import com.fydia.campus.R;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements
		CompoundButton.OnCheckedChangeListener {
	
	private static final String Push_API_KEY = "p471d37c8c8bc507aa17b3bf844501e68";

	private static final String HOME_TAB = "home_tab";
	//private static final String FIND_TAB = "mention_tab";
	private static final String MINE_TAB = "person_tab";
	private static final String MORE_TAB = "more_tab";
	//private static final String MARK_TAB = "mark_tab";

	private Intent mHomeIntent = null;
//	private Intent mFindIntent = null;
	private Intent mMineIntent = null;
	private Intent mMoreIntent = null;
//	private Intent mMarkIntent = null;

	private TabHost mTabHost = null;

	// private TextView mMessageTipsMention = null;
	// private TextView mMessageTipsPerson = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fd);

		mTabHost = getTabHost();
		initIntents();
		// initTips();
		initRadios();
		setupIntents();
		initPush(MainActivity.this);
	}

	private void initPush(Context context) {
		BmobPushManager bmobPush = new BmobPushManager(context);
		BmobInstallation.getCurrentInstallation(this).save();
		BmobPush.startWork(context, Push_API_KEY);
		startService(new Intent(this, NoticeService.class));
	}

	private void initIntents() {
		mHomeIntent = new Intent(this, HomeActivity.class);
		//mFindIntent = new Intent(this, FindActivity.class);
		mMineIntent = new Intent(this, MineActivity.class);
		mMoreIntent = new Intent(this, MoreActivity.class);
		//mMarkIntent = new Intent(this, MarkActivity.class);
	}

	/*
	 * private void initTips() { mMessageTipsMention = (TextView)
	 * findViewById(R.id.message_mention); mMessageTipsPerson = (TextView)
	 * findViewById(R.id.message_person); mMessageTipsMention.setText("2");
	 * mMessageTipsPerson.setText("4"); }
	 */

	private void initRadios() {
		((RadioButton) findViewById(R.id.radio_home))
				.setOnCheckedChangeListener(this);
//		((RadioButton) findViewById(R.id.radio_find))
//				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_mine))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_more))
				.setOnCheckedChangeListener(this);

	}

	private void setupIntents() {
		((RadioButton) findViewById(R.id.radio_home)).setChecked(true);
		mTabHost.addTab(buildTabSpec(HOME_TAB, mHomeIntent));
	//	mTabHost.addTab(buildTabSpec(FIND_TAB, mFindIntent));
		mTabHost.addTab(buildTabSpec(MINE_TAB, mMineIntent));
		mTabHost.addTab(buildTabSpec(MORE_TAB, mMoreIntent));
	//	mTabHost.addTab(buildTabSpec(MARK_TAB, mMarkIntent));
		mTabHost.setCurrentTabByTag(HOME_TAB);
	}

	private TabHost.TabSpec buildTabSpec(String tag, Intent intent) {
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setContent(intent).setIndicator("",
				getResources().getDrawable(R.drawable.icon));
		return tabSpec;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_home:
				mTabHost.setCurrentTabByTag(HOME_TAB);
				break;
//			case R.id.radio_find:
//				mTabHost.setCurrentTabByTag(FIND_TAB);
//				break;
			case R.id.radio_mine:
				mTabHost.setCurrentTabByTag(MINE_TAB);
				break;
			case R.id.radio_more:
				mTabHost.setCurrentTabByTag(MORE_TAB);
				break;
//			case R.id.radio_mark:
//				mTabHost.setCurrentTabByTag(MARK_TAB);
//				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
			quitDialog();
		}
		return

		super.dispatchKeyEvent(event);
	}

	private void quitDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setIcon(null)
				.setCancelable(false)
				.setMessage(R.string.exit_ensure)
				.setPositiveButton(R.string.exit_ensure,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.this.finish();
							}
						})
				.setNegativeButton(R.string.cancel_exit,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create().show();
	}

}
