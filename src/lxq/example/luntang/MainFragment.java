package lxq.example.luntang;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lxq.example.table.LunTan;
import lxq.example.table.LunTanInstance;
import me.maxwin.Util.ImageLoader;
import me.maxwin.Util.MyToast;
import me.maxwin.Util.cons;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fydia.campus.R;


/*
 * 作者：林兴强
 * 时间：2014年12月18日
 * 作用：实现论坛的观看的功能，包括下拉刷新，服务器上获取数据，并把数据显示
 * 在item上
 * 
 * */
/**
 * 
 * @author linxingqianglai
 * @version 1.0
 * 功能实现：
 * 一个返回键：返回到上一个界面
 * 一个添加说说键：跳转到添加说说界面
 * 一个下拉刷新：实现浏览说说，及时更新说说内容
 * 不足：界面需要改进，同时缓存需要做好(缓存已经做好
 * ，利用的是一个全局的静态变量，使得下载保存的对象得以留下来）；
 */
public class MainFragment extends Fragment implements OnItemSelectedListener,IXListViewListener,OnClickListener,OnItemClickListener {
	//保存手机上次刷新的时间放在手机的SharedPreferences
	public final static String TIME="time";
	//下拉刷新的listView
	private XListView mListView;
	//mListView对应的适配器adapter
	private Adapter adapter;
	//返回键
	private ImageView mBack;
	//这边的作用是添加说说的作用
	private Spinner mActionBar;
	private Handler mHandler;
	private View v;
	private FragmentManager fm;
	//保存时间,这里有个容易忽略的就是容易忘记提交事务，导致数据存储失败preferences.commit();
	private SharedPreferences preferences;
	//每个Item的内容,并且是静态的，所有既有缓存的作用
	//，能在一打开的时候就能有内容显示在ListView上
	public   static  LinkedList<LunTan> lunTans= new LinkedList<LunTan>();
	//这边的作用的是异步加载图片
	private ImageLoader imageLoader;
	//private LinkedList<TextView> seeAll_textViews=new LinkedList<TextView>();
	//private LinkedList<TextView> pinglun_textViews=new LinkedList<TextView>();
	//private LinkedList<TextView> meTalk_textViews=new LinkedList<TextView>();
	//LruCache<String,LinkedList<LunTan>> lunTanCache  = new LruCache<String, LinkedList<LunTan>>(512*1024);;         
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fm=getActivity().getFragmentManager();
		//Fragment f=fm.findFragmentById(R.id.main_fragment);
		preferences=getActivity().getPreferences(Context.MODE_PRIVATE);
		String time=preferences.getString(TIME, "错误");
		if(time.equals("错误"))
		{
			SimpleDateFormat myFmt=new SimpleDateFormat("MM月dd日 HH时mm分ss秒");
			//Log.e("new java.util.Date()="+myFmt.format(new java.util.Date()),".....");
			String t=myFmt.format(new java.util.Date());
			//下面注意的点是要commit（），否则就没有效果，如果FragmentTransaction一样，有没有Commit（）就不能显示出来
			preferences.edit().putString(TIME, t).commit();
		}
		adapter=new Adapter();
		imageLoader=new ImageLoader(getActivity());
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Fragment f=fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft=fm.beginTransaction();
		ft.show(f).commit();
		v=(View)inflater.from(getActivity()).inflate(R.layout.fragment_main_linxingqiang, container,false);
		Init();
		return v;
	}
	/**
	 * onStart（），onSume（）希望的作用是实现当后一个Fragment消失之后，
	 * 可以返回并且能够显示到Activity当前的界面来
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Fragment f=fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft=fm.beginTransaction();
		ft.show(f).commit();
		Log.e("fragment_onActivityCreated",f.toString());
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(hidden)
		{
			onResume();
		}
	}
	public void Init()
	{
		/**
		 * mBack 返回键：返回上一个界面
		 * mActionBar 添加说说的ActionBar功能
		 * mListView  下拉刷新
		 */
		mBack=(ImageView)v.findViewById(R.id.back);
		mActionBar=(Spinner)v.findViewById(R.id.action_bar);
		mListView = (XListView)v.findViewById(R.id.xListView);
		mListView.setAdapter(adapter);
		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
		List<String> list=new ArrayList<String>();
		list.add("");
		list.add(getResources().getString(R.string.add_content));
		ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_spinner_item,list);
		mActionBar.setAdapter(spinnerAdapter);
		mActionBar.setOnItemSelectedListener(this);
	}
	/**
	 * 停止加载
	 */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		String time=preferences.getString(TIME, "尚未更新");
		mListView.setRefreshTime(time);
		SimpleDateFormat myFmt=new SimpleDateFormat("MM月dd日 HH时mm分ss秒");
		String t=myFmt.format(new java.util.Date());
		preferences.edit().putString(TIME, t).commit();
	}
	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try{
					//这里可能会出现线程问题
					// 下拉刷新(从第一页开始装载数据)
					queryData(0, STATE_REFRESH);
				}
				catch(Exception e)
				{
					
				}
			}
		}, 2000);
	}
	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 上拉加载更多(加载下一页数据)
				queryData(curPage, STATE_MORE);
			}
		}, 2000);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.back)
		{
			getActivity().finish();
		}
		
	}
	/**
	 * 这是在2014年12月29号添加的，目的在于实现每个item的点击跳转实现评论查看功能
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int i=position;
		Fragment f = fm.findFragmentById(R.id.main_fragment);
		PingLunFragment pinglunFragment=new PingLunFragment();
		FragmentTransaction ft=fm.beginTransaction();
		Bundle bunlBundle=new Bundle();
		bunlBundle.putString(PingLunFragment.OBJECT_ID,lunTans.get(i).getObjectId());
		bunlBundle.putString(PingLunFragment.USER_NAME, lunTans.get(i).getUser().getUsername());
		pinglunFragment.setArguments(bunlBundle);
		LunTanInstance.setLunTan(lunTans.get(i));
	    ft.add(R.id.main_fragment, pinglunFragment).commit();
	    ft.hide(f);

	}
	//这里的 作用是添加说说的监听类
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent.getId()==R.id.action_bar)
		{
			if(position==1)
			{
				mActionBar.setSelection(0);
				Fragment f=fm.findFragmentById(R.id.main_fragment);
				//转到添加说说一个Fragment中
				AddItemFragment addItemFragment = new AddItemFragment();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);//,R.anim.animation_view,R.anim.animation_view_exit);
				//transaction.replace(R.id.main_fragment, fragment).commit();    
				transaction.add(R.id.main_fragment, addItemFragment,cons.AddItemFragment).commit();
				transaction.hide(f);
				
			}
		}
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}
	class Adapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lunTans.size();
		}@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//if(convertView==null)
			//{
				convertView=getActivity().getLayoutInflater()
						.from(getActivity()).inflate(R.layout.list_item_linxingqiang, parent,false);
			//}
			//获取图片的地址
			//for(int i=0;i<lunTans.get(position).getImageURL().size();i++)
			//Log.e("LunTans.ImageURl"+position+"=",lunTans.get(position).getImageURL().get(i));
			//user_textView的作用是显示每个Item的姓名
			TextView user_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_name);
			//显示每个Item发表的时间
			TextView time_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_time);
			//显示每个Item的内容
			TextView content_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_content);
			//显示每个Item的图片
			ImageView img_ImageView=(ImageView)convertView.findViewById(R.id.list_item_luntang_ContentImg);
			//图片显示
			if(lunTans.get(position).getImageURL()!=null&&!lunTans.get(position).getImageURL().equals(""))
			{
				String picPath = lunTans.get(position).getImageURL();
				Log.e("MainFragment---lunTans.get("+position+").getImageURL()=",picPath);
				imageLoader.DisplayImage(lunTans.get(position).getImageURL(), img_ImageView);
			}
			//TextView pinglun_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_pinglun);
			//TextView zan_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_zan);
			//Log.e("User----------",lunTans.get(position).getUser().getUsername());
			user_textView.setText(lunTans.get(position).getUser().getUsername());
			time_textView.setText(lunTans.get(position).getUpdatedAt());
			content_textView.setText(lunTans.get(position).getContent());
			//zan_textViews.set(position, zan_textView);
			//zan_textView.setTag(position);
			//zan_textView.setText(lunTans.get(position).getZan()+"");
			/*zan_textViews.get(position).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int i=Integer.parseInt(v.getTag().toString());
					lunTans.get(i).increment("Zan");
					lunTans.get(i).update(getActivity());
					int zanNum=0;
					if(lunTans.get(i).getZan()!=0)
					{
						zanNum=lunTans.get(i).getZan();//Integer.parseInt(lunTans.get(i).getZan().toString());
					}
					
					if(zan_textViews.get(i)!=null)
					{
						Log.e("zan_textView_Tag",i+"");
						
						zanNum+=1;
						lunTans.get(i).setZan(zanNum);
						Log.e("zan_textView_zanNum",zanNum+"0"+"");
						zan_textViews.get(i).setText(zanNum+"");
						zan_textViews.get(i).setTextColor(Color.RED);
					}
					
				}
			});*/
			//pinglun_textViews.add(pinglun_textView);
			//pinglun_textView.setTag(position);
			/*pinglun_textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//MyToast.show(getActivity(), "评论"+v.getTag());
					Log.e("评论-------------","评论");
					int i=Integer.parseInt(v.getTag().toString());
					Fragment f=fm.findFragmentById(R.id.main_fragment);
					PingLunFragment fragment=new PingLunFragment();
					FragmentTransaction ft=fm.beginTransaction();
					Bundle bunlBundle=new Bundle();
					//bunlBundle.putString(PingLunFragment.OBJECT_ID, "i successful");
					bunlBundle.putString(PingLunFragment.OBJECT_ID,lunTans.get(i).getObjectId());
					bunlBundle.putString(PingLunFragment.USER_NAME, lunTans.get(i).getUser().getUsername());
					
					fragment.setArguments(bunlBundle);
					LunTanInstance.setLunTan(lunTans.get(i));
				    ft.add(R.id.main_fragment, fragment).commit();
				    ft.hide(f);
				}
			});*/
			return convertView;
		}
	}
	private static final int STATE_REFRESH = 0;// 下拉刷新
	private static final int STATE_MORE = 1;// 加载更多
	private int limit = 10;		// 每页的数据是10条
	private int curPage = 0;
	/**
	 * 分页获取数据
	 * @param page	页码
	 * @param actionType	ListView的操作类型（下拉刷新、上拉加载更多）
	 */
	private void queryData(final int page, final int actionType){
		Log.i("bmob", "pageN:"+page+" limit:"+limit+" actionType:"+actionType);
		
		BmobQuery<LunTan> query = new BmobQuery<LunTan>();
		//query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK); 
		query.include("ToUser");
		query.order("-createdAt");
		query.setLimit(limit);			// 设置每页多少条数据
		query.setSkip(page*limit);		// 从第几条数据开始，
		query.findObjects(getActivity(), new FindListener<LunTan>() {
			
			@Override
			public void onSuccess(List<LunTan> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()>0){
					if(actionType == STATE_REFRESH){
						// 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
						curPage = 0;
						lunTans.clear();
						//imageLoader.clearCache();的作用是用来除去缓存
						//imageLoader.clearCache();
						//hash.clear();
						//meTalk_textViews.clear();
						//pinglun_textViews.clear();
						//seeAll_textViews.clear();
					}
					
					// 将本次查询的数据添加到bankCards中
					for (int i=0;i<arg0.size();i++) {
						lunTans.add(arg0.get(i));
						//zan_textViews.add(textView);
					}
					
					// 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
					curPage++;
					MyToast.show(getActivity(),"第"+(page+1)+"页数据加载完成");
				}else if(actionType == STATE_MORE){
					MyToast.show(getActivity(),"没有更多数据了");
				}else if(actionType == STATE_REFRESH){
					MyToast.show(getActivity(),"没有数据");
				}
				onLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				MyToast.show(getActivity(),"查询失败:"+arg1);
				onLoad();
			}
		});
	}
	

}
