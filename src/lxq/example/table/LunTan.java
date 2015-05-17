package lxq.example.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
/**
 * 
 * @author linxingqianglai
 * Content 论坛说说Item的内容   ;
 * ImageURL 表示加载图片的地址  ; 一个String；
 * ToUser   表示用户    ;
 * Zan      表示点赞的数量（不过目前没有让他出现界面了） ;
 * ToTalk    表示说说Item评论的内容；对应的是1对多的关系
 */
public class LunTan extends BmobObject {
	private String Content;
	//private ArrayList<String> ImageURL=new ArrayList<String>();
	private String ImageURL;
	private BmobRelation ToTalk;
	private MyUser ToUser;
	private Integer Zan;
	public MyUser getToUser() {
		return ToUser;
	}

	public void setToUser(MyUser toUser) {
		ToUser = toUser;
	}

	public Integer getZan() {
		return Zan;
	}

	public void setZan(Integer zan) {
		Zan = zan;
	}

	public LunTan() {
		// TODO Auto-generated constructor stub
	}

	public LunTan(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}


	/*public ArrayList<String> getImageURL() {
		return ImageURL;
	}*/
	public String getImageURL()
	{
		return ImageURL;
	}
	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}

	public BmobRelation getToTalk() {
		return ToTalk;
	}

	public void setToTalk(BmobRelation toTalk) {
		ToTalk = toTalk;
	}

	public MyUser getUser() {
		return ToUser;
	}

	public void setUser(MyUser user) {
		this.ToUser = user;
	}

	
	

}
