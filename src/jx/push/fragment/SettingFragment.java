package jx.push.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fydia.campus.R;

@SuppressLint("NewApi")
public class SettingFragment extends Fragment implements OnClickListener{
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
		
	private RelativeLayout rl_switch_chi, rl_switch_he, rl_switch_wan, rl_switch_le;
	
	private ImageView iv_open_chi, iv_open_he, iv_open_wan, iv_open_le, 
	                  iv_close_chi, iv_close_he, iv_close_wan, iv_close_le;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		preferences = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
		editor = preferences.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.jx_fragment_set, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		
		rl_switch_chi = (RelativeLayout) getActivity().findViewById(R.id.rl_switch_chi);
		rl_switch_he  = (RelativeLayout) getActivity().findViewById(R.id.rl_switch_he);
		rl_switch_wan  = (RelativeLayout) getActivity().findViewById(R.id.rl_switch_wan);
		rl_switch_le  = (RelativeLayout) getActivity().findViewById(R.id.rl_switch_le);
		
		iv_open_chi = (ImageView) getActivity().findViewById(R.id.iv_open_chi);
		iv_open_he = (ImageView) getActivity().findViewById(R.id.iv_open_he);
		iv_open_wan = (ImageView) getActivity().findViewById(R.id.iv_open_wan);
		iv_open_le = (ImageView) getActivity().findViewById(R.id.iv_open_le);
				
		iv_close_chi = (ImageView) getActivity().findViewById(R.id.iv_close_chi);
		iv_close_he = (ImageView) getActivity().findViewById(R.id.iv_close_he);
		iv_close_wan = (ImageView) getActivity().findViewById(R.id.iv_close_wan);
		iv_close_le = (ImageView) getActivity().findViewById(R.id.iv_close_le);
		
		boolean isAllowchi = preferences.getBoolean("set_chi", true);
		boolean isAllowhe = preferences.getBoolean("set_he", true);
		boolean isAllowwan = preferences.getBoolean("set_wan", true);
		boolean isAllowle = preferences.getBoolean("set_le", true);
		
		rl_switch_chi.setOnClickListener(this);
		rl_switch_he.setOnClickListener(this);
		rl_switch_wan.setOnClickListener(this);
		rl_switch_le.setOnClickListener(this);
		
		if (isAllowchi) {
			iv_open_chi.setVisibility(View.VISIBLE);
			iv_close_chi.setVisibility(View.INVISIBLE);
		} else {
			iv_open_chi.setVisibility(View.INVISIBLE);
			iv_close_chi.setVisibility(View.VISIBLE);
		}
		
		if (isAllowhe) {
			iv_open_he.setVisibility(View.VISIBLE);
			iv_close_he.setVisibility(View.INVISIBLE);
		} else {
			iv_open_he.setVisibility(View.INVISIBLE);
			iv_close_he.setVisibility(View.VISIBLE);
		}
		
		if (isAllowwan) {
			iv_open_wan.setVisibility(View.VISIBLE);
			iv_close_wan.setVisibility(View.INVISIBLE);
		} else {
			iv_open_wan.setVisibility(View.INVISIBLE);
			iv_close_wan.setVisibility(View.VISIBLE);
		}
		
		if (isAllowle) {
			iv_open_le.setVisibility(View.VISIBLE);
			iv_close_le.setVisibility(View.INVISIBLE);
		} else {
			iv_open_le.setVisibility(View.INVISIBLE);
			iv_close_le.setVisibility(View.VISIBLE);
		}
	}

	public void onClick(View v) {

		switch(v.getId()){
		case R.id.rl_switch_chi:
			if (iv_open_chi.getVisibility() == View.VISIBLE) {
				iv_open_chi.setVisibility(View.INVISIBLE);
				iv_close_chi.setVisibility(View.VISIBLE);
				editor.putBoolean("set_chi", false);
			} else {
				iv_open_chi.setVisibility(View.VISIBLE);
				iv_close_chi.setVisibility(View.INVISIBLE);
				editor.putBoolean("set_chi", true);
			}

			break;
		case R.id.rl_switch_he:
			if (iv_open_he.getVisibility() == View.VISIBLE) {
				iv_open_he.setVisibility(View.INVISIBLE);
				iv_close_he.setVisibility(View.VISIBLE);
				editor.putBoolean("set_he", false);
			} else {
				iv_open_he.setVisibility(View.VISIBLE);
				iv_close_he.setVisibility(View.INVISIBLE);
				editor.putBoolean("set_he", true);
			}
			break;
		case R.id.rl_switch_wan:
			if (iv_open_wan.getVisibility() == View.VISIBLE) {
				iv_open_wan.setVisibility(View.INVISIBLE);
				iv_close_wan.setVisibility(View.VISIBLE);
				editor.putBoolean("set_wan", false);
			} else {
				iv_open_wan.setVisibility(View.VISIBLE);
				iv_close_wan.setVisibility(View.INVISIBLE);
				editor.putBoolean("set_wan", true);
			}
			break;
		case R.id.rl_switch_le:
			if (iv_open_le.getVisibility() == View.VISIBLE) {
				iv_open_le.setVisibility(View.INVISIBLE);
				iv_close_le.setVisibility(View.VISIBLE);
				editor.putBoolean("set_le", false);
			} else {
				iv_open_le.setVisibility(View.VISIBLE);
				iv_close_le.setVisibility(View.INVISIBLE);
				editor.putBoolean("set_le", true);
			}
		}
		editor.commit();
	}



}
