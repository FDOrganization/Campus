/**手机相关数据存放,单例类**/
package sail.data;

import java.util.List;

import android.util.DisplayMetrics;

public class LocalInforM {
	private LocalInforM(){}
	public static LocalInforM mLocalInforM;
	public static LocalInforM getmLocal(){
		if(mLocalInforM == null){
			mLocalInforM = new LocalInforM();
		}
		return mLocalInforM;
	}
	public final static int GETGRADE = 121605;
	public final static int GETSCHDE = 121603;
	public static int flag = 0;                        // 处理课表或者成绩
	public static int tabFlag=0;                   // tab界面被选中的标签号  
	public static String gradeaskpswd = null;      // 用户设置的查验码
	public static int year;
	public static int month;
	public static int day;
	public static int curWeekDay = 1;               // 星期默认为1
	public static DisplayMetrics Ssize;             // 存放手机屏幕大小
	public static int TitleSize;                    // 课表大标题栏宽度
	public static String[] selectStr = {"1","2","3"};
	public static int selectFlag = 0;              	
	public static int curWeek = 1;                   // 当前周数，默认为1
	public static String loginUrl;                    // 登陆链接
	public static String scheduleUrl;                 // 课表链接
	public static List<String> scheduledata = null;           // 课表资源
	public static List<String> gradedata = null;
	public static String Semester = null;             // 学期
	public static String UserName = null;			  // 姓名
	public static String PassWord = null;             // 密码
	public static String code = null;                 // 验证码
}
