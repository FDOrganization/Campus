package lxq.example.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
/**
 * 
 * @author linxingqianglai
 * Content ��̳˵˵Item������   ;
 * ImageURL ��ʾ����ͼƬ�ĵ�ַ  ; һ��String��
 * ToUser   ��ʾ�û�    ;
 * Zan      ��ʾ���޵�����������Ŀǰû���������ֽ����ˣ� ;
 * ToTalk    ��ʾ˵˵Item���۵����ݣ���Ӧ����1�Զ�Ĺ�ϵ
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
