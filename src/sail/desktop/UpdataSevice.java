package sail.desktop;
/**
 * ����С����������
 * @ClassName UpdataSevice
 * @author */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdataSevice extends Service{
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * ���񱻴���ʱ
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	/**
	 *  ����ֹͣʱ���ע��
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
