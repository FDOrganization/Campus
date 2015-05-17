package jx.push.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jx.push.DetailActivity;
import jx.push.NoticeDB;
import jx.push.NoticeList;
import jx.push.listview.XListView;
import jx.push.listview.XListView.IXListViewListener;
import jx.push.listview.XListViewAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fydia.campus.R;

public class NoticeFragment extends Fragment implements IXListViewListener{
	
	private XListView xListView;
	private XListViewAdapter xAdapter;
	private NoticeDB noticeDB;
	List<NoticeList> noticeList = new ArrayList<NoticeList>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		noticeDB = new NoticeDB(getActivity(), "noticeDB", null, 1);

	}

	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.jx_fragment_notice_list, container, false);
	}



	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}



	private void initView() {
				
		
		if(isExist()){
			findAll(noticeList);
		}
		
		xAdapter = new XListViewAdapter(getActivity(), noticeList);
		xListView = (XListView) getActivity().findViewById(R.id.notice_xlistview);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		xListView.setAdapter(xAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NoticeList nl = noticeList.get(position-1);
				Intent mIntent = new Intent(getActivity(),DetailActivity.class);
				Bundle bundle = new Bundle();
                bundle.putString("title", nl.getTitle());
                bundle.putString("content", nl.getContent());
                bundle.putString("channel", nl.getChannel());
                bundle.putString("thu_imgUrl", nl.getThu_imgUrl());
                bundle.putString("imgUrl", nl.getImgUrl());
                bundle.putString("contentUrl", nl.getContentUrl());
                bundle.putString("source", nl.getSource());
                bundle.putString("updatedAt", nl.getUpdatedAt());
                mIntent.putExtras(bundle);
                startActivity(mIntent);
			}
			
		});
		
	}

	private static final int STATE_REFRESH = 0;// 下拉刷新
	private static final int STATE_MORE = 1;// 加载更多
	
	private int limit = 2;		// 每页的数据是10条
	private int curPage = 0;		// 当前页的编号，从0开始

	@Override
	public void onRefresh() {
		queryData(0,STATE_REFRESH);
	}

	@Override
	public void onLoadMore() {	
		queryData(curPage, STATE_MORE);
	}
	
	private void queryData(final int page,final int actionType) {
		// TODO Auto-generated method stub
		Log.i("bmob", "pageN:"+page+" limit:"+limit+" actionType:"+actionType);
		
		BmobQuery<NoticeList> query = new BmobQuery<NoticeList>();
		query.setLimit(limit);
		query.setSkip(page*limit);
		query.order("-updatedAt");
		//设置bmob缓冲
		//query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.findObjects(getActivity(), new FindListener<NoticeList>() {
			
			@Override
			public void onSuccess(List<NoticeList> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()>0){

					if(actionType == STATE_REFRESH){
						// 当是下拉刷新操作时，将当前页的编号重置为0，并把noticeList清空，重新添加
						curPage = 0;
						noticeList.clear();
					}
					//将本次查询的数据添加到noticeList;
					for(NoticeList nl : arg0){
						noticeList.add(nl);
					}
					//页码加一
					++curPage;
					xAdapter.notifyDataSetChanged();
					
					
				}
				else Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
				if(!xListView.ismEnablePullLoad()) xListView.setPullLoadEnable(true);
				onLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				onLoad();
			}
		});
	}
	
	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		Log.d("time", formatter.format(curDate));
		xListView.setRefreshTime(formatter.format(curDate));
	}



    
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = noticeDB.getWritableDatabase();
		super.onDestroy();
		//保存前，删除以前所存数据
		db.delete("notice", null, null);
		for(NoticeList nl : noticeList){
			if(db.isOpen()){
				ContentValues values = new ContentValues();
				values.put("channel", nl.getChannel());
				values.put("title", nl.getTitle());
				values.put("content", nl.getContent());
				values.put("contentUrl", nl.getContentUrl());
				values.put("thu_imgUrl", nl.getThu_imgUrl());
				values.put("imgUrl", nl.getImgUrl());
				values.put("source", nl.getSource());
				values.put("updatedAt", nl.getUpdatedAt());
				db.insert("notice", null, values);
			}
		}
		db.close();
	}
	/**
	 * 找到数据库所存的数据，并添加到noticeList
	 * @param noticeList
	 */
	public void findAll(List<NoticeList> noticeList)
    {
    	SQLiteDatabase db = noticeDB.getWritableDatabase();
    	if(db.isOpen()){
    		
    		Cursor cursor = db.query("notice", null, null, null, null, null, null);
    		while(cursor.moveToNext())
    		{
    			String channel = cursor.getString(cursor.getColumnIndex("channel"));
    			String title = cursor.getString(cursor.getColumnIndex("title"));
    			String content = cursor.getString(cursor.getColumnIndex("content"));
    			String contentUrl = cursor.getString(cursor.getColumnIndex("contentUrl"));
    			String thu_imgUrl = cursor.getString(cursor.getColumnIndex("thu_imgUrl"));
    			String imgUrl     = cursor.getString(cursor.getColumnIndex("imgUrl"));
    			String source = cursor.getString(cursor.getColumnIndex("source"));
    			String time = cursor.getString(cursor.getColumnIndex("updatedAt"));
   			
    			NoticeList nl = new NoticeList(channel, title, content, contentUrl, thu_imgUrl, imgUrl, source);
    			nl.setMyUpdatedAt(time);
    			noticeList.add(nl);

    		}
    		cursor.close();
    		db.close();
    	}
 
    }
	/**
	 * 判断数据库是否存在
	 */
	public boolean isExist()
    {
    	SQLiteDatabase db = noticeDB.getWritableDatabase();
    	if(db.isOpen()){
    		Cursor cursor = db.query("notice", null, null, null, null, null, null);
    		if(cursor.moveToFirst()){
    			return true;
    		}
    		cursor.close();
    		db.close();
    	}
    	return false;
    }
	
	
}
