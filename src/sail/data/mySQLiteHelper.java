package sail.data;
/**
 * 数据库Helper
 * @ClassName mySQLiterHelper
 * @author sail
 * @date 2014/12/8 21:00
 * */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class mySQLiteHelper extends SQLiteOpenHelper {
	// 最低版本号
	private static final int VERSION = 1;
	// 数据库创建语句
	// 数据，列号(日期),行号,课长,开始周数,结束周数,单双周
	// 学年,学期,课程名,课程性质,学分,期中成绩,平时成绩,实验成绩,期末成绩,总评
	final String CREATE_GRADE_SQL="create table grade(AcademicYear varchar(20),Academic varchar(2),CcName varchar(20)" +
			",charCourse varchar(6),credit varchar(4),midgrade varchar(4),oachievement varchar(4),Stest varchar(4),termexamination varchar(4),grade varchar(4))";
	final String CREATE_SCHEDULE_SQL = "create table schedule(Cname varchar(20),ClassRoom varchar(60),col int" +
			",row int,length int,start int,end int,Semester char(20),flag int)";
	// 重写构造函数
	 public mySQLiteHelper(Context context , String DBname ,CursorFactory factory , int version)  
     {  
            super(context, DBname,factory, version);  
     }  
	 // 三变量
	 public mySQLiteHelper(Context context , String DBname ,int version ){
		 this(context,DBname,null,version);
	 }
	 // 二变量SQLiteHelper
	 public mySQLiteHelper(Context context , String DBname){
		 this(context,DBname,null,VERSION);
	 }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		// 首次使用的时候创建表
//		if(LocalInforM.flag == LocalInforM.GETSCHDE)
//			db.execSQL(CREATE_SCHEDULE_SQL);
//		else if(LocalInforM.flag == LocalInforM.GETGRADE)
//			db.execSQL(CREATE_GRADE_SQL);
//		else
//		{
			db.execSQL(CREATE_SCHEDULE_SQL);	
			db.execSQL(CREATE_GRADE_SQL);
//		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
//		onCreate(db);
	}
	
}
