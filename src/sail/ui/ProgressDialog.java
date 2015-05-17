package sail.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fydia.campus.R;


public class ProgressDialog extends android.app.ProgressDialog{
	private boolean flag = false;
	private Context context;
	private ImageView mImageView;
	private AnimationDrawable mAnimation;
	private int id;
	private String txt;
	private TextView mLoadingTv;
	public ProgressDialog(Context context, String txt,int id) {
		super(context);
		this.context = context;
		this.txt = txt;
		this.id = id;
		setCanceledOnTouchOutside(false);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
		mImageView.setBackgroundResource(id);
		mAnimation = (AnimationDrawable)mImageView.getBackground();
		mImageView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(flag){
					cancel();
				}
				mAnimation.start();
			}
		});
		mLoadingTv.setText(txt);
	}
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	

}
