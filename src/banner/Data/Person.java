package banner.Data;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class Person extends BmobObject {
	private String name;
	private String address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		
	}
	public String getaddress() {
		return address;
	} 
	public void setaddress(String address) {
		this.address = address;
	}
	
	
	
}

