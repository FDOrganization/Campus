package lxq.example.luntang;

import java.io.File;
import java.util.ArrayList;

import lxq.example.table.LunTan;
import lxq.example.table.MyUser;
import me.maxwin.Util.MyToast;
import me.maxwin.Util.NativeImageLoader2;
import me.maxwin.Util.cons;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.fydia.campus.R;

public class AddItemFragment extends Fragment implements OnClickListener,TextWatcher {
	//˵˵�ϴ��Ķ�Ӧ���û�����������û�objectId���û���
	MyUser user;
	//MyUser���и�ToLuntan��bmobrealtion����ʾһ�Զ�Ĺ�ϵ
	LunTan lunTan;
	View v;
	//���ؼ�
	ImageView back_imageView;
	//���������ϴ�˵˵
	TextView finish_textView;
	//д˵˵����
	EditText writeLunTan;
	//ͼƬImageView
	ImageView imageShowView;
	//Fragment������
	FragmentManager fm;
	//�ϴ�������
	ProgressDialog progressDialog;
	//ͼƬ·����ַ���飬��showImageFragment����
	String imagePath;
	NativeImageLoader2 imageLoader;
	//ArrayList<String> list=new ArrayList<String>();
	//private GridView gridView;
	//private ArrayList<String> dataList = new ArrayList<String>();
	//public GridImageAdapter gridImageAdapter;
	public AddItemFragment() {
		// TODO Auto-generated constructor stub
	}
	Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what==0x123)
			{
				progressDialog.show();
				progressDialog.setProgress(msg.arg1);
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*user=new MyUser();
		user.setNickname("����ǿ");
		user.setPassword("123456");
		user.setObjectId("AjVX333G");*/
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("�ļ�̫�������ĵȴ�");
    	progressDialog.setMessage("�����ϴ�");
    	//���ý��ȵ����ͣ����õ���progressDialog��public static final STYLE_HORIZONTAL
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	//���ý����ܹ���ʾ�����Ľ���
    	progressDialog.setMax(100);                                                                                           
		fm=getActivity().getFragmentManager();
		imageLoader =  new NativeImageLoader2(getActivity());
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = (View)inflater.from(getActivity()).inflate(R.layout.fragment_add_item_linxingqiang,container, false);
		user = MyUser.getMyUser();//��̬����
		Init();
		return v;
	}
	public void Init()
	{
		back_imageView = (ImageView) v.findViewById(R.id.back_add_item);
	    finish_textView = (TextView) v.findViewById(R.id.finish_add_item);
	    writeLunTan = (EditText) v.findViewById(R.id.writeLuntan);
	    writeLunTan.setText(cons.Content);
	    //������ӵ��������ͼƬ�ϴ���
	    imageShowView = (ImageView) v.findViewById(R.id.fragment_add_item_showImgs);
	    if(!cons.ImageURL.equals(cons.camera_default))
		{
	    	imageLoader.DisplayImage(cons.ImageURL, imageShowView);
			/*Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(cons.ImageURL,  new NativeImageCallBack() {  
	            
	            @Override  
	            public void onImageLoader(Bitmap bitmap, String path) {  
	               // ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);  
	                if(bitmap != null && imageShowView != null){  
	                	imageShowView.setImageBitmap(bitmap);  
	                }  
	            }  
	        });*/
		}
	    /*Bundle bundle=getArguments();
		if(bundle.getStringArrayList(cons.ImagePath)==null)
		{
			//dataList=bundle.getStringArrayList(cons.ImagePath);
		}
		//gridImageAdapter.notifyDataSetChanged();
*/		
		//dataList=cons.list;
		/*for(int i=0;i<dataList.size();i++)
		{
			Log.e("dataList------",dataList.get(i));
		}*/
	    
	    //����ͼƬ�ĵ�ַ
		//cons.list.add("camera_default");
		//dataList.add("camera_default");
		/*for(int i=0;i<cons.list.size();i++)
		{
			Log.e("ddddddddddd",cons.list.get(i));
		}*/
		//gridImageAdapter = new GridImageAdapter(getActivity(), cons.list);
		//gridView.setAdapter(gridImageAdapter);
		//ʵ�����淽���ļ����¼�
		initListener();
	    back_imageView.setOnClickListener(this);
	    writeLunTan.addTextChangedListener(this);
	    finish_textView.setOnClickListener(this);
	}
	private void initListener() {
		//ʵ����ת��GridViewFragmentͼƬ���ȡͼƬ������չ�ֳ���
		imageShowView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				// intent.putArrayListExtra("dataList", dataList);
				bundle.putString(cons.ImageKey,
						cons.ImageURL);
				Fragment ff=fm.findFragmentById(R.id.main_fragment);
				FragmentTransaction ft=fm.beginTransaction();
				GridViewFragment fragment=new GridViewFragment();
				fragment.setArguments(bundle);
				ft.add(R.id.main_fragment,fragment,cons.GridViewFragment).commit();
				ft.hide(ff);
			}
		});
	}
	//�����������Ѱ�ҳ��ˡ�default��֮���ͼƬ
	private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

		ArrayList<String> tDataList = new ArrayList<String>();

		for (String s : dataList) {
			if (!s.contains("default")) {
				tDataList.add(s);
			}
		}

		return tDataList;

	}
	/**
	 * ����һ�����ĵ�LunTan����,���������û�����Ϣ��
	 * @param bankName ��������
	 * @param cardNumber ���п���
	 */
	private void saveLunTanInfo(String content, String imageURL){

	    if(TextUtils.isEmpty(user.getObjectId())){
	        MyToast.show(getActivity(), "��ǰ�û���objectΪ��");
	        return;
	    }
	    lunTan=new LunTan();
	    lunTan.setContent(content);
	    //lunTan.addAll("ImageURL", imageUrls);
	    //lunTan.setImgURL(imgURL);
	    lunTan.setUser(user);
	    lunTan.setZan(0);
	    /*String picPath = imageUrls.get(0);
	    final BmobFile bmobFile = new BmobFile(new File(picPath));
	    bmobFile.uploadblock(getActivity(), new UploadFileListener() {

	        @Override
	        public void onSuccess() {
	            // TODO Auto-generated method stub
	            Log.e("�ϴ��ɹ�","�ϴ��ļ��ɹ�:" + bmobFile.getFileUrl(getActivity()));
	        }

	        @Override
	        public void onProgress(Integer value) {
	            // TODO Auto-generated method stub
	            // ���ص��ϴ����ȣ��ٷֱȣ�
	        }

	        @Override
	        public void onFailure(int code, String msg) {
	            // TODO Auto-generated method stub
	            Log.e("�ļ��ϴ�ʧ��","�ϴ��ļ�ʧ�ܣ�" + msg);
	        }
	    });*/
	    //File ff=new File("/mnt/sdcard/kugou/market/");
	    //ʹ�õ�File����ת����Ȼ���ϴ�
		/*String[] filePaths = new String[imageUrls.size()];
		if(imageUrls!=null && imageUrls.size()>0){
			final int len = imageUrls.size();
			for(int i=0;i<len;i++){
				File ff = new File(imageUrls.get(i));
				filePaths[i] = ff.getAbsolutePath();
			}
		}*/
		if(!cons.ImageURL.equals(cons.camera_default))
		{
			//�����õ������������ϴ�ͼƬ��
			String picPath = cons.ImageURL;
			final BmobFile bmobFile = new BmobFile(new File(picPath));
			bmobFile.uploadblock(getActivity(), new UploadFileListener() {

			    @Override
			    public void onSuccess() {
			        // TODO Auto-generated method stub
			        //bmobFile.getUrl()---���ص��ϴ��ļ��ĵ�ַ������������
			        //bmobFile.getFileUrl(context)--���ص��ϴ��ļ���������ַ����������
			        //toast("�ϴ��ļ��ɹ�:" + bmobFile.getFileUrl(getActivity());
			    	cons.ImageURL = cons.camera_default;
			    	lunTan.setImageURL(bmobFile.getFileUrl(getActivity()));
			    	//lunTan.addAll("ImageURL", (ArrayList<String>)urls);
		        	lunTan.save(getActivity(),new SaveListener() {
		    			
		    			@Override
		    			public void onSuccess() {
		    				// TODO Auto-generated method stub
		    				//�������������User��Ҳ��������Ӧ��lunTan��ʵ��˫������ϵ
		    				addLunTanToUser();
		    			}
		    			
		    			@Override
		    			public void onFailure(int arg0, String arg1) {
		    				// TODO Auto-generated method stub
		    				//MyToast.show(getActivity(), arg1);
		    				Log.e("onFailure",arg0+"="+arg1);
		    			}
		    		});
			    }

			    @Override
			    public void onProgress(Integer value) {
			        // TODO Auto-generated method stub
			        // ���ص��ϴ����ȣ��ٷֱȣ�
			    	progressDialog.show();
		        	Message msg=handler.obtainMessage();
		        	msg.what=0x123;
		        	msg.arg1=value;
		        	msg.sendToTarget();
			    }

			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			        //toast("�ϴ��ļ�ʧ�ܣ�" + msg);
			    }
			});
		}
		else
		{
			lunTan.save(getActivity(),new SaveListener() {
    			
    			@Override
    			public void onSuccess() {
    				// TODO Auto-generated method stub
    				MyToast.show(getActivity(), "removeFragment");
    				addLunTanToUser();
    			}
    			
    			@Override
    			public void onFailure(int arg0, String arg1) {
    				// TODO Auto-generated method stub
    				Log.e("onFailure",arg0+"="+arg1);
    			}
    		});
		}
	    /*//�ϴ��ļ�
	    Bmob.uploadBatch(getActivity(), filePaths, new UploadBatchListener() {

	        @Override
	        public void onSuccess(List<BmobFile> files,List<String> urls) {
	            // TODO Auto-generated method stub
	            //1��files-�ϴ���ɺ��BmobFile���ϣ���Ϊ�˷����Ҷ����ϴ�������ݽ��в�������������Խ����ļ����浽����
	            //2��urls-�ϴ��ļ��ķ�������ַ
	        	for(int i=0;i<urls.size();i++)
	        	{
	        		Log.e("urls="+i,urls.get(i));
	        	}
	        	//lunTan.setImageURL((ArrayList<String>)urls);
	        	lunTan.addAll("ImageURL", (ArrayList<String>)urls);
	        	
	        	lunTan.save(getActivity(),new SaveListener() {
	    			
	    			@Override
	    			public void onSuccess() {
	    				// TODO Auto-generated method stub
	    				MyToast.show(getActivity(), "removeFragment");
	    	            //addCardToUser();
	    				addLunTanToUser();
	    			}
	    			
	    			@Override
	    			public void onFailure(int arg0, String arg1) {
	    				// TODO Auto-generated method stub
	    				//MyToast.show(getActivity(), arg1);
	    				Log.e("onFailure",arg0+"="+arg1);
	    			}
	    		});
	        }

	        @Override
	        public void onError(int statuscode, String errormsg) {
	            // TODO Auto-generated method stub
	            Log.e("onError", "������"+statuscode +",����������"+errormsg);
	        }

	        @Override
	        public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
	            // TODO Auto-generated method stub
	            //1��curIndex--��ʾ��ǰ�ڼ����ļ������ϴ�
	            //2��curPercent--��ʾ��ǰ�ϴ��ļ��Ľ���ֵ���ٷֱȣ�
	            //3��total--��ʾ�ܵ��ϴ��ļ���
	            //4��totalPercent--��ʾ�ܵ��ϴ����ȣ��ٷֱȣ�
	        	
	        	
	        	
	        	progressDialog.show();
	        	Message msg=handler.obtainMessage();
	        	msg.what=0x123;
	        	msg.arg1=curPercent;
	        	msg.sendToTarget();
	        }
	    });*/
	    
	}
	private void addLunTanToUser(){
	    if(TextUtils.isEmpty(user.getObjectId()) || 
	            TextUtils.isEmpty(lunTan.getObjectId())){
	        MyToast.show(getActivity(), "����ʶ�𲻴���");
	        return;
	    }

	    BmobRelation contents = new BmobRelation();
	    contents.add(lunTan);
	    user.setToLunTan(contents);
	    user.update(getActivity(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//cons.list.clear();
				cons.Content="";
				progressDialog.dismiss();
				Fragment fragment=fm.findFragmentById(R.id.main_fragment);
				Fragment mainFragment=fm.findFragmentByTag(cons.MainFragment);
				MyToast.show(getActivity(), "removeFragment");
				FragmentTransaction ft=fm.beginTransaction();
				ft.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
				ft.remove(fragment).commit();
				ft.show(mainFragment);
				

			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.finish_add_item)
		{
			progressDialog.show();
			Log.e("onClick","finish");
			saveLunTanInfo(writeLunTan.getText().toString(), cons.ImageURL);
			cons.Content="";
		}
		else if(v.getId()==R.id.back_add_item)
		{
			cons.ImageURL=cons.camera_default;
			//cons.list.clear();
			cons.Content="";
			Fragment fragment=fm.findFragmentById(R.id.main_fragment);
			Fragment mainFragment = fm.findFragmentByTag(cons.MainFragment);
			MyToast.show(getActivity(), "removeFragment");	
			FragmentTransaction ft=fm.beginTransaction();
			ft.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
			ft.remove(fragment).show(mainFragment).commit();
			
		}
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
		
	}@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(writeLunTan.getText().toString()))
	    {
	    	finish_textView.setEnabled(false);
	    }
		else
		{
			cons.Content=writeLunTan.getText().toString();
		}
		
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("onStart","0000");
		if(!cons.ImageURL.equals(cons.camera_default))
		{
	    	imageLoader.DisplayImage(cons.ImageURL, imageShowView);
			/*Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(cons.ImageURL,  new NativeImageCallBack() {  
	            
	            @Override  
	            public void onImageLoader(Bitmap bitmap, String path) {  
	               // ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);  
	                if(bitmap != null && imageShowView != null){  
	                	imageShowView.setImageBitmap(bitmap);  
	                }  
	            }  
	        });*/
		}
		
		//gridImageAdapter.notifyDataSetChanged();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if(!cons.ImageURL.equals(cons.camera_default))
		{
	    	imageLoader.DisplayImage(cons.ImageURL, imageShowView);
			/*Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(cons.ImageURL,  new NativeImageCallBack() {  
	            
	            @Override  
	            public void onImageLoader(Bitmap bitmap, String path) {  
	               // ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);  
	                if(bitmap != null && imageShowView != null){  
	                	imageShowView.setImageBitmap(bitmap);  
	                }  
	            }  
	        });*/
		}
		//gridImageAdapter.notifyDataSetChanged();
		super.onResume();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("onPause","0000");
		//gridImageAdapter.notifyDataSetChanged();
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
		Fragment f=fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft1=fm.beginTransaction();
		ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
		//ft1.show(f).commit();
		Log.e("Addfragment_onStop=",f.toString());
		//gridImageAdapter.notifyDataSetChanged();
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Fragment f=fm.findFragmentById(R.id.main_fragment);
		FragmentTransaction ft1=fm.beginTransaction();
		ft1.setCustomAnimations(R.anim.animation_view, R.anim.animation_view_exit);
		Log.e("fragment_onResume=",f.toString());
	}
}
