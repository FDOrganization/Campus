package jx.push;


import com.fydia.campus.R;

import jx.push.img.ImageLoader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	private TextView title;
	private TextView content;
	private TextView more;
	private TextView time;
	private ImageView bigImg;
	private ImageView headImg;
	
	ImageLoader imageLoader; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jx_activity_detail);
		initView();
		init();
	}

	private void initView() {
		
		title = (TextView) findViewById(R.id.dtl_title);
		content = (TextView) findViewById(R.id.dtl_content);
		more = (TextView) findViewById(R.id.dtl_more);
		time = (TextView) findViewById(R.id.dtl_time);
		bigImg = (ImageView) findViewById(R.id.dtl_bigimg);
		headImg = (ImageView) findViewById(R.id.dtl_header);
		
	}

	private void init() {
		Intent mIntent = getIntent();
		if(mIntent != null){
			imageLoader = new ImageLoader(DetailActivity.this);
			Bundle bundle = mIntent.getExtras();		
			if(bundle!=null){
				String mtitle = bundle.getString("title");
				String mcontent = bundle.getString("content");
				String mchannel = bundle.getString("channel");
				String mthu_imgUrl = "http://file.bmob.cn/"+bundle.getString("thu_imgUrl");
				String mImgUrl = bundle.getString("imgUrl");
				final String mcontentUrl = bundle.getString("contentUrl");
				String msource = bundle.getString("source");
				String mtime = bundle.getString("updatedAt");
				
				title.setText(mtitle);
				content.setText(mcontent);
				time.setText(mtime);
				
				headImg.setImageBitmap(ImageLoader.getNewsImgHead(mchannel));
				
				imageLoader.DisplayImage(mImgUrl, bigImg);
				
				
				Log.d("title", mtitle+" ");
				Log.d("content", mcontent+" ");
				Log.d("channel", mchannel+" ");
				Log.d("imgUrl", mImgUrl+" ");
				Log.d("mcontent", mcontent);
				Log.d("source", msource+" ");
				Log.d("mtime", mtime+" ");
				Log.d("url", mcontentUrl+" ");
				
				more.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(DetailActivity.this, WebViewActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("mcontentUrl", mcontentUrl);
						i.putExtras(bundle);
						startActivity(i);
					}
				});
				
			}else System.out.println("error!!!");
		}

	}

}
