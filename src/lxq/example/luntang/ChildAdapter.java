package lxq.example.luntang;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lxq.example.luntang.MyImageView.OnMeasureListener;
import me.maxwin.Util.NativeImageLoader2;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;

import com.fydia.campus.R;
  
public class ChildAdapter extends BaseAdapter {  
    private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象  
    OnCheckListListener onCheckListListener;
    //ImageLoader imageLoader;
    /** 
     * 用来存储图片的选中情况 
     */  
    private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();  
    private GridView mGridView;  
    private List<String> list;  
    protected LayoutInflater mInflater; 
    private NativeImageLoader2 imageLoader;
    public ChildAdapter(Context context, List<String> list, GridView mGridView) {  
        this.list = list;  
        this.mGridView = mGridView;  
        mInflater = LayoutInflater.from(context);  
        if(imageLoader==null)
        {
        	imageLoader=new NativeImageLoader2(context);
        }
        //if(ImageLoader==null)
        //{}
    }  
      
    @Override  
    public int getCount() {  
        return list.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return list.get(position);  
    }  
  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
      
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        final ViewHolder viewHolder;  
        String path = list.get(position);  
        if(convertView == null)
        {  
            convertView = mInflater.inflate(R.layout.fragment_grid_child_item_linxingqiang, null);  
            viewHolder = new ViewHolder();  
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);  
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);  
              
            //用来监听ImageView的宽和高  
            viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {  
                  
                @Override  
                public void onMeasureSize(int width, int height) {  
                    mPoint.set(width, height);  
                }  
            });  
              
            convertView.setTag(viewHolder);  
        }else{  
            viewHolder = (ViewHolder) convertView.getTag();  
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);  
        }  
        viewHolder.mImageView.setTag(path);  
        viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
              
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
                //如果是未选中的CheckBox,则添加动画  
                if(!mSelectMap.containsKey(position) || !mSelectMap.get(position)){  
                    addAnimation(viewHolder.mCheckBox);  
                }  
                if(isChecked)
                {
                	mSelectMap.put(position, isChecked);
                	onCheckListListener.OnCheckList(position);
                	  
                }
            }  
        });  
          
        viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);  
        //ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
        //if(mImageView!=null)
        imageLoader.DisplayImage(path,viewHolder.mImageView);
        //else
        	//Log.e("mImageView=null","..................");
        //利用NativeImageLoader类加载本地图片  
        /*Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {  
              
            @Override  
            public void onImageLoader(Bitmap bitmap, String path) {  
                ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);  
                if(bitmap != null && mImageView != null){  
                    mImageView.setImageBitmap(bitmap);  
                }  
            }  
        }); */ 
          
       /* if(bitmap != null){  
            viewHolder.mImageView.setImageBitmap(bitmap);  
        }else{  
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);  
        } */ 
          
        return convertView;  
    }  
      
    /** 
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画  
     * @param view 
     */  
    private void addAnimation(View view){  
        float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};  
        AnimatorSet set = new AnimatorSet();  
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),   
                ObjectAnimator.ofFloat(view, "scaleY", vaules));  
                set.setDuration(150);  
        set.start();  
    }  
      
      
    /** 
     * 获取选中的Item的position 
     * @return 
     */  
    public List<Integer> getSelectItems(){  
        List<Integer> list = new ArrayList<Integer>();  
        for(Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){  
            Map.Entry<Integer, Boolean> entry = it.next();  
            if(entry.getValue()){  
                list.add(entry.getKey());  
            }  
        }  
          
        return list;  
    }  
      
  //表示图片被选择打钩的接口
    public interface OnCheckListListener
    {
    	public void OnCheckList(int selectPosition);
    }
    public void setOnCheckListListener(OnCheckListListener on)
    {
    	onCheckListListener=on;
    }
    public static class ViewHolder{  
        public MyImageView mImageView;  
        public CheckBox mCheckBox;  
    }  
    
  
  
}  
