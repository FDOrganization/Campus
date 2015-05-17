package lein.fydia.regist;

import android.view.View.OnClickListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;

public class MyUser extends BmobUser {
	private static MyUser user;
	private String nickname;
	private BmobRelation ToLunTan;
	public MyUser() {
		// TODO Auto-generated constructor stub
	}

    public static MyUser getMyUser()
    {
    	if(user==null)
    	{
    		user=new MyUser();
    	}
    	return user;
    }
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public BmobRelation getToLunTan() {
		return ToLunTan;
	}
	public void setToLunTan(BmobRelation toLunTan) {
		ToLunTan = toLunTan;
	}



}
