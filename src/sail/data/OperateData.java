package sail.data;

/**
 * �������ݿ�,�����ݽ���������
 * @ClassName operatedata
 * @author sail
 * @date 2014.12.12 ����9:23
 * */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sail.XmlAssist.ClassInfo;
import sail.internet.GetSchdule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

@SuppressWarnings("unused")
public class OperateData {
	private List<String> dataList = new ArrayList<String>();
	public 	final String SCTABLENAME = "schedule"; 
	public  final String GRTABLENAME = "grade";
	private static final String DBNAME = "schedule.db";
	private static final int DANZHOU = 1;
	private static final int SHUANGZHOU = 2;
	private static final int NONE = 0;
	private String[] weekdays = {"��һ","�ܶ�","����","����","����","����","����"};  // �ж��ܼ�
	// �α�����
	private ArrayList<ClassInfo> list = new ArrayList<ClassInfo>();
	private Context context;
	// ��ʼ��SQLhelper��
	private mySQLiteHelper helper ;
	public OperateData(Context context){
		this.context = context;
		helper = new mySQLiteHelper(context,DBNAME);

		switch (LocalInforM.flag) {
		case LocalInforM.GETSCHDE:
			// ɾ���ɱ�
			DeleteTable(SCTABLENAME);
			mydealScheduleData(LocalInforM.scheduledata);
			break;
		case LocalInforM.GETGRADE:
			// ɾ���ɱ�
			// DeleteTable(GRTABLENAME);
			try {
				savaGrades();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}


	/** 
	 * �α����ݾ����ⷽ��
	 * @author sail
	 * */
	private void mydealScheduleData(List<String> dataList){
		for(String string : dataList){

			// ���ո�ֶ�
			String[] temp = string.split(" ");
			// ��ȡ������string���鳤��
			int length = temp.length;
			// ������5����һ���������Ҫ�Ĵ��������������temp[2]֮��
			// temp[0],temp[3],temp[4]�ֱ�Ϊ�γ�������ʦ������
			if(length == 5){
				cut(temp[0],temp[4],temp[2]);
			}
			else if(10 > length &&length > 5){
				cut(temp[0],temp[4],temp[2]);
			}
			else if(length < 5){
				cut(temp[0],null,temp[2]);
			}
			else if(length % 5 == 0){
				for(int i = 0 ; i < length ; i+=5){
					cut(temp[i],temp[i+4],temp[i+2]);
				}
			}
		}
	}
	/**
	 * �жϿα������Ƿ��е�˫��
	 * @author sail
	 * */
	private int selectWeek(String string){
		int flag;
		// �ж��Ƿ��е�˫��
		if(string.indexOf("����") != -1){
			flag = 1;
		}
		else if(string.indexOf("˫��") != -1){
			flag = 2;
		}else
			flag = 0;
		return flag;
	}
	/** 
	 * �α����ݾ���ָ�
	 * @author sail
	 * */

	private void cut(String Cname,String ClassRoom,String string){
		String temp = string;
		// ��ſγ���Ϣ��String
		int m = 0,col = 0,start = 0,end = 0,numLength = 0;
		// ȷ���ܼ�

		while(string.indexOf(weekdays[m]) == -1){ //�����ַ�ƥ��
			m += 1;

		}
		col = m + 1;
		// ��temp[2]��������
		try{
			int a = Integer.parseInt(string.substring
					(string.indexOf("��")+1,string.indexOf(",")));
			int b = Integer.parseInt(string.substring
					(string.lastIndexOf(",")+1,string.indexOf("��")));
			string = string.substring(string.indexOf("{"),string.indexOf("}"));
			start = Integer.parseInt(string.substring
					(string.indexOf("��")+1,string.indexOf("-")));
			end = Integer.parseInt(string.substring
					(string.indexOf("-")+1,string.indexOf("��")));

			numLength = b - a + 1;

			// д�����ݿ�
			gsData_from(Cname,ClassRoom,col,a,numLength,start,end,selectWeek(string));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * �õ��α����ݲ�д�����ݿ�
	 * @author sail
	 * */
	public boolean gsData_from(String Cname,String ClassRoom,int col,int row,int length,int start,int end,int flag){
		//����һ��ContenValues���󣬴洢����  
		ContentValues contentValues = new ContentValues();  
		//��������䵽ContentValues�����У�ContentValues�����еļ���ֵ��Ӧ���ݿ��е��к�ֵ��ֵ�����������е�������ͬ��  
		contentValues.put("Cname", Cname);  
		contentValues.put("ClassRoom", ClassRoom);
		contentValues.put("col", col);  
		contentValues.put("row", row); 
		contentValues.put("length", length);  
		contentValues.put("start", start);  
		contentValues.put("end", end); 
		contentValues.put("Semester",LocalInforM.Semester);
		contentValues.put("flag", flag);
		//������д��SQLiteDatabase����  
		SQLiteDatabase db = helper.getWritableDatabase();  
		//����insert����д�����ݿ�.��һ������Ϊ�������ڶ���Ϊnull������������ΪCntentValues����  
		db.insert("schedule", null, contentValues);

		return false;
	}
	/**
	 * �����ݿ��ÿα����ݣ�������װ��
	 * @author sail
	 * */
	public ArrayList<ClassInfo> getClassInfo_from_db(){
		//		 helper = new mySQLiteHelper(context,name);
		ClassInfo classInfo;
		//����ֻ����SQLiteDatabase����  
		SQLiteDatabase db = helper.getReadableDatabase();  
		//����SQLitedatabase��query������ȡ�������������Cursor�����С�  
		//query�����ĵ�һ�������Ǳ���  
		//�ڶ���������ʹ��String������������һ������ռ��һ��Ԫ��  
		//������������SQL����е�Where��������䣬���еģ���Ӧ���ĸ�������String���顣�������м����ַ����м����ʺš�  
		//������������Where������  
		//���ĸ���������  
		//�����������SQL�е�having  
		//����������������  
		Cursor cursor = db.query("schedule", null,null, null, null, null, null);  
		//ʹ��cursor.moveToNext()���α�����һ�С��α�Ĭ���ڵ�һ�е���һ�С�  
		while (cursor.moveToNext()) {  
			//ʹ��GetString��ȡ���е�ֵ������Ϊʹ��cursor.getColumnIndex("name")��ȡ����š�  
			String Cname = cursor.getString(cursor.getColumnIndex("Cname"));  
			String  ClassRoom = cursor.getString(cursor.getColumnIndex("ClassRoom"));  
			int col = cursor.getInt(cursor.getColumnIndex("col"));
			int row = cursor.getInt(cursor.getColumnIndex("row"));
			int length = cursor.getInt(cursor.getColumnIndex("length"));
			int start = cursor.getInt(cursor.getColumnIndex("start"));
			int end = cursor.getInt(cursor.getColumnIndex("end"));
			int flag = cursor.getInt(cursor.getColumnIndex("flag"));
			// ���ص�ǰ��Ӧ�ϵĿ�
			// �����Ͽ�ʱ��
			if(start <= LocalInforM.curWeek && LocalInforM.curWeek <= end){
				// �жϵ�˫��
				if(flag == 0 || ((LocalInforM.curWeek % 2 == 0)&&flag == 2)
						|| (LocalInforM.curWeek % 2 != 0) && flag == 1){
					classInfo = new ClassInfo();
					classInfo.setClassname(Cname);
					classInfo.setWeekday(col);
					classInfo.setClassNumLen(length);
					classInfo.setClassRoom(ClassRoom);
					classInfo.setFromClassNum(row);
					list.add(classInfo);}
			}
		}  
		return list;
	}
	/** 
	 * �жϿα����ݿ��Ƿ�Ϊ�� 
	 * @return boolean
	 * @author sail
	 * @param 
	 * */
	public boolean getEmpty(String str){
		try{
			// ����һ��ֻ������
			SQLiteDatabase db = helper.getReadableDatabase();  
			// ����Cursor
			Cursor cursor = db.query(str, null,null, null, null, null, null);
			// ������һ��,����flase��Ϊ��
			return cursor.moveToNext();
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * �����ݿ��ȡ��ǰid�����Ŀα�
	 * @author sail
	 * */
	public ArrayList<ClassInfo> getClassInfo_form_db(int id){
		return list;
	}
	/**
	 * �������ݿ�
	 * @author sail
	 * */
	public void CreatSql(){
		helper.getWritableDatabase();
	}
	/**
	 * ɾ�����ݿ���ָ���ı�
	 * @author sail
	 * */
	public void DeleteTable(String name){
		// ɾ��sql���
		String deleteString = "delete from "+name;
		// ��ö�дȨ��
		SQLiteDatabase db = helper.getWritableDatabase();
		// ɾ��ָ����
		db.execSQL(deleteString);
	}

	/** 
	 * ɾ���ɼ����е��ظ�ֵ
	 * @author sail
	 * */
	private void DeleteMore(){
		String del = "delete from grade"+
				"  where CcName  in (select  CcName  from grade  group  by  CcName  having  count(CcName) > 1)"
				+"and rowid not in (select min(rowid) from  grade  group by CcName  having count(CcName)>1)";	
		// ��ö�дȨ��
		SQLiteDatabase db = helper.getWritableDatabase();
		// ɾ��ָ����
		db.execSQL(del);
	}
	/**��
	 * ���ɼ�����д�����ݿ�
	 * @author sail
	 * */
	private void savaGrades() throws Exception{
		int i=0;
		List<String> listGrade = LocalInforM.gradedata;
		for(String grade : listGrade){
			i++;
			// ��Ҫǰ��������
			if(i > 4){
				//�����򰴿ո�ֶ�
				String[] temp = grade.split(" ");

				//����һ��ContenValues���󣬴洢����  
				ContentValues contentValues = new ContentValues();  
				//��������䵽ContentValues�����У�ContentValues�����еļ���ֵ��Ӧ���ݿ��е��к�ֵ��ֵ�����������е�������ͬ��  
				contentValues.put("AcademicYear",temp[0]);  
				contentValues.put("Academic", temp[1]);
				contentValues.put("CcName", temp[3]);  
				contentValues.put("charCourse", temp[4]); 
				contentValues.put("credit",temp[6]); 
				contentValues.put("oachievement",temp[7]);  
				contentValues.put("midgrade",temp[8]);  
				contentValues.put("termexamination", temp[9]); 
				contentValues.put("Stest", temp[10]); 
				contentValues.put("grade",temp[11]);
				//������д��SQLiteDatabase����  
				SQLiteDatabase db = helper.getWritableDatabase();  
				//����insert����д�����ݿ�.��һ������Ϊ�������ڶ���Ϊnull������������ΪCntentValues����  
				db.insert(GRTABLENAME, null, contentValues);
			}
		}
	}
	/**
	 * �����ݿ����ָ���ĳɼ�
	 * @return Map<String,List<String>>
	 * @author sail
	 * @param ѧ��,ѧ��
	 * */
	public Map<String,List<String>> getGrade(String str1,String str2){
		DeleteMore();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		// // ѧ��,ѧ��,�γ���,�γ�����,ѧ��,���гɼ�,ƽʱ�ɼ�,ʵ��ɼ�,��ĩ�ɼ�,����
		ArrayList<String> nameList,charCourseList,creditList,midList,usuaList
		,setList,endList,gredeList;
		try{
			SQLiteDatabase db = helper.getReadableDatabase();  
			Cursor cr = db.query("grade", null,null, null, null, null, null); 
			nameList = new ArrayList<String>(cr.getCount());
			charCourseList = new ArrayList<String>(cr.getCount());
			creditList = new ArrayList<String>(cr.getCount());
			midList = new ArrayList<String>(cr.getCount());
			usuaList = new ArrayList<String>(cr.getCount());
			setList = new ArrayList<String>(cr.getCount());
			endList = new ArrayList<String>(cr.getCount());
			gredeList = new ArrayList<String>(cr.getCount());
			int i = 0;
			//ʹ��cursor.moveToNext()���α�����һ�С��α�Ĭ���ڵ�һ�е���һ�С�  
			while (cr.moveToNext()) {  
				String Academic = cr.getString(cr.getColumnIndex("Academic"));
				String AcademicYear = cr.getString(cr.getColumnIndex("AcademicYear"));
				if(Academic.equals(str2) && AcademicYear.equals(str1)) {
					nameList.add(i,cr.getString(cr.getColumnIndex("CcName")));
					charCourseList.add(i,cr.getString(cr.getColumnIndex("charCourse")));
					creditList.add(i,cr.getString(cr.getColumnIndex("credit")));
					midList.add(i,cr.getString(cr.getColumnIndex("midgrade")));
					usuaList.add(i,cr.getString(cr.getColumnIndex("oachievement")));
					setList.add(i,cr.getString(cr.getColumnIndex("Stest")));
					endList.add(i,cr.getString(cr.getColumnIndex("termexamination")));
					gredeList.add(i,cr.getString(cr.getColumnIndex("grade")));
					i++;
				}
			}
			map.put("�γ���", nameList);
			map.put("����", charCourseList);
			map.put("ѧ��", creditList);
			map.put("���гɼ�", midList);
			map.put("ƽʱ�ɼ�", usuaList);
			map.put("ʵ��ɼ�", setList);
			map.put("��ĩ�ɼ�", endList);
			map.put("����", gredeList);

			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
