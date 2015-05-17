package sail.internet;
/**
 * net单例
 * @ClassName NetManager
 * @author sail
 * @date 2014/12/14 23:00
 * */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetManager {
	private static HttpClient client = new DefaultHttpClient();
	private  String name;

	private NetManager() {
	}
	private static class NetManagerHolder{
		private static final NetManager INS = new NetManager(); 
	}
	public static final NetManager getNetManager(){
		return NetManagerHolder.INS;
	}
	
	/**
	 * 用戶登錄
	 */
	public String getWebWithPost(String url, List<NameValuePair> params) {
		HttpPost httpRequest = new HttpPost(url);
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			HttpResponse httpResponse = client.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常是200
//				COOKIE = ((AbstractHttpClient)client).getCookieStore().getCookies();
				return readFromStream(httpResponse.getEntity().getContent());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取网页�?
	 */
	public static String readFromStream(InputStream inputStream)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream, "GBK"));
		String data = null;
		while ((data = br.readLine()) != null) {
			sb.append(data);
		}
		return sb.toString();
	}

	/**
	 * 获取验证�?
	 */
	public Bitmap getcode() throws Exception {
		// 声明post
		HttpPost httpPost = new HttpPost("http://jw.hzau.edu.cn/CheckCode.aspx");
		// 
		HttpResponse httpResponse = client.execute(httpPost);
		byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bitmap;
	}

	public String getWeb(String url, List<NameValuePair> params) {
		try {

			HttpPost httpRequest = new HttpPost(url);
			// httpRequest.setHeader("Cookie","ASP.NET_SessionId="+COOKIE.get(0).getValue());
			// httpRequest.setHeader("Accept","text/html, application/xhtml+xml");
			// httpRequest.setHeader("Accept-Encoding","gzip, deflate");
			// httpRequest.setHeader("Accept-Language","zh-CN");
			// httpRequest.setHeader("Cache-Control","Cache-Control");
			// httpRequest.setHeader("Connection","Keep-Alive");
			// httpRequest.setHeader("Content-Length","1595");
			// httpRequest.setHeader("Content-Type","application/x-www-form-urlencoded");
			 httpRequest.setHeader("Host","jw.hzau.edu.cn");
			httpRequest.setHeader("Referer", url);
			// httpRequest.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			// httpRequest.setHeader("X-HttpWatch-RID","35366-10018");
			if(!params.isEmpty())
				httpRequest.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			HttpResponse httpResponse = client.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
				return readFromStream(httpResponse.getEntity().getContent());
			else
				return "false";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	public String getViewState(String url,boolean SetReferer) {
		try {
			HttpPost httpRequest = new HttpPost(url);
			if(SetReferer){
				 httpRequest.setHeader("Referer", url);

			}
			HttpResponse response = client.execute(httpRequest);
			if (response.getStatusLine().getStatusCode() == 200) {
				mJSoup js = new mJSoup(readFromStream(response.getEntity()
						.getContent()));
				return js.getViewState();
				// return readFromStream(response.getEntity().getContent());
			} else
				return null;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		
		}
		return null;
	}
	// 解析网页得到学生姓名
	public boolean getName(String html) {
		mJSoup js2 = new mJSoup(html);
		name = js2.getName();
			return true;
	}
	//
	public String getName(){
		return name;
	}
	
}
