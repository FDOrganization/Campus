package jx.push.listview;

import java.util.List;

import jx.push.NoticeList;
import jx.push.img.ImageLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fydia.campus.R;

public class XListViewAdapter extends BaseAdapter {
	
    Context context;
    List<NoticeList> noticeList;
    ImageLoader imageLoader;
    
	public XListViewAdapter(Context context, List<NoticeList> noticeList) {
		super();
		this.context = context;
		this.noticeList = noticeList;
		imageLoader = new ImageLoader(context);
	}
	
	public final class ViewHolder{  
	        public ImageView imgHead;  
	        public TextView title;  
	        public TextView content;  
	        public TextView source;
	}  
	
	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public Object getItem(int position) {
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null){
			
			convertView =LayoutInflater.from(context).inflate(R.layout.jx_simple_item, null);
			holder = new ViewHolder();
			holder.imgHead = (ImageView) convertView.findViewById(R.id.header);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.source = (TextView) convertView.findViewById(R.id.source);
			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
        NoticeList nl = noticeList.get(position);
        if(nl.getImgUrl() != null)
        {
        	imageLoader.DisplayImage(nl.getImgUrl(), holder.imgHead);
        }
        else
        {
        	Bitmap bitmap = imageLoader.getNewsImgHead(nl.getChannel());
        	holder.imgHead.setImageBitmap(bitmap);
        }
		holder.title.setText(nl.getTitle());
		holder.content.setText(nl.getContent());
		
		if(nl.getSource()!=null){
			holder.source.setText(nl.getSource());
		}
		else
		{
			holder.source.setText("ทะตในคื๗สา");
		}
		return convertView;
	}
		
}
