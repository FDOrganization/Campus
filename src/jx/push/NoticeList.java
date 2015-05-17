package jx.push;

import cn.bmob.v3.BmobObject;

public class NoticeList extends BmobObject {
	
	private String channel;
	private String title;
	private String content;
	private String contentUrl;
	private String thu_imgUrl;
	private String imgUrl;
	private String source;
		
    /**
     * 
     * @param channel 频道
     * @param title   标题	
     * @param content   内容	
     * @param contentUrl  详细内容网页
     * @param thu_imgUrl  缩略图
     * @param imgUrl      大图
     * @param source      来源
     */
	
	public NoticeList(String channel, String title, String content,
			String contentUrl, String thu_imgUrl, String imgUrl, String source) {
		super();
		this.channel = channel;
		this.title = title;
		this.content = content;
		this.contentUrl = contentUrl;
		this.thu_imgUrl = thu_imgUrl;
		this.imgUrl = imgUrl;
		this.source = source;
	}
	public String getThu_imgUrl() {
		return thu_imgUrl;
	}
	public void setThu_imgUrl(String thu_imgUrl) {
		this.thu_imgUrl = thu_imgUrl;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setMyUpdatedAt(String updatedAt) {
		this.setUpdatedAt(updatedAt);
	}

}
