package sail.XmlAssist;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fydia.campus.R;

public class MyGradeListAdapter extends BaseAdapter{
	private Map<String,List<String>> data;
	private Context context;
	public MyGradeListAdapter(Map<String,List<String>> data,Context context) {
		this.data = data;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.get("课程名").size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View v, ViewGroup vg) {
		v = LayoutInflater.from(context).inflate(R.layout.grade_list_item, null);
		TextView text1 = (TextView)v.findViewById(R.id.grade_list_item_1);
		TextView text2 = (TextView)v.findViewById(R.id.grade_list_item_2);
		List<String> list1 = data.get("课程名");
		List<String> list2 = data.get("期中成绩");
		text1.setText(list1.get(position).toString());
		text2.setText(list2.get(position).toString());
		return v;
	}

}
