package lxq.example.table;

import cn.bmob.v3.BmobObject;
/**
 * 
 * @author linxingqianglai
 * ToLunTan表示对应的论坛;
	Content表示对应的论坛的内容
	评论者s
	private MyUser TalkName;
 *
 */
public class Talk extends BmobObject {
	//ToLunTan表示对应的论坛
	private LunTan ToLunTan;
	//Content表示对应的论坛的内容
	private String Content;
	//评论者
	private MyUser TalkName;
	private Boolean CannotSee;
	public Talk() {
		// TODO Auto-generated constructor stub
	}

	public Talk(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Boolean getCannotSee() {
		return CannotSee;
	}

	public void setCannotSee(Boolean cannotSee) {
		CannotSee = cannotSee;
	}

	public LunTan getToLunTan() {
		return ToLunTan;
	}

	public void setToLunTan(LunTan toLunTan) {
		ToLunTan = toLunTan;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public MyUser getTalkName() {
		return TalkName;
	}

	public void setTalkName(MyUser talkName) {
		TalkName = talkName;
	}
	

}
