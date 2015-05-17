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
	//说说上传的对应的用户，里面包含用户objectId和用户名
	MyUser user;
	//MyUser下有个ToLuntan的bmobrealtion，表示一对多的关系
	LunTan lunTan;
	View v;
	//返回键
	ImageView back_imageView;
	//结束键，上传说说
	TextView finish_textView;
	//写说说内容
	EditText writeLunTan;
	//图片ImageView
	ImageView imageShowView;
	//Fragment管理器
	FragmentManager fm;
	//上传进度条
	ProgressDialog progressDialog;
	//图片路径地址数组，从showImageFragment中来
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
		user.setNickname("林兴强");
		user.setPassword("123456");
		user.setObjectId("AjVX333G");*/
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("文件太大，请耐心等待");
    	progressDialog.setMessage("正在上传");
    	//设置进度的类型，利用的是progressDialog的public static final STYLE_HORIZONTAL
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	//设置进度能够表示的最大的进度
    	progressDialog.setMax(100);                                                                                           
		fm=getActivity().getFragmentManager();
		imageLoader =  new NativeImageLoader2(getActivity());
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = (View)inflater.from(getActivity()).inflate(R.layout.fragment_add_item_linxingqiang,container, false);
		user = MyUser.getMyUser();//静态方法
		Init();
		return v;
	}
	public void Init()
	{
		back_imageView = (ImageView) v.findViewById(R.id.back_add_item);
	    finish_textView = (TextView) v.findViewById(R.id.finish_add_item);
	    writeLunTan = (EditText) v.findViewById(R.id.writeLuntan);
	    writeLunTan.setText(cons.Content);
	    //后面添加的用来添加图片上传的
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
	    
	    //保存图片的地址
		//cons.list.add("camera_default");
		//dataList.add("camera_default");
		/*for(int i=0;i<cons.list.size();i++)
		{
			Log.e("ddddddddddd",cons.list.get(i));
		}*/
		//gridImageAdapter = new GridImageAdapter(getActivity(), cons.list);
		//gridView.setAdapter(gridImageAdapter);
		//实现上面方法的监听事件
		initListener();
	    back_imageView.setOnClickListener(this);
	    writeLunTan.addTextChangedListener(this);
	    finish_textView.setOnClickListener(this);
	}
	private void initListener() {
		//实现跳转到GridViewFragment图片库获取图片并将其展现出来
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
	//这里的作用是寻找除了“default”之外的图片
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
	 * 创建一条博文到LunTan表中,并关联到用户的信息中
	 * @param bankName 银行名称
	 * @param cardNumber 银行卡号
	 */
	private void saveLunTanInfo(String content, String imageURL){

	    if(TextUtils.isEmpty(user.getObjectId())){
	        MyToast.show(getActivity(), "当前用户的object为空");
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
	            Log.e("上传成功","上传文件成功:" + bmobFile.getFileUrl(getActivity()));
	        }

	        @Override
	        public void onProgress(Integer value) {
	            // TODO Auto-generated method stub
	            // 返回的上传进度（百分比）
	        }

	        @Override
	        public void onFailure(int code, String msg) {
	            // TODO Auto-generated method stub
	            Log.e("文件上传失败","上传文件失败：" + msg);
	        }
	    });*/
	    //File ff=new File("/mnt/sdcard/kugou/market/");
	    //使用的File进行转化，然后上传
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
			//这里用到的类是批量上传图片类
			String picPath = cons.ImageURL;
			final BmobFile bmobFile = new BmobFile(new File(picPath));
			bmobFile.uploadblock(getActivity(), new UploadFileListener() {

			    @Override
			    public void onSuccess() {
			        // TODO Auto-generated method stub
			        //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
			        //bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
			        //toast("上传文件成功:" + bmobFile.getFileUrl(getActivity());
			    	cons.ImageURL = cons.camera_default;
			    	lunTan.setImageURL(bmobFile.getFileUrl(getActivity()));
			    	//lunTan.addAll("ImageURL", (ArrayList<String>)urls);
		        	lunTan.save(getActivity(),new SaveListener() {
		    			
		    			@Override
		    			public void onSuccess() {
		    				// TODO Auto-generated method stub
		    				//这里的作用是在User表也关联到对应的lunTan，实现双方的联系
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
			        // 返回的上传进度（百分比）
			    	progressDialog.show();
		        	Message msg=handler.obtainMessage();
		        	msg.what=0x123;
		        	msg.arg1=value;
		        	msg.sendToTarget();
			    }

			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			        //toast("上传文件失败：" + msg);
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
	    /*//上传文件
	    Bmob.uploadBatch(getActivity(), filePaths, new UploadBatchListener() {

	        @Override
	        public void onSuccess(List<BmobFile> files,List<String> urls) {
	            // TODO Auto-generated method stub
	            //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
	            //2、urls-上传文件的服务器地址
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
	            Log.e("onError", "错误码"+statuscode +",错误描述："+errormsg);
	        }

	        @Override
	        public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
	            // TODO Auto-generated method stub
	            //1、curIndex--表示当前第几个文件正在上传
	            //2、curPercent--表示当前上传文件的进度值（百分比）
	            //3、total--表示总的上传文件数
	            //4、totalPercent--表示总的上传进度（百分比）
	        	
	        	
	        	
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
	        MyToast.show(getActivity(), "内容识别不存在");
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
