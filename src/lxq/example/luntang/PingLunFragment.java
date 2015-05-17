package lxq.example.luntang;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lxq.example.table.LunTan;
import lxq.example.table.LunTanInstance;
import lxq.example.table.MyUser;
import lxq.example.table.Talk;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.fydia.campus.R;

public class PingLunFragment extends Fragment implements OnItemClickListener,OnClickListener,TextWatcher,IXListViewListener {
	/**
	 * 表示的是每个说说(Item)的Id，以便后面查询用到
	 */
	public static final String OBJECT_ID="objectId";//表示luntan对应的objectId
	//表示评论的内容
	public static final String CONTENT="Content";
	//表示图片的地址
	public static final String IMG_URL="ImgURL";
	/**
	 * 表示Id对应的用户名
	 */
	public static final String USER_NAME="username"; //表示luntan对应的用户名
	private  static final String TIME="time"; 
	/**
	 * objectId 表示的bmob上论坛对应的ID类似于身份证
	 * username 表示luntan的objecctId对应的用户名
	 */
	private String objectId;
	private String username;
	//输入法管理器
	private InputMethodManager imm;
	private View v;
	//表示的是返回键，返回上一个界面
	private TextView back_textView;
	private TextView add_textView;//评论键
	private XListView mListView;//下拉刷新
	private RelativeLayout relativeLayout;
	//表示的输入按钮，左边表示的是输入编辑框
	private TextView input_textView;
	//这里表示的就是输入编辑框，真正实现输入的EditView
	private EditText input_editView;
	private FragmentManager fm;
	//从另一个Fragment中获得的bundle信息
	private Bundle bundle;
	//表示的是评论集
	private static  List<Talk> talks;
	private SharedPreferences preferences;
	private Handler mHandler;//=new Handler();
	private LunTan lunTan;
	private Talk talk=new Talk();
	private Adapter adapter;
	public PingLunFragment() {
		// TODO Auto-generated constructor stub
	}
	/*
	 * 308行附近是根据用户来添加的，到时候需要修改
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fm=getActivity().getFragmentManager();
		//从上一个MainFragment界面传来的bundle参数
		bundle=getArguments();
		objectId=bundle.getString(OBJECT_ID);
		username=bundle.getString(USER_NAME);
		lunTan=new LunTan();
		lunTan.setObjectId(objectId);
		//Log.e("PinglunFragment_"+OBJECT_ID, bundle.getString(OBJECT_ID));
		//MyToast.show(getActivity(),"object="+objectId);
		imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		talks=new ArrayList<Talk>();
		preferences=getActivity().getPreferences(Context.MODE_PRIVATE);
		adapter=new Adapter();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(v==null)
		{
			v=(View)inflater.from(getActivity()).inflate(R.layout.fragment_pinglun_linxingqiang, container,false);
		}
		Init();
		return v;
		
	}
	public void Init()
	{
		back_textView=(TextView)v.findViewById(R.id.fragment_pinglun_back);
		add_textView=(TextView)v.findViewById(R.id.fragment_pinglun_add_pinglun);
		mListView=(XListView)v.findViewById(R.id.fragment_pnglun_xListView);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mHandler=new Handler();
		relativeLayout = (RelativeLayout)v.findViewById(R.id.fragment_pinglun_layout_input);
		input_textView = (TextView)v.findViewById(R.id.fragment_pinglun_bn_input);
		input_editView = (EditText)v.findViewById(R.id.fragment_pinglun_editView_input);
		back_textView.setOnClickListener(this);
		add_textView.setOnClickListener(this);
		input_textView.setOnClickListener(this);
		input_editView.addTextChangedListener(this);
		
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		String time = preferences.getString(TIME, "尚未更新");
		mListView.setRefreshTime(time);
		SimpleDateFormat myFmt = new SimpleDateFormat("MM月dd日 HH时mm分ss秒");
		String t = myFmt.format(new java.util.Date());
		Log.e("time",t);
		preferences.edit().putString(TIME, t).commit();
	}
	//
	@Override
	public void onRefresh() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
					//这里可能会出现线程问题
					// 下拉刷新(从第一页开始装载数据)
					queryData(0, STATE_REFRESH);
			}
		}, 2000);
		//onLoad();
	}

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
		
		BmobQuery<Talk> query_talk = new BmobQuery<Talk>();
		//query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK); 
		query_talk.include("ToLunTan,TalkName");
		query_talk.setLimit(limit);			// 设置每页多少条数据
		query_talk.setSkip(page*limit);		// 从第几条数据开始，
		query_talk.order("-updatedAt");
		LunTan l=LunTanInstance.getLunTan();
		query_talk.addWhereRelatedTo("ToTalk", new BmobPointer(l));
		query_talk.findObjects(getActivity(), new FindListener<Talk>() {
			
			@Override
			public void onSuccess(List<Talk> arg0) {
				// TODO Auto-generated method stub
				Log.e("Success------------","Success="+arg0.size());
				if(arg0.size()>0){
					if(actionType == STATE_REFRESH){
						// 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
						curPage = 0;
						talks.clear();
						//meTalk_textViews.clear();
						//pinglun_textViews.clear();
						//seeAll_textViews.clear();
					}
					
					// 将本次查询的数据添加到bankCards中
					for (int i=0;i<arg0.size();i++) {
						/*BmobQuery<Talk> talks=new BmobQuery<Talk>();
						talks.include("User");
						talks.addWhereRelatedTo("ToTalk", new BmobPointer(arg0.get(i)));
						talks.findObjects(getActivity(), new FindListener<Talk>() {
							
							@Override
							public void onSuccess(List<Talk> t) {
								// TODO Auto-generated method stub
								for(int j=0;j<t.size();j++)
								{
									comments.getLast().set(j, t.get(j).getContent());
								}
							}
							
							@Override
							public void onError(int arg0, String arg1) {
								// TODO Auto-generated method stub
								
							}
						});*/
						talks.add(arg0.get(i));
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
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryData(0, STATE_REFRESH);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*Fragment f=fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft1=fm.beginTransaction();
		ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
		ft1.show(f).commit();
		Log.e("Addfragment_onPause",f.toString());*/
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Fragment f=fm.findFragmentById(R.id.main_fragment);
		//FragmentTransaction ft1=fm.beginTransaction();
		//ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
		//ft1.show(f).commit();
		//Log.e("Addfragment_onStop",f.toString());
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		/*Fragment f=fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft1=fm.beginTransaction();
		//ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
		ft1.show(f).commit();
		Log.e("fragment_onResume",f.toString());*/
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.fragment_pinglun_back)
		{
			Fragment fragment=fm.findFragmentById(R.id.main_fragment);
			FragmentTransaction ft=fm.beginTransaction();
			Fragment mainFragment=fm.findFragmentByTag(cons.MainFragment);
			//ft.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
			ft.remove(fragment).commit();
			ft.show(mainFragment);
		}
		else if(v.getId()==R.id.fragment_pinglun_add_pinglun)
		{
			if (imm.isActive()) { 
				  //如果开启 
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
				 //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的 
				relativeLayout.setVisibility(View.VISIBLE);
				input_editView.setFocusable(true);
			} 
		}
		else if(v.getId()==R.id.fragment_pinglun_bn_input)
		{
			if(TextUtils.isEmpty(input_editView.getText().toString()))
			{
				MyToast.show(getActivity(), "评论内容不能为空");
			}
			else
			{
				if(TextUtils.isEmpty(lunTan.getObjectId())){
			        MyToast.show(getActivity(), "当前用户的object为空");
			        return;
			    }
				//网络添加评论内容
				MyUser user=LunTanInstance.getLunTan().getUser();
				talk.setContent(input_editView.getText().toString());
				talk.setToLunTan(lunTan);
				talk.setTalkName(user);
				talk.save(getActivity(), new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						addTalkToLunTan();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
					Log.e("评论保存失败",arg1);	
					}
				});
				input_editView.setText("");
			}
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	private void addTalkToLunTan(){
	    if(TextUtils.isEmpty(lunTan.getObjectId()) || 
	            TextUtils.isEmpty(talk.getObjectId())){
	        MyToast.show(getActivity(), "当前用户或者当前Talk对象的object为空");
	        return;
	    }

	    BmobRelation t = new BmobRelation();
	    t.add(talk);
	    lunTan.setToTalk(t);
	    lunTan.update(getActivity(), new UpdateListener() {

	        @Override
	        public void onSuccess() {
	            // TODO Auto-generated method stub
	        	MyToast.show(getActivity(), "已成功");
	        }

	        @Override
	        public void onFailure(int arg0, String arg1) {
	            // TODO Auto-generated method stub
	        	MyToast.show(getActivity(), "添加失败");
	        }
	    });
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	private class Adapter extends BaseAdapter
	{
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null)
			{
				convertView=getActivity().getLayoutInflater()
						.from(getActivity()).inflate(R.layout.list_item_pinglun_linxingqiang, parent,false);
			}
			TextView name=(TextView)convertView.findViewById(R.id.list_item_pinglun);
			TextView time=(TextView)convertView.findViewById(R.id.list_item_time);
			TextView content=(TextView)convertView.findViewById(R.id.list_item_content);
			name.setText(talks.get(position).getTalkName().getUsername());
			time.setText(talks.get(position).getUpdatedAt());
			content.setText(talks.get(position).getContent());
			if(content==null)
			{
				Log.e("contextView","contextView");
			}
			if(talks.get(position).getContent()!=null)
			{
				Log.e("talks.get(position).getContent()",talks.get(position).getContent());
			}
			return convertView;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.e("talks.size()",talks.size()+"");
			return talks.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
