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
	// ͼƬ����N
	private static int N = 4;
	// ���� String
	private String[] titles = new String[N];
	// TextView ����
	private TextView textView;
	// ViewPager ����
	private ViewPager mViewPager;
	ArrayList<View> mViews;
	private ImageView mImageView;
	// ImagerView ����
	private ImageView[] mImageViews;
	// ��ǰͼƬ���
	private int currentItem = 1;
	private ScheduledExecutorService scheduledExecutorService;
	// LinearLayout ����
	private LinearLayout layout;
	// ViewGroup ����
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
		// �����ޱ��ⴰ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// listView = (ListView)findViewById(R.id.mylistView);
		// View ����
		mViews = new ArrayList<View>();
		// ���� LayoutInflater ����
		LayoutInflater inflater = getLayoutInflater();
		// ʵ���������ļ�
		Group = (ViewGroup) inflater.inflate(R.layout.activity_home, null);
		listView = inflater.inflate(R.layout.activity_list, null);
		View start = inflater.inflate(R.layout.item_start, null);
		View end = inflater.inflate(R.layout.item_end, null);
		View view1 = inflater.inflate(R.layout.item01, null);
		View view2 = inflater.inflate(R.layout.item02, null);
		View view3 = inflater.inflate(R.layout.item03, null);
		View view4 = inflater.inflate(R.layout.item04, null);
		View view = inflater.inflate(R.layout.item_empty, null);
		// ����ͼƬ��͸����
		// ImageView imageView =
		// (ImageView)Group.findViewById(R.id.myHomeImageView);
		// imageView.getBackground().setAlpha(50);
		// ���item
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

		// װ��View
		// start �� end ΪΪʵ��ѭ����View �䲻�ɼ�
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
		// �󶨶���
		FindConAndSet();
		textView = (TextView) Group.findViewById(R.id.mHomeTextView);
		mViewPager = (ViewPager) Group.findViewById(R.id.viewpager);
		layout = (LinearLayout) Group.findViewById(R.id.group);
		// ����list �Ĵ�Сȷ�� imageViews�Ĵ�С
		mImageViews = new ImageView[mViews.size()];
		// ��ǰ�ڶ�λ�������ڶ�λ��Ϊ mImageViews ��ֵ
		for (int i = 1; i < mImageViews.length - 1; i++) {
			mImageView = new ImageView(this);
			// �����������
			mImageView.setLayoutParams(new LayoutParams(20, 20));
			mImageView.setPadding(0, 0, 0, 0);
			if (i == 1) {
				mImageView
						.setBackgroundResource(R.drawable.page_indicator_focused);

			} else {
				mImageView.setBackgroundResource(R.drawable.page_indicator);
			}
			mImageViews[i] = mImageView;
			// ���ص�ǰView
			layout.addView(mImageViews[i]);

		}
		// listView.addHeaderView(layout);
		/***
		 * ��������
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
				// ��һ�����������������ǰ View ʱ����Ԥ�������ת
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
	// ״̬�ı��ʱ����� arg0 1��2��0 ���ڻ�����������ϣ�δ����
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	// arg0 :��ǰҳ�棬������������ҳ�档arg1:��ǰҳ��ƫ�Ƶİٷֱȡ�arg2:��ǰҳ��ƫ�Ƶ�����λ��
	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		// TODO Auto-generated method stub
		// �ڵ�ǰҳ������ title
		if (position > 0 && position < mViews.size() - 1) {
			textView.setText(titles[position - 1]);
		}
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		/*
		 * �жϲ������Ƿ���� 3 ��������ɽ�������ѭ���л�
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
		// ��Activity��ʾ������ÿ10�����л�һ��ͼƬ��ʾ
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 10,
				TimeUnit.SECONDS);
		super.onStart();
	}

	protected void onStop() {
		// ��Activity���ɼ���ʱ��ֹͣ�л�
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	// �л���ǰ��ʾ��ͼƬ
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mViewPager.setCurrentItem(currentItem);// �л���ǰ��ʾ��ͼƬ
			textView.setText(titles[currentItem - 1]); // �л���ǰ��ʾ��title
		};
	};

	/**
	 * �����л�����
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewPager) {
				// System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem % (mViews.size() - 2)) + 1;
				handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
			}
		}
	}

	/**
	 * �ӷ���˻�ȡ����
	 */
	private void GetList() {
		// ��ʼ��Bmob
		Bmob.initialize(this, "78849d348edb02f2736f49f1bdffbf66");
		// ��ѯ����
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
	 * ������������ݣ���ʵ����ImageDownload ���������������
	 * */
	@SuppressWarnings("unused")
	private void GetDatas(List<ImageData> datas) {
		for (int i = 0; i < N; i++) {
			ImageData temp = datas.get(i);
			titles[i] = temp.GetStr();
			str[i] = temp.Getaddress();
			// ʵ����ImageDowload����ʵ������
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

	/******** ����ˢ�²��� ********/

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