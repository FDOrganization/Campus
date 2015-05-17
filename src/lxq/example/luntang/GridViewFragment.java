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
 * ��Fragment������ʾ������ͼƬ
 * ��������ͼƬ�����ַ��һ����ΪGridViewFragment����ʾ��ͼƬ
 *
 */
public class GridViewFragment extends Fragment {
	private View v;
	private TextView back;
	//mGroup������ʾһ��ͼƬ������ĸ���ͼƬ��ַ
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>(); 
	//�����ÿ��ͼƬ��ַ������й���Ϣ����������ͼƬ��ַ���Լ�ͼƬ�ļ���
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
                //�رս�����  
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
     * ����ContentProviderɨ���ֻ��е�ͼƬ���˷��������������߳��� 
     */  
    private void getImages() {  
        //��ʾ������  
        mProgressDialog = ProgressDialog.show(getActivity(), null, "���ڼ���...");  
          
        new Thread(new Runnable() {  
              
            @Override  
            public void run() {  
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                ContentResolver mContentResolver = getActivity().getContentResolver();  
                //ֻ��ѯjpeg��png��ͼƬ  
                Cursor mCursor = mContentResolver.query(mImageUri, null,  
                        MediaStore.Images.Media.MIME_TYPE + "=? or "  
                                + MediaStore.Images.Media.MIME_TYPE + "=?",  
                        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DEFAULT_SORT_ORDER);  
                  
                if(mCursor == null){  
                    return;  
                }  
                  
                while (mCursor.moveToNext()) {  
                    //��ȡͼƬ��·��  
                    String path = mCursor.getString(mCursor  
                            .getColumnIndex(MediaStore.Images.Media.DATA));  
                      
                    //��ȡ��ͼƬ�ĸ�·����  
                    String parentName = new File(path).getParentFile().getName();  
                    //���ݸ�·������ͼƬ���뵽mGruopMap��  
                    if (!mGruopMap.containsKey(parentName)) {  
                        List<String> chileList = new ArrayList<String>();  
                        chileList.add(path);  
                        mGruopMap.put(parentName, chileList);  
                    } else {  
                        mGruopMap.get(parentName).add(path);  
                    }  
                }  
                  
                //֪ͨHandlerɨ��ͼƬ���  
                mHandler.sendEmptyMessage(SCAN_OK); 
                //
                mCursor.close();  
            }  
        }).start();  
          
    }  
      
      
    /** 
     * ��װ�������GridView������Դ����Ϊ����ɨ���ֻ���ʱ��ͼƬ��Ϣ����HashMap�� 
     * ������Ҫ����HashMap��������װ��List 
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
            mImageBean.setTopImagePath(value.get(0));//��ȡ����ĵ�һ��ͼƬ  
            list.add(mImageBean);  
        }  
          
        return list;  
          
    }  
  
 
}
