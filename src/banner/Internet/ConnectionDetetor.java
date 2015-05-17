/**
 * 
 * 本类实现对网络连接状态的判断***/

package banner.Internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetetor {
	/**
	 * 默认三种网络状态 -1联通，0移动，1电信*/
	private int i;
	private Context context;
	public ConnectionDetetor(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	@SuppressWarnings("unused")
	public boolean isConnectiongToInternet(){
		ConnectivityManager manager = (ConnectivityManager)context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager != null){
			NetworkInfo [] infos = manager.getAllNetworkInfo();
			for(int i = 0 ; i < infos.length ; i++){
				if(infos[i].getState() == NetworkInfo.State.CONNECTED){
					return true;
				}
			}
		}
		return false;
	}
	
}
