package lxq.example.luntang;

import lxq.example.table.MyUser;
import me.maxwin.Util.MyToast;
import me.maxwin.Util.cons;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.fydia.campus.R;

/**
 * 
 * 
 * @author linxingqianglai
 * 这个是程序的入口
 *
 */
public class MainActivity extends Activity  {
	/** Called when the activity is first created. */
	FragmentManager fm;
	FragmentTransaction ft;
	MainFragment mf;
	//MyUser表示用户登录
	MyUser user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int maxMemory = (int) Runtime.getRuntime().freeMemory()/(1024);
		Log.e("free_memory=",maxMemory+"k");
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_linxingqiang);
		Bmob.initialize(MainActivity.this, "274deae65b4e084d6c74355fbc9bcb5e");
		user=MyUser.getMyUser();
		user.setUsername("林兴强");
		user.setPassword("123456");
		user.login(this, new SaveListener() {
			    @Override
			    public void onSuccess() {
			        // TODO Auto-generated method stub
			        MyToast.show(MainActivity.this, "登录成功:");
			    }
			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			        MyToast.show(MainActivity.this, "登录失败:"+msg);
			    }
			});
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		mf = new MainFragment();
		Fragment f=fm.findFragmentById(R.id.main_fragment);
		if(f==null)
		{
			ft.add(R.id.main_fragment, mf,cons.MainFragment).commit();
		}
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		return ;
	}
	
	

}
