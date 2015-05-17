package lxq.example.luntang;

import java.util.List;

import lxq.example.luntang.ChildAdapter.OnCheckListListener;
import me.maxwin.Util.cons;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.fydia.campus.R;

/*
 * 这个ShowImageFragment的作用是浏览图片，以及为博文添加图片
 * */
public class ShowImageFragment extends Fragment implements OnItemClickListener{
	//返回键
	private TextView back_textView;
	//结束键
	private TextView finish_textView;
	private GridView mGridView;  
	/*
	 * Bundle bundle=getArguments();
	 * list=bundle.getStringArrayList("data");
	 * 作用是从GridViewFragment获得对应对应文件夹下的图片文件数组（包括路径）
	 * */
    private List<String> list;  
    private ChildAdapter adapter;
    private View v;
    private FragmentManager fm;
    Handler handler = new Handler()
    {
    	public void handleMessage(android.os.Message msg) 
    	{
    		if(msg.what == 0x123)
    		{
    			cons.ImageURL=list.get(msg.arg1);
    			/*ArrayList<String> list1=new ArrayList<String>();
				for(int i=0;i<adapter.getSelectItems().size();i++)
				{
					//Log.e("path"+i,list.get(adapter.getSelectItems().get(i)));
					//表示被点击的数据中所对应的图片文件
					list1.add(list.get(adapter.getSelectItems().get(i)));
				}*/
				
				Fragment f=fm.findFragmentById(R.id.main_fragment);
				FragmentTransaction ft1=fm.beginTransaction();
				/*Bundle bundle=new Bundle();
				bundle.putStringArrayList(cons.ImagePath, list1);*/
				//cons.list=list1;
				/*for(int j=0;j<cons.list.size();j++)
				{
					if(cons.list.get(j).contains("default"))
					cons.list.remove(j);
				}
				//cons.list.remove(cons.list.size()-1);
				cons.list.clear();
				for(int j=0;j<list1.size();j++)
				{
					cons.list.add(list1.get(j));
				}
				cons.list.add("camera_default");
				for(int i=0;i<cons.list.size();i++)
				{
					Log.e("path-onShowImageFragment"+i,cons.list.get(i));
					
				}*/
				Fragment gridViewFragment = fm.findFragmentByTag(cons.GridViewFragment);
				Fragment addItemfragment=fm.findFragmentByTag(cons.AddItemFragment);
				//addItemfragment.setArguments(bundle);
				ft1.remove(f).remove(gridViewFragment).show(addItemfragment).commit();
				addItemfragment.onResume();
				addItemfragment.onStart();
    		}
    		
    		
    	};
    };
    /*
     * 39行要修改
     * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fm=getActivity().getFragmentManager();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(v==null)
		{
			v=inflater.from(getActivity()).inflate(R.layout.fragment_show_image_linxingqiang, container, false);
		}
		mGridView = (GridView)v.findViewById(R.id.fragment_show_image_child_grid); 
		back_textView=(TextView)v.findViewById(R.id.fragment_show_image_back);
		finish_textView=(TextView)v.findViewById(R.id.fragment_show_image_finish);
		back_textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment f = fm.findFragmentById(R.id.main_fragment);
				Fragment gridFragment = fm.findFragmentByTag(cons.GridViewFragment);
				FragmentTransaction ft1 = fm.beginTransaction();
				ft1.remove(f).show(gridFragment).commit();
				//ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
				//onStop();
			}
		});
		finish_textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)  {
				adapter.notifyDataSetChanged();
				// TODO Auto-generated method stub
				/*ArrayList<String> list1=new ArrayList<String>();
				for(int i=0;i<adapter.getSelectItems().size();i++)
				{
					//Log.e("path"+i,list.get(adapter.getSelectItems().get(i)));
					//表示被点击的数据中所对应的图片文件
					list1.add(list.get(adapter.getSelectItems().get(i)));
				}
				
				Fragment f=fm.findFragmentById(R.id.main_fragment);
				FragmentTransaction ft1=fm.beginTransaction();
				Bundle bundle=new Bundle();
				bundle.putStringArrayList(cons.ImagePath, list1);
				//cons.list=list1;
				for(int j=0;j<cons.list.size();j++)
				{
					if(cons.list.get(j).contains("default"))
					cons.list.remove(j);
				}
				//cons.list.remove(cons.list.size()-1);
				cons.list.clear();
				for(int j=0;j<list1.size();j++)
				{
					cons.list.add(list1.get(j));
				}
				cons.list.add("camera_default");
				for(int i=0;i<cons.list.size();i++)
				{
					Log.e("path-onShowImageFragment"+i,cons.list.get(i));
					
				}
				Fragment gridViewFragment=fm.findFragmentByTag(cons.GridViewFragment);
				Fragment addItemfragment=fm.findFragmentByTag(cons.AddItemFragment);
				//addItemfragment.setArguments(bundle);
				ft1.remove(f).remove(gridViewFragment).commit();
				addItemfragment.onResume();
				ft1.show(addItemfragment);*/
				//
				//AddItemFragment.gridImageAdapter.notifyDataSetChanged();
			}
		});
		/*list = getIntent().getStringArrayListExtra("data");  */
        Bundle bundle = getArguments();
        list = bundle.getStringArrayList(cons.ImageChildList);
        adapter = new ChildAdapter(getActivity(), list, mGridView);  
        adapter.setOnCheckListListener(new OnCheckListListener() {
			
			@Override
			public void OnCheckList(int selectPosition) {
				// TODO Auto-generated method stub
				Message msg=handler.obtainMessage();
				msg.arg1=selectPosition;
				msg.what=0x123;
				msg.sendToTarget();
			}
		});
        mGridView.setAdapter(adapter);
		return v;
	}
	/*@Override  
    public void onBackPressed() {  
        Toast.makeText(this, "选中 " + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();  
        super.onBackPressed();  
    }  */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//adapter.getSelectItems()
		/*if(adapter.getSelectItems().size()>1)
        {
        	for(int i=0;i<adapter.getSelectItems().size();i++)
        	{
        		((CheckBox)mGridView.getChildAt(i)).setChecked(false);
        	}
        	mSelectMap.clear();
        	
        }*/
		// TODO Auto-generated method stub
		/*if(parent.getId()==R.id.fragment_show_image_child_grid)
		{
			ArrayList<String> list1=new ArrayList<String>();
			for(int i=0;i<adapter.getSelectItems().size();i++)
			{
				//Log.e("path"+i,list.get(adapter.getSelectItems().get(i)));
				list1.add(list.get(adapter.getSelectItems().get(i)));
			}
			
			Fragment f=fm.findFragmentById(R.id.main_fragment);
			FragmentTransaction ft1=fm.beginTransaction();
			Bundle bundle=new Bundle();
			bundle.putStringArrayList(cons.ImagePath, list1);
			//cons.list=list1;
			for(int j=0;j<cons.list.size();j++)
			{
				if(cons.list.get(j).contains("default"))
				cons.list.remove(j);
			}
			//cons.list.remove(cons.list.size()-1);
			for(int j=0;j<list1.size();j++)
			{
				cons.list.add(list1.get(j));
			}
			cons.list.add("camera_default");
			for(int i=0;i<cons.list.size();i++)
			{
				Log.e("path----ShowImageFragment"+i,cons.list.get(i));
				
			}
			Fragment gridViewFragment=fm.findFragmentByTag(cons.GridViewFragment);
			Fragment addItemfragment=fm.findFragmentByTag(cons.AddItemFragment);
			//addItemfragment.setArguments(bundle);
			ft1.remove(f).remove(gridViewFragment).commit();
			ft1.show(addItemfragment);
			//
			
		}*/
		
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		/*Fragment f = fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft1 = fm.beginTransaction();
		ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
		ft1.show(f).commit();*/
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}

