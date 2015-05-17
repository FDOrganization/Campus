package sail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.fydia.campus.R;
/**
 * 添加成绩button调处的Fragment
 * @author sail
 * */
public class AddFragment extends Fragment{
	private View view;
	private Button addButton;
	private fralistener listener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (View)inflater.inflate(R.layout.fra_add_grade, container, false);
		return view;
	
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}
	private void init(){
		addButton = (Button)view.findViewById(R.id.ask_new_grade);
		addButton.setOnClickListener(new myListener());
	}
	class myListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ask_new_grade:
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				listener.goTo(2);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
		
	}
	public interface fralistener{
		public void goTo(int i);
	}
	public void setListener(fralistener listener){
		this.listener = listener;
	}
}
