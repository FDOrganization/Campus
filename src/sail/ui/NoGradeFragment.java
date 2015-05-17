package sail.ui;

import sail.data.LocalInforM;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fydia.campus.R;

public class NoGradeFragment extends Fragment{
	private View view;
	private Button addButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = (View)inflater.inflate(R.layout.fra_no_grade, container,false);
		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}
	private void init(){
		addButton = (Button)view.findViewById(R.id.no_ask_grade);
		addButton.setOnClickListener(new myListener());
	}
	class myListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.no_ask_grade:
				LocalInforM.flag = LocalInforM.GETGRADE;
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		
	}
}
