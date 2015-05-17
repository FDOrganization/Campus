package lxq.example.luntang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.maxwin.Util.ImageBean;
import me.maxwin.Util.cons;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.fydia.campus.R;

/**
 * 
 * @author linxingqianglai
 * 此Fragment用来显示搜索到图片
 * ，并且用图片数组地址第一个作为GridViewFragment中显示的图片
 *
 */
public class GridViewFragment extends Fragment {
	private View v;
	private TextView back;
	//mGroup用来表示一个图片组下面的各个图片地址
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>(); 
	//存放在每组图片地址数组的有关信息，包括首张图片地址，以及图片文件名
    private List<ImageBean> list = new ArrayList<ImageBean>();  
    private final static int SCAN_OK = 1;  
    private ProgressDialog mProgressDialog;  
    private GroupAdapter adapter;  
    private GridView mGroupGridView;  
    private FragmentManager fm;
    private Handler mHandler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            switch (msg.what) {  
            case SCAN_OK:  
                //关闭进度条  
                mProgressDialog.dismiss();  
                adapter = new GroupAdapter(getActivity(), list = subGroupOfImage(mGruopMap), mGroupGridView);  
                mGroupGridView.setAdapter(adapter); 
                break;  
            }  
        }  
          
    };  
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
			v=(View)inflater.from(getActivity()).inflate(R.layout.fragment_gridview_linxignqiang, container, false);
		}
		
		mGroupGridView = (GridView)v.findViewById(R.id.fragment_gridview_grid);
		back=(TextView)v.findViewById(R.id.fragment_gridview_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment=fm.findFragmentByTag(cons.GridViewFragment);
				Fragment addItemfragment=fm.findFragmentByTag(cons.AddItemFragment);
				
				//addItemfragment.setArguments(bundle);
				//AddItemFragment addItem=new AddItemFragment();
				FragmentTransaction ft=fm.beginTransaction();
				ft.remove(fragment).commit();
				ft.show(addItemfragment);
			}
		});
		getImages();  
        
        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
                List<String> childList = mGruopMap.get(list.get(position).getFolderName());  
                  
               /* Intent mIntent = new Intent(MainActivity.this, ShowImageActivity.class);  
                mIntent.putStringArrayListExtra("data", (ArrayList<String>)childList);  
                startActivity(mIntent); */ 
                Fragment fragment=fm.findFragmentById(R.id.main_fragment);
                FragmentTransaction ft=fm.beginTransaction();
                ShowImageFragment frag=new ShowImageFragment();
                Bundle bundle=new Bundle();
                bundle.putStringArrayList(cons.ImageChildList, (ArrayList<String>)childList);
                frag.setArguments(bundle);
                ft.add(R.id.main_fragment,frag,cons.ShowImageFragment).commit();
                ft.hide(fragment);
            }  
        });
		return v;
	}
	/** 
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 
     */  
    private void getImages() {  
        //显示进度条  
        mProgressDialog = ProgressDialog.show(getActivity(), null, "正在加载...");  
          
        new Thread(new Runnable() {  
              
            @Override  
            public void run() {  
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                ContentResolver mContentResolver = getActivity().getContentResolver();  
                //只查询jpeg和png的图片  
                Cursor mCursor = mContentResolver.query(mImageUri, null,  
                        MediaStore.Images.Media.MIME_TYPE + "=? or "  
                                + MediaStore.Images.Media.MIME_TYPE + "=?",  
                        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DEFAULT_SORT_ORDER);  
                  
                if(mCursor == null){  
                    return;  
                }  
                  
                while (mCursor.moveToNext()) {  
                    //获取图片的路径  
                    String path = mCursor.getString(mCursor  
                            .getColumnIndex(MediaStore.Images.Media.DATA));  
                      
                    //获取该图片的父路径名  
                    String parentName = new File(path).getParentFile().getName();  
                    //根据父路径名将图片放入到mGruopMap中  
                    if (!mGruopMap.containsKey(parentName)) {  
                        List<String> chileList = new ArrayList<String>();  
                        chileList.add(path);  
                        mGruopMap.put(parentName, chileList);  
                    } else {  
                        mGruopMap.get(parentName).add(path);  
                    }  
                }  
                  
                //通知Handler扫描图片完成  
                mHandler.sendEmptyMessage(SCAN_OK); 
                //
                mCursor.close();  
            }  
        }).start();  
          
    }  
      
      
    /** 
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 
     * 所以需要遍历HashMap将数据组装成List 
     *  
     * @param mGruopMap 
     * @return 
     */  
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap){  
        if(mGruopMap.size() == 0){  
            return null;  
        }  
        List<ImageBean> list = new ArrayList<ImageBean>();  
        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();  
        while (it.hasNext()) {  
            Map.Entry<String, List<String>> entry = it.next();  
            ImageBean mImageBean = new ImageBean();  
            String key = entry.getKey();  
            List<String> value = entry.getValue();  
              
            mImageBean.setFolderName(key);  
            mImageBean.setImageCounts(value.size());  
            mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片  
            list.add(mImageBean);  
        }  
          
        return list;  
          
    }  
  
 
}
