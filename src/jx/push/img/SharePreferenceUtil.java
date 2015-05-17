package jx.push.img;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	private String SHARE_KEY_CHI = "chi";
	private String SHARE_KEY_HE = "he";
	private String SHARE_KEY_WAN = "wan";
	private String SHARE_KEY_LE = "le";
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor;
	
	public SharePreferenceUtil(Context context, String name) {
		// TODO Auto-generated constructor stub
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

}
