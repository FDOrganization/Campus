package sail.ui;

import sail.XmlAssist.MyGradeListAdapter;
import sail.data.OperateData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.fydia.campus.R;

public class GradeListFragment extends Fragment{
	private View view;
	private Spinner spinner1,spinner2;
	private int s1,s2;
	private OperateData operateData;
	private String[] str1,str2;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = (View)inflater.inflate(R.layout.fra_grade, container,false);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		init();
	}
	private void init(){
		//
		operateData = new OperateData(getActivity());
		//
		str1 = getResources().getStringArray(R.array.years);
		str2 = getResources().getStringArray(R.array.num);
		//
		spinner1 = (Spinner)view.findViewById(R.id.grade_spinner_1);
		spinner2 = (Spinner)view.findViewById(R.id.grade_spinner_2);
		listView = (ListView)view.findViewById(R.id.gradeList);
		String[] str1 = getResources().getStringArray(R.array.years);
		String[] str2 = getResources().getStringArray(R.array.num);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,str1);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,str2);
		spinner1.setAdapter(adapter1);
		spinner2.setAdapter(adapter2);
		spinner1.setOnItemSelectedListener(new mySpinnerListener());
		spinner2.setOnItemSelectedListener(new mySpinnerListener());
	}
	class mySpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int position,
				long id) {
//			switch (view.getId()) {
//			case R.id.grade_spinner_1:
//				s1 = position;
//				break;
//			case R.id.grade_spinner_2:
//				s2 = position;
//				break;
//			default:
//				break;
//			}
			MyGradeListAdapter adapter = new MyGradeListAdapter(operateData.getGrade(spinner1.getSelectedItem().toString()
					, spinner2.getSelectedItem().toString()),getActivity());
			listView.setAdapter(adapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
}
