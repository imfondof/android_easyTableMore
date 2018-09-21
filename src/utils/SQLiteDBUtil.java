package utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBUtil extends SQLiteOpenHelper {

	public static final String NAME = "easytablemore";
	public static final int VERSION = 2;//因为增加一个数据库，所以更新版本号为2

	/**
	 * content上下文对象 name数据库名称 factory数据库工厂 version版本
	 * 
	 * @param context
	 */
	public SQLiteDBUtil(Context context) {
		super(context, NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	//
	@Override
	public void onCreate(SQLiteDatabase db) {

		String userSQL = "create table kebiao"
				+ "(id integer primary key autoincrement,"
				+ "account varchar(20)," + "courseId integer,"
				+ "name varchar(20)," + "teacher varchar(20),"
				+ "place varchar(20)," + "term integer," + "week varchar(20))";
		db.execSQL(userSQL);
		
		//添加新的表   需要在更新这里写，，，而且需要更新版本号
		String noteSQL = "create table note"
				+ "(id integer primary key autoincrement,"
				+ "account varchar(20)," + "title varchar(20),"
				+ "course varchar(20)," + "content varchar(5000),"
				+ "time date)";
		db.execSQL(noteSQL);

		// CREATE TABLE kebiao(id integer primary key autoincrement,keyaccount
		// varchar(20),courseId integer,name varchar(20),teacher
		// varchar(20),place varchar(20),term integer,week varchar(20));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//添加新的表   需要在更新这里写，，，而且需要更新版本号
		String noteSQL = "create table note"
				+ "(id integer primary key autoincrement,"
				+ "account varchar(20)," + "title varchar(20),"
				+ "course varchar(20)," + "content varchar(5000),"
				+ "time date)";
		db.execSQL(noteSQL);

	}

}
