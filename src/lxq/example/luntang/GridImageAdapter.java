/*package lxq.example.luntang;

import java.util.ArrayList;

import com.example.luntang.R;

import me.maxwin.Util.NativeImageLoader;
import me.maxwin.Util.cons;
import me.maxwin.Util.NativeImageLoader.NativeImageCallBack;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridImageAdapter extends BaseAdapter {
	//上下文环境
	private Context mContext;
	//private ArrayList<String> dataList;
	//窗口展示的像素
	private DisplayMetrics dm;
	private Point mPoint = new Point(0, 0);
	public GridImageAdapter(Context c, ArrayList<String> dataList) {

		mContext = c;
		//this.dataList=cons.list;
		//this.dataList = dataList;
		dm = new DisplayMetrics();
		mPoint.x=dm.widthPixels;
		mPoint.y=dm.heightPixels;
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

	}

	@Override
	public int getCount() 
	{
		return cons.list.size();//dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return cons.list;//dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		for(int i=0;i<cons.list.size();i++)
		{
			Log.e("dataListcons.list.get("+i+")=",cons.list.get(i));
		}
		String path;
		if (cons.list != null && position<cons.list.size() )
			path = cons.list.get(position);
		else
			path = "camera_default";
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_gridview_image_item_linxingqiang, null);  
            viewHolder.mImageView=(ImageView)convertView.findViewById(R.id.fragment_gridview_image_item_imageView);
            mPoint.x=viewHolder.mImageView.getWidth();
    		mPoint.y=viewHolder.mImageView.getHeight();
    		convertView.setTag(viewHolder); 
    		viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		} 
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();  
	        viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		mPoint.x = viewHolder.mImageView.getWidth();
		mPoint.y = viewHolder.mImageView.getHeight();
		viewHolder.mImageView.setTag(path);
		if (cons.list.get(position).contains("default"))
		{
			Log.e("path.contains(\"default\")","------------------");
			viewHolder.mImageView.setImageResource(R.drawable.camera_default);
		}		
		else
		{
			//利用NativeImageLoader类加载本地图片  
	        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(cons.list.get(position), mPoint, new NativeImageCallBack() {  
	              
	            @Override  
	            public void onImageLoader(Bitmap bitmap, String path) {  
	               // ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);  
	                if(bitmap != null && viewHolder.mImageView != null){  
	                    viewHolder.mImageView.setImageBitmap(bitmap);  
	                }  
	            }  
	        });  
	        
	        if(bitmap != null)
	        {  
	            //viewHolder.mImageView.setImageBitmap(bitmap);  
	        }else{  
	            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);  
	        } ImageManager2.from(mContext).displayImage(imageView, path,R.drawable.camera_default,100,100);
		}
		return convertView;
	}
	public static class ViewHolder{  
        public ImageView mImageView;  
    } 
	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

}
*/