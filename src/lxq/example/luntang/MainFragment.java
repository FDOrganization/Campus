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
 * ���ߣ�����ǿ
 * ʱ�䣺2014��12��18��
 * ���ã�ʵ����̳�Ĺۿ��Ĺ��ܣ���������ˢ�£��������ϻ�ȡ���ݣ�����������ʾ
 * ��item��
 * 
 * */
/**
 * 
 * @author linxingqianglai
 * @version 1.0
 * ����ʵ�֣�
 * һ�����ؼ������ص���һ������
 * һ�����˵˵������ת�����˵˵����
 * һ������ˢ�£�ʵ�����˵˵����ʱ����˵˵����
 * ���㣺������Ҫ�Ľ���ͬʱ������Ҫ����(�����Ѿ�����
 * �����õ���һ��ȫ�ֵľ�̬������ʹ�����ر���Ķ����������������
 */
public class MainFragment extends Fragment implements OnItemSelectedListener,IXListViewListener,OnClickListener,OnItemClickListener {
	//�����ֻ��ϴ�ˢ�µ�ʱ������ֻ���SharedPreferences
	public final static String TIME="time";
	//����ˢ�µ�listView
	private XListView mListView;
	//mListView��Ӧ��������adapter
	private Adapter adapter;
	//���ؼ�
	private ImageView mBack;
	//��ߵ����������˵˵������
	private Spinner mActionBar;
	private Handler mHandler;
	private View v;
	private FragmentManager fm;
	//����ʱ��,�����и����׺��Եľ������������ύ���񣬵������ݴ洢ʧ��preferences.commit();
	private SharedPreferences preferences;
	//ÿ��Item������,�����Ǿ�̬�ģ����м��л��������
	//������һ�򿪵�ʱ�������������ʾ��ListView��
	public   static  LinkedList<LunTan> lunTans= new LinkedList<LunTan>();
	//��ߵ����õ����첽����ͼƬ
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
		String time=preferences.getString(TIME, "����");
		if(time.equals("����"))
		{
			SimpleDateFormat myFmt=new SimpleDateFormat("MM��dd�� HHʱmm��ss��");
			//Log.e("new java.util.Date()="+myFmt.format(new java.util.Date()),".....");
			String t=myFmt.format(new java.util.Date());
			//����ע��ĵ���Ҫcommit�����������û��Ч�������FragmentTransactionһ������û��Commit�����Ͳ�����ʾ����
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
	 * onStart������onSume����ϣ����������ʵ�ֵ���һ��Fragment��ʧ֮��
	 * ���Է��ز����ܹ���ʾ��Activity��ǰ�Ľ�����
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
		 * mBack ���ؼ���������һ������
		 * mActionBar ���˵˵��ActionBar����
		 * mListView  ����ˢ��
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
	 * ֹͣ����
	 */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		String time=preferences.getString(TIME, "��δ����");
		mListView.setRefreshTime(time);
		SimpleDateFormat myFmt=new SimpleDateFormat("MM��dd�� HHʱmm��ss��");
		String t=myFmt.format(new java.util.Date());
		preferences.edit().putString(TIME, t).commit();
	}
	/**
	 * ����ˢ��
	 */
	@Override
	public void onRefresh() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try{
					//������ܻ�����߳�����
					// ����ˢ��(�ӵ�һҳ��ʼװ������)
					queryData(0, STATE_REFRESH);
				}
				catch(Exception e)
				{
					
				}
			}
		}, 2000);
	}
	/**
	 * ���ظ���
	 */
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// �������ظ���(������һҳ����)
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
	 * ������2014��12��29����ӵģ�Ŀ������ʵ��ÿ��item�ĵ����תʵ�����۲鿴����
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
	//����� ���������˵˵�ļ�����
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent.getId()==R.id.action_bar)
		{
			if(position==1)
			{
				mActionBar.setSelection(0);
				Fragment f=fm.findFragmentById(R.id.main_fragment);
				//ת�����˵˵һ��Fragment��
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
			//��ȡͼƬ�ĵ�ַ
			//for(int i=0;i<lunTans.get(position).getImageURL().size();i++)
			//Log.e("LunTans.ImageURl"+position+"=",lunTans.get(position).getImageURL().get(i));
			//user_textView����������ʾÿ��Item������
			TextView user_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_name);
			//��ʾÿ��Item�����ʱ��
			TextView time_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_time);
			//��ʾÿ��Item������
			TextView content_textView=(TextView)convertView.findViewById(R.id.list_item_luntang_content);
			//��ʾÿ��Item��ͼƬ
			ImageView img_ImageView=(ImageView)convertView.findViewById(R.id.list_item_luntang_ContentImg);
			//ͼƬ��ʾ
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
					//MyToast.show(getActivity(), "����"+v.getTag());
					Log.e("����-------------","����");
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
	private static final int STATE_REFRESH = 0;// ����ˢ��
	private static final int STATE_MORE = 1;// ���ظ���
	private int limit = 10;		// ÿҳ��������10��
	private int curPage = 0;
	/**
	 * ��ҳ��ȡ����
	 * @param page	ҳ��
	 * @param actionType	ListView�Ĳ������ͣ�����ˢ�¡��������ظ��ࣩ
	 */
	private void queryData(final int page, final int actionType){
		Log.i("bmob", "pageN:"+page+" limit:"+limit+" actionType:"+actionType);
		
		BmobQuery<LunTan> query = new BmobQuery<LunTan>();
		//query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK); 
		query.include("ToUser");
		query.order("-createdAt");
		query.setLimit(limit);			// ����ÿҳ����������
		query.setSkip(page*limit);		// �ӵڼ������ݿ�ʼ��
		query.findObjects(getActivity(), new FindListener<LunTan>() {
			
			@Override
			public void onSuccess(List<LunTan> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()>0){
					if(actionType == STATE_REFRESH){
						// ��������ˢ�²���ʱ������ǰҳ�ı������Ϊ0������bankCards��գ��������
						curPage = 0;
						lunTans.clear();
						//imageLoader.clearCache();��������������ȥ����
						//imageLoader.clearCache();
						//hash.clear();
						//meTalk_textViews.clear();
						//pinglun_textViews.clear();
						//seeAll_textViews.clear();
					}
					
					// �����β�ѯ��������ӵ�bankCards��
					for (int i=0;i<arg0.size();i++) {
						lunTans.add(arg0.get(i));
						//zan_textViews.add(textView);
					}
					
					// ������ÿ�μ��������ݺ󣬽���ǰҳ��+1������������ˢ�µ�onPullUpToRefresh�����оͲ���Ҫ����curPage��
					curPage++;
					MyToast.show(getActivity(),"��"+(page+1)+"ҳ���ݼ������");
				}else if(actionType == STATE_MORE){
					MyToast.show(getActivity(),"û�и���������");
				}else if(actionType == STATE_REFRESH){
					MyToast.show(getActivity(),"û������");
				}
				onLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				MyToast.show(getActivity(),"��ѯʧ��:"+arg1);
				onLoad();
			}
		});
	}
	

}
