/**�ֻ�������ݴ��,������**/
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
	public static int flag = 0;                        // ����α���߳ɼ�
	public static int tabFlag=0;                   // tab���汻ѡ�еı�ǩ��  
	public static String gradeaskpswd = null;      // �û����õĲ�����
	public static int year;
	public static int month;
	public static int day;
	public static int curWeekDay = 1;               // ����Ĭ��Ϊ1
	public static DisplayMetrics Ssize;             // ����ֻ���Ļ��С
	public static int TitleSize;                    // �α����������
	public static String[] selectStr = {"1","2","3"};
	public static int selectFlag = 0;              	
	public static int curWeek = 1;                   // ��ǰ������Ĭ��Ϊ1
	public static String loginUrl;                    // ��½����
	public static String scheduleUrl;                 // �α�����
	public static List<String> scheduledata = null;           // �α���Դ
	public static List<String> gradedata = null;
	public static String Semester = null;             // ѧ��
	public static String UserName = null;			  // ����
	public static String PassWord = null;             // ����
	public static String code = null;                 // ��֤��
}
