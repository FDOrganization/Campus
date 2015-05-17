package me.maxwin.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fydia.campus.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


/**
 * 
 * @author linxingqianglai
 * @category ������������������첽����ͼƬ��ʾ��ImageView�ϣ�
 *���췽��ImageLoader dd=ImageLoader(Context context)
 *ʵ�ַ���dd.DisplayImage(String url, ImageView imageView) ;
 */
public class ImageLoader {  
	  
    MemoryCache memoryCache = new MemoryCache();  
    FileCache fileCache;  
    private Map<ImageView, String> imageViews = Collections  
            .synchronizedMap(new WeakHashMap<ImageView, String>());  
    // �̳߳�  
    ExecutorService executorService;
    public ImageLoader(Context context) {  
        fileCache = new FileCache(context);  
        executorService = Executors.newFixedThreadPool(5);  
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
  
        Bitmap bitmap = memoryCache.get(url);  
        if (bitmap != null)  
            imageView.setImageBitmap(bitmap);  
        else {  
            // ��û�еĻ��������̼߳���ͼƬ  
        	//���ļ��������Ҳ��Ϊ�Ǻ�ʱ���������Ը��������ϼ���ͼƬͬ���Ƿ������߳������
            queuePhoto(url, imageView);  
            imageView.setImageResource(stub_id);  
        }  
    }  
  //���̼߳���ͼƬ
    private void queuePhoto(String url, ImageView imageView) {  
        PhotoToLoad p = new PhotoToLoad(url, imageView);  
        executorService.submit(new PhotosLoader(p));//�ύ�߳�  
    }  
  
    private Bitmap getBitmap(String url) {  
        File f = fileCache.getFile(url);  
  
        // �ȴ��ļ������в����Ƿ���  
        Bitmap b = decodeFile(f);  
        if (b != null)  
            return b;  
  
        // ����ָ����url������ͼƬ  
        try {  
            Bitmap bitmap = null;  
            URL imageUrl = new URL(url);  
            HttpURLConnection conn = (HttpURLConnection) imageUrl  
                    .openConnection();  
            conn.setConnectTimeout(30000);  
            conn.setReadTimeout(30000);  
            conn.setInstanceFollowRedirects(true);  
            InputStream is = conn.getInputStream();  
            OutputStream os = new FileOutputStream(f);  
            CopyStream(is, os);  
            os.close(); 
            /*
             * �Լ����
             * bitmap = BitmapFactory.decodeStream(is);
             * */
            
            bitmap = decodeFile(f);  
            is.close();
            return bitmap;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return null;  
        }  
    }  
  
    // decode���ͼƬ���Ұ����������Լ����ڴ����ģ��������ÿ��ͼƬ�Ļ����СҲ�������Ƶ�  
    private Bitmap decodeFile(File f) {  
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
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);  
  
            // Find the correct scale value. It should be the power of 2.  
            //final int REQUIRED_SIZE = 70;  ��߸Ķ�������
            final int REQUIRED_SIZE = 300;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;  
            int scale = 1;  
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

            // decode with inSampleSize  
            BitmapFactory.Options o2 = new BitmapFactory.Options();  
            o2.inSampleSize = scale;  
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);  
        } catch (FileNotFoundException e) {  
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
            memoryCache.put(photoToLoad.url, bmp);  
            if (imageViewReused(photoToLoad))  
                return;  
            BitmapDisplayerRunnable bd = new BitmapDisplayerRunnable(bmp, photoToLoad);  
            // ���µĲ�������UI�߳���  
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
        memoryCache.clear();  
        fileCache.clear();  
    }  
  
    public static void CopyStream(InputStream is, OutputStream os) {  
        final int buffer_size = 1024;  
        try {  
            byte[] bytes = new byte[buffer_size];  
            for (;;) {  
                int count = is.read(bytes, 0, buffer_size);  
                if (count == -1) //��ʾ���������� 
                    break;  
                os.write(bytes, 0, count);  
            }  
        } catch (Exception ex) {  
        }  
    }  
}  
