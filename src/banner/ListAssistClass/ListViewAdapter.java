package banner.ListAssistClass;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListViewAdapter extends BaseAdapter {
	// 
	private List<ViewGroup> Listgroup;

	public ListViewAdapter(List<ViewGroup> Listgroup){
		this.Listgroup = Listgroup;
//		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Listgroup.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < Listgroup.size() ; i++)
		arg2 = Listgroup.get(i);
		return arg2;
	}
	

}
