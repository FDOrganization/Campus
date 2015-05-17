package me.maxwin.Util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	public MyToast() {
		// TODO Auto-generated constructor stub
	}
	public static void show(Context c,String str)
	{
		Toast.makeText(c, str, Toast.LENGTH_LONG).show();
	}
}