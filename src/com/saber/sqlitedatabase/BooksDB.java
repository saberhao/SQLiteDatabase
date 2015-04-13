package com.saber.sqlitedatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class BooksDB extends SQLiteOpenHelper {
	  private final static String DATABASE_NAME = "BOOKS.db";
	  private final static int DATABASE_VERSION = 1;
	  private final static String TABLE_NAME = "books_table";
	  public final static String BOOK_ID = "book_id";
	  public final static String BOOK_NAME = "book_name";
	  public final static String BOOK_AUTHOR = "book_author";
	  private static final String ACTIVITY_TAG="BooksDB";
	  
	  public BooksDB(Context context) {
		// TODO Auto-generated constructor stub
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	//创建table
	@Override
	public void onCreate(SQLiteDatabase db) {
		  String sql = "CREATE TABLE " + TABLE_NAME + " (" + BOOK_ID
	        + " INTEGER primary key autoincrement, " + BOOK_NAME + " text, "+  BOOK_AUTHOR +" text);";
	      db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
	
	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	//增加操作
	 public long insert(String bookname,String author)
	  {
	    SQLiteDatabase db = this.getWritableDatabase();
	    /* ContentValues */
	    ContentValues cv = new ContentValues();
	    cv.put(BOOK_NAME, bookname);
	    cv.put(BOOK_AUTHOR, author);
	    long row = db.insert(TABLE_NAME, null, cv);
	    return row;
	  }
	 //删除操作
	  public void delete(int id)
	  {
	    SQLiteDatabase db = this.getWritableDatabase();
	    String where = BOOK_ID + " = ?";
	    String[] whereValue ={ Integer.toString(id) };
	    db.delete(TABLE_NAME, where, whereValue);
	  }
	  //修改操作
	  public void update(int id, String bookname,String author)
	  {
	    SQLiteDatabase db = this.getWritableDatabase();
	    String where = BOOK_ID + " = ?";
	    String[] whereValue = { Integer.toString(id) };
	  
	    ContentValues cv = new ContentValues();
	    cv.put(BOOK_NAME, bookname);
	    cv.put(BOOK_AUTHOR, author);
	    db.update(TABLE_NAME, cv, where, whereValue);
	  }
	  public String findAuthor(String[] bookname)
	  {
	  	String author = "NoThisBook";
	  	String[] BookInfo = {BOOK_ID,BOOK_NAME,BOOK_AUTHOR};
		String where = BOOK_NAME + " = ?";
	    String[] whereValue = bookname;
	    SQLiteDatabase db = this.getReadableDatabase();
		//make the cursor to find out the author
		Cursor cursor = db
				.query(TABLE_NAME, BookInfo, where, whereValue, null, null, null);

		//Can not find the author
		if (cursor.getCount() == 0) {
            cursor.close(); 
			return author;	
		}
		Log.v(ACTIVITY_TAG, "I can find "+ cursor.getCount() + " author(s)");
	    
		// cursor must move to first index, otherwise it will out of index
        if(cursor.moveToFirst())
        {
    	    int authorIndex = cursor.getColumnIndex("book_author");
    	    Log.v(ACTIVITY_TAG, "authorIndex =  "+ authorIndex);
    		author = cursor.getString(authorIndex);
    		Log.v(ACTIVITY_TAG, "author =  "+ author);  	
        }
		return author;
	  }
}
