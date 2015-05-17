package lxq.example.table;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
/**
 * 
 * @author linxingqianglai
 * ToLunTan 表示一个人所写的论坛个数
 */
public class MyUser extends BmobUser {
	private static MyUser user;
	private String nickname;//这里的作用似乎没用到
	private BmobRelation ToLunTan;
	private  MyUser() {
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
