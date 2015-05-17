package me.maxwin.Util;

import java.lang.ref.SoftReference;
import java.util.Collections;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 
 * @author linxingqianglai
 * 这里的缓存用到的android 3.1版本以后支持的软缓存LruCache
 *
 */
public  class MemoryCache {  
	  
    private static final String TAG = "MemoryCache";  
    // 放入缓存时是个同步操作  
    // LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列
    //，即LRU  
    // 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率  
    private LruCache<String, Bitmap> cache; 
    // 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存  
    // 缓存只能占用的最大堆内存  
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
  //存入Bitmap图片
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
     * 图片占用的内存 
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