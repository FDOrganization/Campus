package sail.ui;
/**
 * 成绩查询类<br>
 * @author sail
 * @date 2015/3/16
 * 
 * */
import sail.data.OperateData;
import sail.ui.AddFragment.fralistener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.fydia.campus.R;

public class GradeActivity extends FragmentActivity implements fralistener {
	private static final int ADDGRADE = 0;
	private static final int SHOWGRADE = 1;
	private static final int DELEADDGRADE = 2;
	private static final int SHOWNOGRADE = 3;
	private AddFragment addFragment;
	private OperateData operateData;
	private Button addButton;
	private Button backButton;
	private static int ADD_GRADE_BUTTON_CLICK_NUM = 0;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grade);
		init();
	}
	/**
	 * 初始化
	 * */
	private void init(){
		// 初始控件
		addButton = (Button)findViewById(R.id.add_grade);
		backButton = (Button)findViewById(R.id.backButton);
		backButton.setOnClickListener(new MyButtonListener());
		addButton.setOnClickListener(new MyButtonListener());
		// 初始化fragment
		
		// 初始
		 addFragment = new AddFragment();
		// 构建数据库操作类
		 operateData = new OperateData(GradeActivity.this);
		// 判断当前成绩数据库是否为空
		 if(!operateData.getEmpty("grade")){
			  // 为空
			 fragmentFactory(SHOWNOGRADE);
		 }else{
			 // 不为空
			 fragmentFactory(SHOWGRADE);
		 }
		 
	}
	/**
	 * 添加成绩的监听方法
	 * @author sail
	 * */
    class MyButtonListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.add_grade:
				ADD_GRADE_BUTTON_CLICK_NUM++;
				if(ADD_GRADE_BUTTON_CLICK_NUM % 2 != 0){
					addButton.setBackgroundResource(R.drawable.ic_navigation_close_normal);
					fragmentFactory(ADDGRADE);
				}else{
					addButton.setBackgroundResource(R.drawable.ic_common_add_normal);
					fragmentFactory(DELEADDGRADE);
				}
				
				break;
			case R.id.backButton:
				Intent intent = new Intent(GradeActivity.this, MainActivity.class);
				startActivity(intent);
				GradeActivity.this.finish();
				java.lang.System.gc();
				break;
				
			default:
				break;
			}
		}
    	
    }
	/**
	 * fragment 工厂化方法
	 * @author sail
	 * @param fraName,要求调用fragment的名字
	 * @return null
	 * */
	private void fragmentFactory(int fraName){
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		switch (fraName) {
		case ADDGRADE:
			addFragment.setListener(this);
			fragmentTransaction.add(R.id.frameContent_sail,addFragment);
			fragmentTransaction.addToBackStack(ACTIVITY_SERVICE);
			fragmentTransaction.commit();
			break;
		case DELEADDGRADE:
			fragmentTransaction.remove(addFragment);
//			fragmentTransaction.addToBackStack(ACTIVITY_SERVICE);
			fragmentTransaction.commit();
			break;
		case SHOWGRADE:
			GradeListFragment listFragment = new GradeListFragment();
			fragmentTransaction.replace(R.id.frameContent_sail,listFragment);
			fragmentTransaction.commit();
			break;
		case SHOWNOGRADE:
			NoGradeFragment fragment = new NoGradeFragment();
			fragmentTransaction.replace(R.id.frameContent_sail,fragment);
			fragmentTransaction.commit();
			break;
		default:
			break;
		}
	}
	
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(ADD_GRADE_BUTTON_CLICK_NUM % 2 != 0){
			addButton.performClick();
		}
	}
	@Override
	public void goTo(int i) {
		// TODO Auto-generated method stub
		fragmentFactory(i);
		addButton.performClick();
	}
	

}
