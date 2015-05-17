package sail.data;
/**
 * ���ݿ�Helper
 * @ClassName mySQLiterHelper
 * @author sail
 * @date 2014/12/8 21:00
 * */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class mySQLiteHelper extends SQLiteOpenHelper {
	// ��Ͱ汾��
	private static final int VERSION = 1;
	// ���ݿⴴ�����
	// ���ݣ��к�(����),�к�,�γ�,��ʼ����,��������,��˫��
	// ѧ��,ѧ��,�γ���,�γ�����,ѧ��,���гɼ�,ƽʱ�ɼ�,ʵ��ɼ�,��ĩ�ɼ�,����
	final String CREATE_GRADE_SQL="create table grade(AcademicYear varchar(20),Academic varchar(2),CcName varchar(20)" +
			",charCourse varchar(6),credit varchar(4),midgrade varchar(4),oachievement varchar(4),Stest varchar(4),termexamination varchar(4),grade varchar(4))";
	final String CREATE_SCHEDULE_SQL = "create table schedule(Cname varchar(20),ClassRoom varchar(60),col int" +
			",row int,length int,start int,end int,Semester char(20),flag int)";
	// ��д���캯��
	 public mySQLiteHelper(Context context , String DBname ,CursorFactory factory , int version)  
     {  
            super(context, DBname,factory, version);  
     }  
	 // ������
	 public mySQLiteHelper(Context context , String DBname ,int version ){
		 this(context,DBname,null,version);
	 }
	 // ������SQLiteHelper
	 public mySQLiteHelper(Context context , String DBname){
		 this(context,DBname,null,VERSION);
	 }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		// �״�ʹ�õ�ʱ�򴴽���
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
