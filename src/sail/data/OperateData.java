package sail.data;

/**
 * 操作数据库,及数据解析处理类
 * @ClassName operatedata
 * @author sail
 * @date 2014.12.12 晚上9:23
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
	private String[] weekdays = {"周一","周二","周三","周四","周五","周六","周日"};  // 判断周几
	// 课表数据
	private ArrayList<ClassInfo> list = new ArrayList<ClassInfo>();
	private Context context;
	// 初始化SQLhelper类
	private mySQLiteHelper helper ;
	public OperateData(Context context){
		this.context = context;
		helper = new mySQLiteHelper(context,DBNAME);

		switch (LocalInforM.flag) {
		case LocalInforM.GETSCHDE:
			// 删除旧表
			DeleteTable(SCTABLENAME);
			mydealScheduleData(LocalInforM.scheduledata);
			break;
		case LocalInforM.GETGRADE:
			// 删除旧表
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
	 * 课表数据具体拆解方法
	 * @author sail
	 * */
	private void mydealScheduleData(List<String> dataList){
		for(String string : dataList){

			// 按空格分段
			String[] temp = string.split(" ");
			// 获取正则后的string数组长度
			int length = temp.length;
			// 若等于5则是一般情况，需要的处理的数据则存放在temp[2]之中
			// temp[0],temp[3],temp[4]分别为课程名，教师，教室
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
	 * 判断课表数据是否含有单双周
	 * @author sail
	 * */
	private int selectWeek(String string){
		int flag;
		// 判断是否含有单双周
		if(string.indexOf("单周") != -1){
			flag = 1;
		}
		else if(string.indexOf("双周") != -1){
			flag = 2;
		}else
			flag = 0;
		return flag;
	}
	/** 
	 * 课表数据具体分割
	 * @author sail
	 * */

	private void cut(String Cname,String ClassRoom,String string){
		String temp = string;
		// 存放课程信息的String
		int m = 0,col = 0,start = 0,end = 0,numLength = 0;
		// 确定周几

		while(string.indexOf(weekdays[m]) == -1){ //进行字符匹配
			m += 1;

		}
		col = m + 1;
		// 对temp[2]继续操作
		try{
			int a = Integer.parseInt(string.substring
					(string.indexOf("第")+1,string.indexOf(",")));
			int b = Integer.parseInt(string.substring
					(string.lastIndexOf(",")+1,string.indexOf("节")));
			string = string.substring(string.indexOf("{"),string.indexOf("}"));
			start = Integer.parseInt(string.substring
					(string.indexOf("第")+1,string.indexOf("-")));
			end = Integer.parseInt(string.substring
					(string.indexOf("-")+1,string.indexOf("周")));

			numLength = b - a + 1;

			// 写入数据库
			gsData_from(Cname,ClassRoom,col,a,numLength,start,end,selectWeek(string));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 得到课表数据并写入数据库
	 * @author sail
	 * */
	public boolean gsData_from(String Cname,String ClassRoom,int col,int row,int length,int start,int end,int flag){
		//创建一个ContenValues对象，存储数据  
		ContentValues contentValues = new ContentValues();  
		//把数据填充到ContentValues对象中，ContentValues对象中的键和值对应数据库中的列和值。值必须与数据列的类型相同。  
		contentValues.put("Cname", Cname);  
		contentValues.put("ClassRoom", ClassRoom);
		contentValues.put("col", col);  
		contentValues.put("row", row); 
		contentValues.put("length", length);  
		contentValues.put("start", start);  
		contentValues.put("end", end); 
		contentValues.put("Semester",LocalInforM.Semester);
		contentValues.put("flag", flag);
		//创建可写的SQLiteDatabase对象  
		SQLiteDatabase db = helper.getWritableDatabase();  
		//调用insert方法写入数据库.第一个参数为表名，第二个为null，第三个参数为CntentValues对象。  
		db.insert("schedule", null, contentValues);

		return false;
	}
	/**
	 * 从数据库获得课表数据，并进行装载
	 * @author sail
	 * */
	public ArrayList<ClassInfo> getClassInfo_from_db(){
		//		 helper = new mySQLiteHelper(context,name);
		ClassInfo classInfo;
		//创建只读的SQLiteDatabase对象  
		SQLiteDatabase db = helper.getReadableDatabase();  
		//调用SQLitedatabase的query方法获取检索结果并放入Cursor对象中。  
		//query方法的第一个参数是表名  
		//第二个参数是使用String数组存放列名，一个列明占用一个元素  
		//第三个参数是SQL语句中的Where条件子语句，其中的？对应第四个参数中String数组。数组中有几个字符就有几个问号。  
		//第三个参数是Where的条件  
		//第四个参数分组  
		//第五个参数是SQL中的having  
		//第六个参数是排序  
		Cursor cursor = db.query("schedule", null,null, null, null, null, null);  
		//使用cursor.moveToNext()把游标下移一行。游标默认在第一行的上一行。  
		while (cursor.moveToNext()) {  
			//使用GetString获取列中的值。参数为使用cursor.getColumnIndex("name")获取的序号。  
			String Cname = cursor.getString(cursor.getColumnIndex("Cname"));  
			String  ClassRoom = cursor.getString(cursor.getColumnIndex("ClassRoom"));  
			int col = cursor.getInt(cursor.getColumnIndex("col"));
			int row = cursor.getInt(cursor.getColumnIndex("row"));
			int length = cursor.getInt(cursor.getColumnIndex("length"));
			int start = cursor.getInt(cursor.getColumnIndex("start"));
			int end = cursor.getInt(cursor.getColumnIndex("end"));
			int flag = cursor.getInt(cursor.getColumnIndex("flag"));
			// 返回当前周应上的课
			// 过滤上课时间
			if(start <= LocalInforM.curWeek && LocalInforM.curWeek <= end){
				// 判断单双周
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
	 * 判断课表数据库是否为空 
	 * @return boolean
	 * @author sail
	 * @param 
	 * */
	public boolean getEmpty(String str){
		try{
			// 声明一个只读对象
			SQLiteDatabase db = helper.getReadableDatabase();  
			// 构造Cursor
			Cursor cursor = db.query(str, null,null, null, null, null, null);
			// 访问下一行,返回flase则为空
			return cursor.moveToNext();
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 从数据库获取当前id周数的课表
	 * @author sail
	 * */
	public ArrayList<ClassInfo> getClassInfo_form_db(int id){
		return list;
	}
	/**
	 * 创建数据库
	 * @author sail
	 * */
	public void CreatSql(){
		helper.getWritableDatabase();
	}
	/**
	 * 删除数据库中指定的表
	 * @author sail
	 * */
	public void DeleteTable(String name){
		// 删除sql语句
		String deleteString = "delete from "+name;
		// 获得读写权限
		SQLiteDatabase db = helper.getWritableDatabase();
		// 删除指定表
		db.execSQL(deleteString);
	}

	/** 
	 * 删除成绩表中的重复值
	 * @author sail
	 * */
	private void DeleteMore(){
		String del = "delete from grade"+
				"  where CcName  in (select  CcName  from grade  group  by  CcName  having  count(CcName) > 1)"
				+"and rowid not in (select min(rowid) from  grade  group by CcName  having count(CcName)>1)";	
		// 获得读写权限
		SQLiteDatabase db = helper.getWritableDatabase();
		// 删除指定表
		db.execSQL(del);
	}
	/**　
	 * 将成绩数据写入数据库
	 * @author sail
	 * */
	private void savaGrades() throws Exception{
		int i=0;
		List<String> listGrade = LocalInforM.gradedata;
		for(String grade : listGrade){
			i++;
			// 不要前三个数据
			if(i > 4){
				//　正则按空格分段
				String[] temp = grade.split(" ");

				//创建一个ContenValues对象，存储数据  
				ContentValues contentValues = new ContentValues();  
				//把数据填充到ContentValues对象中，ContentValues对象中的键和值对应数据库中的列和值。值必须与数据列的类型相同。  
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
				//创建可写的SQLiteDatabase对象  
				SQLiteDatabase db = helper.getWritableDatabase();  
				//调用insert方法写入数据库.第一个参数为表名，第二个为null，第三个参数为CntentValues对象。  
				db.insert(GRTABLENAME, null, contentValues);
			}
		}
	}
	/**
	 * 从数据库读出指定的成绩
	 * @return Map<String,List<String>>
	 * @author sail
	 * @param 学年,学期
	 * */
	public Map<String,List<String>> getGrade(String str1,String str2){
		DeleteMore();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		// // 学年,学期,课程名,课程性质,学分,期中成绩,平时成绩,实验成绩,期末成绩,总评
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
			//使用cursor.moveToNext()把游标下移一行。游标默认在第一行的上一行。  
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
			map.put("课程名", nameList);
			map.put("属性", charCourseList);
			map.put("学分", creditList);
			map.put("期中成绩", midList);
			map.put("平时成绩", usuaList);
			map.put("实验成绩", setList);
			map.put("期末成绩", endList);
			map.put("总评", gredeList);

			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
