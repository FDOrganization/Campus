package sail.XmlAssist;
/**
 * 横向listview adapter
 * @author sail
 * @date 2014.12.18 20:20
 * */
import sail.data.LocalInforM;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fydia.campus.R;

public class HorListBaseAdapter extends BaseAdapter {
	public int selectIndex = LocalInforM.curWeek;
	@SuppressWarnings("unused")
	private Context context;
	private String[] str;
	private TextView textView;
	public HorListBaseAdapter(Context context,String[] str){
		this.context = context;
		this.str = str;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View v, ViewGroup gv) {
		// TODO Auto-generated method stub
		// 获得显示的item
		
		v = (View)LayoutInflater.from(gv.getContext()).inflate(R.layout.activity_horlistitem, null);
		textView = (TextView)v.findViewById(R.id.HorTextView);
		TextView hintTextView = (TextView)v.findViewById(R.id.HintTextView);
		textView.setText(str[position]);
//		textView.setOnClickListener(listener);
		// 不触发点击事件
//		textView.setClickable(false);
		if(selectIndex-1 == position){
			textView.setBackgroundResource(R.drawable.select_back);
			hintTextView.setBackgroundResource(R.drawable.hint_text_back);
			hintTextView.setText("        长按\n   设为本周");
			hintTextView.setTextSize(7);
//			textView.setBackgroundColor(Color.parseColor("#FF5151"));
			}
		else{
				textView.setBackgroundColor(Color.parseColor("#d0d0d0"));
				// 未选中则不能触发longclick
				textView.setLongClickable(false);
			}
		return v;
	}
	
	
	
			

}
