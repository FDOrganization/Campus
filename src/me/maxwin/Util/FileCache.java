package me.maxwin.Util;

import java.io.File;

import android.content.Context;
import android.util.Log;

public class FileCache {  
	  
    private File cacheDir;  
  
    public FileCache(Context context) {  
        // 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片  
        // 没有SD卡就放在系统的缓存目录中  
        if (android.os.Environment.getExternalStorageState().equals(  
                android.os.Environment.MEDIA_MOUNTED))  
            cacheDir = new File(  
                    context.getExternalCacheDir(),  
                    "LazyList");//这边是定义，实际还没有创建这个文件夹，在23行就是创建这个文件夹
        else  
            cacheDir = context.getCacheDir();  
        if (!cacheDir.exists())  
            cacheDir.mkdirs();  //这里是实际真正创建文件夹(或者目录)的地方
    }  
  
    public File getFile(String url) {  
        // 将url的hashCode作为缓存的文件名   hashCode是作为一个对象的身份证
        String filename = String.valueOf(url.hashCode());  
        // Another possible solution  
        // String filename = URLEncoder.encode(url);  
        File f = new File(cacheDir, filename);  
        Log.e("FileCache....................",f.toString());
        return f;  
  
    }  
  
    public void clear() {  
        File[] files = cacheDir.listFiles();  
        if (files == null)  
            return;  
        for (File f : files)  
            f.delete();  
    }  
  
}  
