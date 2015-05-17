package jx.push;

import org.json.JSONException;
import org.json.JSONObject;

import com.fydia.campus.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import cn.bmob.push.PushConstants;

public class NoticePushReceiver extends BroadcastReceiver{

	private SharedPreferences preferences;
	
    boolean isAllowchi;
	boolean isAllowhe; 
	boolean isAllowwan;
	boolean isAllowle;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
	    preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
	    isAllowchi = preferences.getBoolean("set_chi", true);
		isAllowhe = preferences.getBoolean("set_he", true);
		isAllowwan = preferences.getBoolean("set_wan", true);
		isAllowle = preferences.getBoolean("set_le", true);
		
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE))
		{
			String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			// ����֪ͨ
			Log.d("bombPush", "�ͻ��˽�����Ϣ��"+message);
			
			JSONObject json;
			JSONObject json1,json2;
			try {
				json = new JSONObject(message);
			    json1 = json.getJSONObject("aps");
			    String  category = json.getString("category");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			Notification n = new Notification();  
	        n.icon = R.drawable.ic_launcher;  
	        n.tickerText = "У԰��Ϣ����";  
	        n.when = System.currentTimeMillis();  
	        //n.flags=Notification.FLAG_ONGOING_EVENT;  
	        Intent i = new Intent(context,PushActivity.class);
	        i.putExtra("title", "У԰��Ϣ����");
	        i.putExtra("content", "У԰��Ϣ����");
	        i.putExtra("channel", "У԰��Ϣ����");
	        i.putExtra("imgUrl", "У԰��Ϣ����");
	        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
	        n.setLatestEventInfo(context, "��Ϣ", message, pi);  
	        n.defaults |= Notification.DEFAULT_SOUND;
	        n.flags = Notification.FLAG_AUTO_CANCEL;
	        nm.notify(1, n);
	        
		}
		
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			//����ʵʱ���ݼ���Service
			context.startService(new Intent(context, NoticeService.class));
		}
	}

	

}
