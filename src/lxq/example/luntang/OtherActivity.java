package lxq.example.luntang;

import java.io.File;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import cn.bmob.v3.datatype.BmobFile;

import com.fydia.campus.R;

public class OtherActivity extends ActionBarActivity {
	ImageView ig;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_linxingqiang);
		ig=(ImageView)findViewById(R.id.img);
		/*BmobFile file=new BmobFile(new File("file.bmob.cn/M00/DD/61/oYYBAFSXyz6AATb7AAAHW12KAzQ862.png"));
		//file.bmob.cn/M00/DD/61/oYYBAFSXyz6AATb7AAAHW12KAzQ862.png
		///mnt/sdcard/kugou/market/20140115174623714009.png
		file.loadImage(this, ig);
		Log.e("fileUrl",file.getFileUrl(this));*/
		String picPath = "file.bmob.cn/M00/DD/64/oYYBAFSX2WSARtgyAAAHW12KAzQ118.png";
		final BmobFile bmobFile = new BmobFile(new File(picPath));
		bmobFile.loadImage(OtherActivity.this, ig);
		/*bmobFile.uploadblock(this, new UploadFileListener() {

		    @Override
		    public void onSuccess() {
		        // TODO Auto-generated method stub
		        Log.e("----","上传文件成功:" + bmobFile.getFileUrl(getApplicationContext()));
		        bmobFile.loadImage(OtherActivity.this, ig);
		    }

		    @Override
		    public void onProgress(Integer value) {
		        // TODO Auto-generated method stub
		        // 返回的上传进度（百分比）
		    }

		    @Override
		    public void onFailure(int code, String msg) {
		        // TODO Auto-generated method stub
		       Log.e("","上传文件失败：" + msg);
		    }
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.other, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
