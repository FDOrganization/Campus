package me.maxwin.Util;

import java.lang.ref.SoftReference;
import java.util.Collections;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 
 * @author linxingqianglai
 * ����Ļ����õ���android 3.1�汾�Ժ�֧�ֵ�����LruCache
 *
 */
public  class MemoryCache {  
	  
    private static final String TAG = "MemoryCache";  
    // ���뻺��ʱ�Ǹ�ͬ������  
    // LinkedHashMap���췽�������һ������true�������map���Ԫ�ؽ��������ʹ�ô������ٵ�������
    //����LRU  
    // �����ĺô������Ҫ�������е�Ԫ���滻�����ȱ������������ʹ�õ�Ԫ�����滻�����Ч��  
    private LruCache<String, Bitmap> cache; 
    // ������ͼƬ��ռ�õ��ֽڣ���ʼ0����ͨ���˱����ϸ���ƻ�����ռ�õĶ��ڴ�  
    // ����ֻ��ռ�õ������ڴ�  
    private long limit = 1000000;// max memory in bytes  
  
    public MemoryCache() {  
        // use 25% of available heap size  
        setLimit(Runtime.getRuntime().maxMemory()); 
        if(cache==null)
        {
        	int MACMEMORY = (int)(Runtime.getRuntime().freeMemory());
        	cache = new  LruCache<String, Bitmap>(MACMEMORY)
        			{
        				protected int sizeOf(String key, Bitmap value) 
        				{
        					return value.getRowBytes()*value.getHeight()/1024;
        				};
        			};
        }
    }  
  
    public void setLimit(long new_limit) {   
        limit = new_limit;  
        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");  
    }  
  
    public Bitmap get(String id) {  
        try {  
            if (cache.get(id)==null)  
                return null;  
            return cache.get(id);  
        } catch (NullPointerException ex) {  
        	ex.printStackTrace();
            return null;  
        }  
    }  
  //����BitmapͼƬ
    public void put(String id, Bitmap bitmap) {  
        try {  
            if (cache.get(id)!=null)
            cache.put(id, bitmap);  
            //checkSize();  
        } catch (Throwable th) {  
            th.printStackTrace();  
        }  
    }  
    public void clear() {  
        cache.evictAll();  
    }  
  
    /** 
     * ͼƬռ�õ��ڴ� 
     *  
     * @param bitmap 
     * @return 
     */  
    long getSizeInBytes(Bitmap bitmap) {  
        if (bitmap == null)  
            return 0;  
        return bitmap.getRowBytes() * bitmap.getHeight();  
    }  
}  