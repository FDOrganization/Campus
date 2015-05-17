package banner.Load;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;


public class ImageDownload {
	private ExecutorService executorService ;
	private Bitmap bitmap;

	public ImageDownload(final String str,final RelativeLayout relativeLayout){
		executorService = Executors.newFixedThreadPool(6);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					@SuppressWarnings("deprecation")
					Drawable name = new BitmapDrawable(bitmap);
					relativeLayout.setBackground(name);

					break;
				}
			}
		};
		
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						URL Url = new URL(str);
						HttpURLConnection conn  = (HttpURLConnection)Url.openConnection();
						conn.setDoInput(true);
						conn.connect(); 
						InputStream inputStream = conn.getInputStream();
						bitmap = BitmapFactory.decodeStream(inputStream);
						Message msg=new Message();
						msg.what=1;
						handler.sendMessage(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

