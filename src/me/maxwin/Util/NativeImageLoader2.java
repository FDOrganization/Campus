package me.maxwin.Util;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.fydia.campus.R;

/**
 * 
 * @author linxingqianglai
 * @category 该类的作用是sd卡上异步加载图片显示在ImageView上；
 * 用此类，图片可以已经被优化，同时放在缓存里
 * 构造方法ImageLoader dd = ImageLoader(Context context)
 * 实现方法dd.DisplayImage(String url, ImageView imageView) ;
 */
public class NativeImageLoader2 {  
	private LruCache<String,Bitmap> mCache;
	
	/**
	 * Collections.synchronizedMap(new WeakHashMap<ImageView, String>()); 
	 * 这里的作用保证里面的弱软引用是线程安全的，或者是线程同步的
	 */
    private Map<ImageView, String> imageViews = Collections  
            .synchronizedMap(new WeakHashMap<ImageView, String>());  
    // 线程池  
    ExecutorService executorService;
    public NativeImageLoader2(Context context) {  
        //fileCache = new FileCache(context);  
    	if(mCache == null)
    	{
    		mCache=new  LruCache<String, Bitmap>(200*1024*1024);
    	}
        executorService = Executors.newFixedThreadPool(10);  
    }  
  
    // 当进入listview时默认的图片，可换成你自己的默认图片  
    //final int stub_id = R.drawable.ic_launcher;  这里改动了
    final int stub_id = R.drawable.friends_sends_pictures_no;
    // 最主要的方法  
    //这里为什么是url对应imgageView的map，这是因为一个url的图像可以对应多个
    /*imageView,一个imageView对应一个url。
     * */
    public void DisplayImage(String url, ImageView imageView) {  
        imageViews.put(imageView, url);  
        // 先从内存缓存中查找  
  
        Bitmap bitmap = mCache.get(url);  
        if (bitmap != null&&imageView!=null) 
        {
        	imageView.setImageBitmap(bitmap);
        	//queuePhoto(url, imageView); 
        }    
        else {  
            // 若没有的话则开启新线程加载图片  
        	//从文件夹里加载也认为是耗时操作，
            queuePhoto(url, imageView);  
            //imageView.setImageResource(stub_id);  
        }  
    }  
  //新线程加载图片
    private void queuePhoto(String url, ImageView imageView) {  
        PhotoToLoad p = new PhotoToLoad(url, imageView);  
        executorService.submit(new PhotosLoader(p));//提交线程  
    }  
  
    private Bitmap getBitmap(String url) {  
  
        // 先从文件缓存中查找是否有  
        Bitmap b = decodeFile(url);  
        if (b != null)  
            return b;  
        else return null;
    }  
  
    // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的  
    private Bitmap decodeFile(String ImageURL) {  
        try {  
            // decode image size  
            BitmapFactory.Options o = new BitmapFactory.Options();  
            /*
             * If set to true, the decoder will return null (no bitmap), 
             * but the out... fields will still be set, （表示bitmap范围仍会被设置）
             * allowing the caller to query the bitmap without having to allocate（分配） the memory for its pixels.
             * （允许需求者不需要分配内存就能查询bitmap的像素，）
             * 这里的意思是如果o.inJustDecodeBounds是真，那么decode的对象不会返回bitmap对象，否则就会返回bitmap对象，
             * 比如下面的o2就是返回一个bitmap对象
             * */
            o.inJustDecodeBounds = true;  
            //这里就是表示不会返回bitmap对象
            BitmapFactory.decodeFile(ImageURL, o);//decodeStream(new FileInputStream(f), null, o);  
  
            // Find the correct scale value. It should be the power of 2.  
            //final int REQUIRED_SIZE = 70;  这边改动了如下
            final int REQUIRED_SIZE = 300;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;  
            int scale = 1;  
            int scale1 = 1;
            /*while (true) {  
                if (width_tmp / 2 < REQUIRED_SIZE  
                        || height_tmp / 2 < REQUIRED_SIZE)  
                    break;  
                width_tmp /= 2;  
                height_tmp /= 2; 
                //改动过
                scale *= 2;  
            }  */
 
          //这里做了改版
            scale = o.outWidth/REQUIRED_SIZE;
            scale1 = o.outHeight/REQUIRED_SIZE;
            scale = scale > scale1?scale:scale1;
            // decode with inSampleSize  
            BitmapFactory.Options o2 = new BitmapFactory.Options();  
            o2.inSampleSize = scale;  
            return BitmapFactory.decodeFile(ImageURL, o2);//decodeStream(new FileInputStream(f), null, o2);  
        } catch (Exception e) {  
        }  
        return null;  
    }  
  
    // Task for the queue  
    private class PhotoToLoad {  
        public String url;  
        public ImageView imageView;  
  
        public PhotoToLoad(String u, ImageView i) {  
            url = u;  
            imageView = i;  
        }  
    }  
  
    class PhotosLoader implements Runnable {  
        PhotoToLoad photoToLoad;  
  
        PhotosLoader(PhotoToLoad photoToLoad) {  
            this.photoToLoad = photoToLoad;  
        }  
  
        @Override  
        public void run() {  
            if (imageViewReused(photoToLoad))  
                return;  
            //这里为什么要Runnable,这是因为下面的行为是下载图片，所以要用用Runnable新开线程下载
            //同时还有个BitmapDisplayerRunnable，这里的作用因为不能再非ui线程中不能更新ui
            //必须在ui线程中进行更新操作，所有利用的是activity.runOnUiThread(Runnable);
            //所有按照整体看来，总共有两个Runnable();
            /*
             * 这里有个疑惑就是在getBitmap（string str）中实现了对sd卡的文件的访问
             * 这是因为对外存储sd卡的访问是延时访问，如果直接访问会影响用户体验，所以
             * 要在新线程里访问，
             * */
            Bitmap bmp = getBitmap(photoToLoad.url);  
            //mCache.put(photoToLoad.url, bmp);  
            if (imageViewReused(photoToLoad))  
                return;  
            BitmapDisplayerRunnable bd = new BitmapDisplayerRunnable(bmp, photoToLoad);  
            // 更新的操作放在UI线程中  
            //原来可以这样地获取上下文环境或者Activity
            Activity a = (Activity) photoToLoad.imageView.getContext();  
            a.runOnUiThread(bd);  
        }  
    }  
  
    /** 
     * 防止图片错位 
     *  如果tag为空，或者tag不等于当前需要的url，则表示这个photoToload不可以被使用。保证tag对应url。
     * @param photoToLoad 
     * @return 
     */  
    boolean imageViewReused(PhotoToLoad photoToLoad) {  
        String tag = imageViews.get(photoToLoad.imageView);  
        if (tag == null || !tag.equals(photoToLoad.url))  
            return true;  
        return false;  
    }  
  
    // 用于在UI线程中更新界面  
    class BitmapDisplayerRunnable implements Runnable {  
        Bitmap bitmap;  
        PhotoToLoad photoToLoad;  
        public BitmapDisplayerRunnable(Bitmap b, PhotoToLoad p) {  
            bitmap = b;  
            photoToLoad = p;  
        }  
  
        public void run() {  
            if (imageViewReused(photoToLoad)) 
                return;  
            if (bitmap != null)  
                photoToLoad.imageView.setImageBitmap(bitmap);  
            else  
                photoToLoad.imageView.setImageResource(stub_id);  
        }  
    }  
  
    public void clearCache() {  
        mCache.evictAll(); 
    }   
}  

