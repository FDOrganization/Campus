package jx.push;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

import com.fydia.campus.R;

public class NoticeService extends Service {
	
	private BmobRealTimeData data;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		data = new BmobRealTimeData();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		data.start(this, new ValueEventListener() {
			
			@Override
			public void onDataChange(JSONObject arg0) {
				
				String title = null; 
				String content = null; 
				String channel = null;
				String thu_imgUrl = null;
				String imgUrl = null;
				String contentUrl = null;
				String source = null;
				String updatedAt = null;
              
				if(BmobRealTimeData.ACTION_UPDATETABLE.equals(arg0.opt("action"))){
					
					
					try {
						JSONObject data = arg0.optJSONObject("data");
						Log.d("json_test", arg0.toString());
						title = data.getString("title");
						content = data.getString("content");
						channel = data.getString("channel");
						thu_imgUrl = data.getString("thu_imgUrl");
						imgUrl = data.getString("imgUrl");
						contentUrl = data.getString("contentUrl");
						source = data.getString("source");
						updatedAt = data.getString("updatedAt");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("-RealTimeData-", data.toString());
					if(isPermitPush(channel)){
						Log.d("-FUCK-", "fuck");
						NotificationManager nm = (NotificationManager) NoticeService.this.getSystemService(Context.NOTIFICATION_SERVICE);
						Notification n = new Notification();
						
				        n.icon = R.drawable.ic_launcher;  
				        n.tickerText = title;  
				        n.when = System.currentTimeMillis();  
				        Intent i = new Intent(NoticeService.this,DetailActivity.class);  
	                    Bundle bundle = new Bundle();
	                    bundle.putString("title", title+" ");
	                    bundle.putString("content", content+" ");
	                    bundle.putString("channel", channel+"");
						bundle.putString("thu_imgUrl", thu_imgUrl+" ");
	                    bundle.putString("imgUrl", imgUrl+" ");
	                    bundle.putString("contentUrl", contentUrl+" ");                
						bundle.putString("source", source+" ");                    
						bundle.putString("updatedAt", updatedAt+" ");
	                    
	                    i.putExtras(bundle);
	                    
				        PendingIntent pi = PendingIntent.getActivity(NoticeService.this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);  
				        n.setLatestEventInfo(NoticeService.this, "œ˚œ¢",  content, pi);  
				        n.defaults |= Notification.DEFAULT_SOUND;
				        n.flags = Notification.FLAG_AUTO_CANCEL;
				        nm.notify(1, n);
					}
					
				}
			}
			
			@Override
			public void onConnectCompleted() {
				// TODO Auto-generated method stub
				if(data.isConnected()){
					data.subTableUpdate("NoticeList");
				}
			}
		});
		return START_STICKY;
	}

	/**
	 * ≈–∂¡ «∑ÒÕ∆ÀÕ
	 * @param channel 
	 * @return
	 */
	protected boolean isPermitPush(String channel) {
		SharedPreferences preferences = NoticeService.this.getSharedPreferences("setting", Context.MODE_PRIVATE);
	    boolean isAllowchi = preferences.getBoolean("set_chi", true);
	    boolean isAllowhe = preferences.getBoolean("set_he", true);
	    boolean isAllowwan = preferences.getBoolean("set_wan", true);
	    boolean isAllowle = preferences.getBoolean("set_le", true);
		
	    switch (channel) {
		case "≥‘":
			return isAllowchi;
		case "∫»":
			return isAllowhe;
		case "ÕÊ":
			return isAllowwan;
		case "¿÷":
			return isAllowle;		
		default:
			break;
		}
	    return true;
	}

}
