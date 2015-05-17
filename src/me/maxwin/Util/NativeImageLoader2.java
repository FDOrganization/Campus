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
 * @category �����������sd�����첽����ͼƬ��ʾ��ImageView�ϣ�
 * �ô��࣬ͼƬ�����Ѿ����Ż���ͬʱ���ڻ�����
 * ���췽��ImageLoader dd = ImageLoader(Context context)
 * ʵ�ַ���dd.DisplayImage(String url, ImageView imageView) ;
 */
public class NativeImageLoader2 {  
	private LruCache<String,Bitmap> mCache;
	
	/**
	 * Collections.synchronizedMap(new WeakHashMap<ImageView, String>()); 
	 * ��������ñ�֤����������������̰߳�ȫ�ģ��������߳�ͬ����
	 */
    private Map<ImageView, String> imageViews = Collections  
            .synchronizedMap(new WeakHashMap<ImageView, String>());  
    // �̳߳�  
    ExecutorService executorService;
    public NativeImageLoader2(Context context) {  
        //fileCache = new FileCache(context);  
    	if(mCache == null)
    	{
    		mCache=new  LruCache<String, Bitmap>(200*1024*1024);
    	}
        executorService = Executors.newFixedThreadPool(10);  
    }  
  
    // ������listviewʱĬ�ϵ�ͼƬ���ɻ������Լ���Ĭ��ͼƬ  
    //final int stub_id = R.drawable.ic_launcher;  ����Ķ���
    final int stub_id = R.drawable.friends_sends_pictures_no;
    // ����Ҫ�ķ���  
    //����Ϊʲô��url��ӦimgageView��map��������Ϊһ��url��ͼ����Զ�Ӧ���
    /*imageView,һ��imageView��Ӧһ��url��
     * */
    public void DisplayImage(String url, ImageView imageView) {  
        imageViews.put(imageView, url);  
        // �ȴ��ڴ滺���в���  
  
        Bitmap bitmap = mCache.get(url);  
        if (bitmap != null&&imageView!=null) 
        {
        	imageView.setImageBitmap(bitmap);
        	//queuePhoto(url, imageView); 
        }    
        else {  
            // ��û�еĻ��������̼߳���ͼƬ  
        	//���ļ��������Ҳ��Ϊ�Ǻ�ʱ������
            queuePhoto(url, imageView);  
            //imageView.setImageResource(stub_id);  
        }  
    }  
  //���̼߳���ͼƬ
    private void queuePhoto(String url, ImageView imageView) {  
        PhotoToLoad p = new PhotoToLoad(url, imageView);  
        executorService.submit(new PhotosLoader(p));//�ύ�߳�  
    }  
  
    private Bitmap getBitmap(String url) {  
  
        // �ȴ��ļ������в����Ƿ���  
        Bitmap b = decodeFile(url);  
        if (b != null)  
            return b;  
        else return null;
    }  
  
    // decode���ͼƬ���Ұ����������Լ����ڴ����ģ��������ÿ��ͼƬ�Ļ����СҲ�������Ƶ�  
    private Bitmap decodeFile(String ImageURL) {  
        try {  
            // decode image size  
            BitmapFactory.Options o = new BitmapFactory.Options();  
            /*
             * If set to true, the decoder will return null (no bitmap), 
             * but the out... fields will still be set, ����ʾbitmap��Χ�Իᱻ���ã�
             * allowing the caller to query the bitmap without having to allocate�����䣩 the memory for its pixels.
             * �����������߲���Ҫ�����ڴ���ܲ�ѯbitmap�����أ���
             * �������˼�����o.inJustDecodeBounds���棬��ôdecode�Ķ��󲻻᷵��bitmap���󣬷���ͻ᷵��bitmap����
             * ���������o2���Ƿ���һ��bitmap����
             * */
            o.inJustDecodeBounds = true;  
            //������Ǳ�ʾ���᷵��bitmap����
            BitmapFactory.decodeFile(ImageURL, o);//decodeStream(new FileInputStream(f), null, o);  
  
            // Find the correct scale value. It should be the power of 2.  
            //final int REQUIRED_SIZE = 70;  ��߸Ķ�������
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
                //�Ķ���
                scale *= 2;  
            }  */
 
          //�������˸İ�
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
            //����ΪʲôҪRunnable,������Ϊ�������Ϊ������ͼƬ������Ҫ����Runnable�¿��߳�����
            //ͬʱ���и�BitmapDisplayerRunnable�������������Ϊ�����ٷ�ui�߳��в��ܸ���ui
            //������ui�߳��н��и��²������������õ���activity.runOnUiThread(Runnable);
            //���а������忴�����ܹ�������Runnable();
            /*
             * �����и��ɻ������getBitmap��string str����ʵ���˶�sd�����ļ��ķ���
             * ������Ϊ����洢sd���ķ�������ʱ���ʣ����ֱ�ӷ��ʻ�Ӱ���û����飬����
             * Ҫ�����߳�����ʣ�
             * */
            Bitmap bmp = getBitmap(photoToLoad.url);  
            //mCache.put(photoToLoad.url, bmp);  
            if (imageViewReused(photoToLoad))  
                return;  
            BitmapDisplayerRunnable bd = new BitmapDisplayerRunnable(bmp, photoToLoad);  
            // ���µĲ�������UI�߳���  
            //ԭ�����������ػ�ȡ�����Ļ�������Activity
            Activity a = (Activity) photoToLoad.imageView.getContext();  
            a.runOnUiThread(bd);  
        }  
    }  
  
    /** 
     * ��ֹͼƬ��λ 
     *  ���tagΪ�գ�����tag�����ڵ�ǰ��Ҫ��url�����ʾ���photoToload�����Ա�ʹ�á���֤tag��Ӧurl��
     * @param photoToLoad 
     * @return 
     */  
    boolean imageViewReused(PhotoToLoad photoToLoad) {  
        String tag = imageViews.get(photoToLoad.imageView);  
        if (tag == null || !tag.equals(photoToLoad.url))  
            return true;  
        return false;  
    }  
  
    // ������UI�߳��и��½���  
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

