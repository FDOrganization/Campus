package banner.Data;

import java.io.File;
import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class ImageData extends BmobObject {

	/**
	 * 
	 */
	
	private String name;
	private String address;
	private String str;
	private File image;
	public String GetName() {
		return name;
	}
	public String Getaddress() {
		return address;
	}
	public String GetStr() {
		return str;
	}
	public File GetFile() {
		return image;
	}
}
