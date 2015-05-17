package me.maxwin.Util;

import java.io.File;

import android.content.Context;
import android.util.Log;

public class FileCache {  
	  
    private File cacheDir;  
  
    public FileCache(Context context) {  
        // �����SD������SD���н�һ��LazyList��Ŀ¼��Ż����ͼƬ  
        // û��SD���ͷ���ϵͳ�Ļ���Ŀ¼��  
        if (android.os.Environment.getExternalStorageState().equals(  
                android.os.Environment.MEDIA_MOUNTED))  
            cacheDir = new File(  
                    context.getExternalCacheDir(),  
                    "LazyList");//����Ƕ��壬ʵ�ʻ�û�д�������ļ��У���23�о��Ǵ�������ļ���
        else  
            cacheDir = context.getCacheDir();  
        if (!cacheDir.exists())  
            cacheDir.mkdirs();  //������ʵ�����������ļ���(����Ŀ¼)�ĵط�
    }  
  
    public File getFile(String url) {  
        // ��url��hashCode��Ϊ������ļ���   hashCode����Ϊһ����������֤
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
