package sail.internet;
/**
 * html����������
 * @ClassName mJsoup
 * @author sail
 * @date 2014/12/14 20:21
 * */
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class mJSoup {
	private String html;
	public mJSoup(String source) {
		// TODO Auto-generated constructor stub
		html = source;
	}
	/*
	 * �õ�stateview
	 * @author sail
	 * */
	public String getViewState(){
		String state = null;
		int i,j;
		String temp = html; 
		String flag1 = "VIEWSTATE\" value=\"";
		i=temp.indexOf(flag1);
		temp = temp.substring(i,temp.length()); 
		String flag2 = "\" />";
		j=temp.indexOf(flag2);
		state = temp.substring(flag1.length(),j);
		return state;
	}
	public String getName(){
		String name = null;
		Document document = Jsoup.parse(html);
		Element element = document.getElementById("xhxm");
		name = element.text();
		name = name.substring(0,name.length()-2);
		return name;
	}
	/*
	 * �õ��α�string
	 * @author sail
	 * */
	public List<String> getScheduleCourse(){
		List<String> Course = new ArrayList<String>();
		Document document = Jsoup.parse(html);
		Elements elements = document.select("td[rowspan]");
		for(Element element:elements){
			String str = element.text();
			// ���˵�������Ϣ
			if(str.length() > 2)
				Course.add(str);
		}
		return Course;
	}
	/*
	 * �õ��ɼ�string
	 * @author sail
	 * */
	public List<String> getGradeCourse(){
		List<String> Course = new ArrayList<String>();
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByTag("tr");
		for(Element element:elements){
			String str = element.text();
			// ���˵�������Ϣ
			if(str.length() > 2)
				Course.add(str);
		}
		return Course;
	}
}
