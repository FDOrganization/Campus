package lein.fydia.campus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jx.push.PushActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import banner.Data.ImageData;
import banner.Internet.ConnectionDetetor;
import banner.ListAssistClass.ListViewAdapter;
import banner.ListAssistClass.myListView;
import banner.ListAssistClass.myListView.OnRefreshListener;
import banner.Load.ImageDownload;
import banner.Web.WebActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fydia.campus.R;

public class HomeActivity extends Activity implements OnPageChangeListener,
		OnRefreshListener {
	// 图片常量N
	private static int N = 4;
	// 标题 String
	private String[] titles = new String[N];
	// TextView 对象
	private TextView textView;
	// ViewPager 对象
	private ViewPager mViewPager;
	ArrayList<View> mViews;
	private ImageView mImageView;
	// ImagerView 数组
	private ImageView[] mImageViews;
	// 当前图片标号
	private int currentItem = 1;
	private ScheduledExecutorService scheduledExecutorService;
	// LinearLayout 对象
	private LinearLayout layout;
	// ViewGroup 对象
	private ViewGroup Group;
	private static String[] str = new String[N];
	private RelativeLayout relativeLayoutstart, relativeLayoutend,
			relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4;
	private myListView list;
	private View listView;
	private ListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_list);
		// 设置无标题窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// listView = (ListView)findViewById(R.id.mylistView);
		// View 数组
		mViews = new ArrayList<View>();
		// 定义 LayoutInflater 对象
		LayoutInflater inflater = getLayoutInflater();
		// 实例化布局文件
		Group = (ViewGroup) inflater.inflate(R.layout.activity_home, null);
		listView = inflater.inflate(R.layout.activity_list, null);
		View start = inflater.inflate(R.layout.item_start, null);
		View end = inflater.inflate(R.layout.item_end, null);
		View view1 = inflater.inflate(R.layout.item01, null);
		View view2 = inflater.inflate(R.layout.item02, null);
		View view3 = inflater.inflate(R.layout.item03, null);
		View view4 = inflater.inflate(R.layout.item04, null);
		View view = inflater.inflate(R.layout.item_empty, null);
		// 设置图片的透明度
		// ImageView imageView =
		// (ImageView)Group.findViewById(R.id.myHomeImageView);
		// imageView.getBackground().setAlpha(50);
		// 获得item
		relativeLayoutstart = (RelativeLayout) start
				.findViewById(R.id.itemstart);
		relativeLayoutend = (RelativeLayout) end.findViewById(R.id.itemend);
		relativeLayout1 = (RelativeLayout) view1.findViewById(R.id.item1);
		relativeLayout2 = (RelativeLayout) view2.findViewById(R.id.item2);
		relativeLayout3 = (RelativeLayout) view3.findViewById(R.id.item3);
		relativeLayout4 = (RelativeLayout) view4.findViewById(R.id.item4);
		GetList();
		
		Button bbs = (Button) Group.findViewById(R.id.bbs);
		Button schedule = (Button) Group.findViewById(R.id.schedule);
		Button xiaoxiList = (Button) Group.findViewById(R.id.msm);
		
		schedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						sail.ui.MainActivity.class));
			}
		});

		bbs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						lxq.example.luntang.MainActivity.class));
			}
		});
		
		xiaoxiList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						PushActivity.class));
			}
		});

		// 装载View
		// start 、 end 为为实现循环的View 其不可见
		ConnectionDetetor Condetetor = new ConnectionDetetor(this);
		if (!Condetetor.isConnectiongToInternet()) {
			mViews.add(view);
		} else {
			mViews.add(start);
			mViews.add(view1);
			mViews.add(view2);
			mViews.add(view3);
			mViews.add(view4);
			mViews.add(end);
		}
		// 绑定对象
		FindConAndSet();
		textView = (TextView) Group.findViewById(R.id.mHomeTextView);
		mViewPager = (ViewPager) Group.findViewById(R.id.viewpager);
		layout = (LinearLayout) Group.findViewById(R.id.group);
		// 根据list 的大小确定 imageViews的大小
		mImageViews = new ImageView[mViews.size()];
		// 在前第二位到倒数第二位中为 mImageViews 赋值
		for (int i = 1; i < mImageViews.length - 1; i++) {
			mImageView = new ImageView(this);
			// 设置相关属性
			mImageView.setLayoutParams(new LayoutParams(20, 20));
			mImageView.setPadding(0, 0, 0, 0);
			if (i == 1) {
				mImageView
						.setBackgroundResource(R.drawable.page_indicator_focused);

			} else {
				mImageView.setBackgroundResource(R.drawable.page_indicator);
			}
			mImageViews[i] = mImageView;
			// 加载当前View
			layout.addView(mImageViews[i]);

		}
		// listView.addHeaderView(layout);
		/***
		 * 绑定适配器
		 */
		PagerAdapter pa = new PagerAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mViews.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				// TODO Auto-generated method stub
				((ViewPager) container).removeView(mViews.get(position));
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public Object instantiateItem(View arg0, int arg1) {
				// TODO Auto-generated method stub
				((ViewPager) arg0).addView(mViews.get(arg1), 0);
				// 绑定一个监听器，当点击当前 View 时按照预设进行跳转
				(mViews.get(arg1)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (arg0.getId() == R.id.item1) {
							Intent intent = new Intent();
							intent.setClass(HomeActivity.this,
									WebActivity.class);
							startActivity(intent);
						}
					}
				});

				return mViews.get(arg1);
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub

			}
		};

		// setContentView(Group);
		mViewPager.setAdapter(pa);
		mViewPager.setOnPageChangeListener(this);

		// mlist.addHeaderView(Group);
		/*
		 * ArrayList arrayList = new ArrayList(); for(int i=0;i<4;i++){
		 * arrayList.add(" "+i); } adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, arrayList);
		 */
		List<ViewGroup> Listgroup = new ArrayList<ViewGroup>();
		Listgroup.add(Group);
		list = (myListView) listView.findViewById(R.id.mylistview);
		adapter = new ListViewAdapter(Listgroup);
		list.setAdapter(adapter);
		list.setOnRefreshListener(this);
		loadData(myListView.REFRESH);
		// InsRefList();
		setContentView(listView);

	}

	@Override
	// 状态改变的时候调用 arg0 1，2，0 正在滑动，滑动完毕，未滑动
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	// arg0 :当前页面，及你点击滑动的页面。arg1:当前页面偏移的百分比。arg2:当前页面偏移的像素位置
	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		// TODO Auto-generated method stub
		// 在当前页面设置 title
		if (position > 0 && position < mViews.size() - 1) {
			textView.setText(titles[position - 1]);
		}
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		/*
		 * 判断布局数是否大于 3 若大于则可进行无限循环切换
		 */
		if (mViews.size() > 3) {
			if (position < 1) {
				position = mViews.size() - 2;

			} else if (position > mViews.size() - 2) {
				position = 1;
			}
			mViewPager.setCurrentItem(position, false);

		}

		for (int i = 1; i < mImageViews.length - 1; i++) {
			if (i == position) {
				mImageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}

		}
	}

	//
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每10秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 10,
				TimeUnit.SECONDS);
		super.onStart();
	}

	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
			textView.setText(titles[currentItem - 1]); // 切换当前显示的title
		};
	};

	/**
	 * 换行切换任务
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewPager) {
				// System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem % (mViews.size() - 2)) + 1;
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}
	}

	/**
	 * 从服务端获取数据
	 */
	private void GetList() {
		// 初始化Bmob
		Bmob.initialize(this, "78849d348edb02f2736f49f1bdffbf66");
		// 查询数据
		BmobQuery<ImageData> query = new BmobQuery<ImageData>();
		query.findObjects(this, new FindListener<ImageData>() {

			@Override
			public void onSuccess(List<ImageData> arg0) {
				GetDatas(arg0);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 解析服务端数据，并实例化ImageDownload 方法加载相关数据
	 * */
	@SuppressWarnings("unused")
	private void GetDatas(List<ImageData> datas) {
		for (int i = 0; i < N; i++) {
			ImageData temp = datas.get(i);
			titles[i] = temp.GetStr();
			str[i] = temp.Getaddress();
			// 实例化ImageDowload对象，实现下载
			ImageDownload downloadstart = new ImageDownload(str[3],
					relativeLayoutstart);
			ImageDownload downloadend = new ImageDownload(str[0],
					relativeLayoutend);
			ImageDownload download1 = new ImageDownload(str[0], relativeLayout1);
			ImageDownload download2 = new ImageDownload(str[1], relativeLayout2);
			ImageDownload download3 = new ImageDownload(str[2], relativeLayout3);
			ImageDownload download4 = new ImageDownload(str[3], relativeLayout4);
		}
	}

	private void FindConAndSet() {
		/*
		 * Button movie = (Button) Group.findViewById(R.id.movie);
		 * movie.setOnClickListener(new myOnClickListener());
		 */

	}

	class myOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			/*
			 * case R.id.cours: break;
			 */
			default:
				break;
			}
		}

	}

	/******** 下拉刷新操作 ********/

	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case myListView.REFRESH:
				list.onRefreshComplete();
				break;
			}

			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData(myListView.REFRESH);
	}

	private void loadData(final int what) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = handler2.obtainMessage();
				msg.what = what;
				GetList();
				handler2.sendMessage(msg);
			}
		}).start();
	}
}