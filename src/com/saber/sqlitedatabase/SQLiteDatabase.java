package com.saber.sqlitedatabase;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class SQLiteDatabase extends Activity implements AdapterView.OnItemClickListener {
	private BooksDB  mBooksDB;
	private Cursor   mCursor;
	private EditText BookName;
	private EditText BookAuthor;
	private ListView BooksList;
	private static final String ACTIVITY_TAG="SQLiteDatabase";
	
	private int BOOK_ID = 0;
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_DELETE = Menu.FIRST + 1;
	protected final static int MENU_UPDATE = Menu.FIRST + 2;
	protected final static int MENU_FINDAUTHOR = Menu.FIRST + 3;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpViews();
    }
    
    public void setUpViews(){
    	mBooksDB = new BooksDB(this);
    	mCursor  = mBooksDB.select();
    	
    	BookName = (EditText)findViewById(R.id.bookname);
    	BookAuthor = (EditText)findViewById(R.id.author);
    	BooksList = (ListView)findViewById(R.id.bookslist);
    	
    	BooksList.setAdapter(new BooksListAdapter(this, mCursor));
    	BooksList.setOnItemClickListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
   	
        menu.add(Menu.NONE, MENU_ADD, 0, "ADD");
        menu.add(Menu.NONE, MENU_DELETE, 0, "DELETE");
        menu.add(Menu.NONE, MENU_UPDATE, 0, "UPDATE");
		menu.add(Menu.NONE, MENU_FINDAUTHOR, 0, "FINDAUTHOR"); //Saberhao add
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
      super.onOptionsItemSelected(item);
      switch (item.getItemId())
      {
		case MENU_ADD:
			add();
			break;
		case MENU_DELETE:
			delete();
			break;
		case MENU_UPDATE:
			update();
		    break;
		case MENU_FINDAUTHOR:
			findauthor();
		    break;
      }
      return true;
    }
    
    public void add(){
    	String bookname = BookName.getText().toString();
    	String author  = BookAuthor.getText().toString();
    	//书名和作者都不能为空，或者退出
    	if (bookname.equals("") || author.equals("")){
    		Toast.makeText(this, "Add Fail,bookname and author can not be NULL!", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	 mBooksDB.insert(bookname, author);
    	 mCursor.requery();
    	 BooksList.invalidateViews();
    	 BookName.setText("");
    	 BookAuthor.setText("");
    	 Toast.makeText(this, "Add Successed!", Toast.LENGTH_SHORT).show();
    }
    
    public void delete(){
		if (BOOK_ID == 0) {
			return;
		}
		mBooksDB.delete(BOOK_ID);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Delete Successed!", Toast.LENGTH_SHORT).show();
    }
    
    public void update(){
    	String bookname = BookName.getText().toString();
    	String author  = BookAuthor.getText().toString();
    	//书名和作者都不能为空，或者退出
    	if (bookname.equals("") || author.equals("")){
    		return;
    	}
    	mBooksDB.update(BOOK_ID, bookname, author);
    	mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Update Successed!", Toast.LENGTH_SHORT).show();
    }
	
	public void findauthor(){
    	String bookname = BookName.getText().toString();
		
    	//find out the book author according to the book name
    	if (bookname.equals("")){
			Toast.makeText(this, "Find author Fail,Plz input book name", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	Log.v(ACTIVITY_TAG, "I have find the author");
    	String[] BookNameSet = {bookname};
    	String authername = mBooksDB.findAuthor(BookNameSet);

		if(!authername.equals("NoThisBook"))
		{
			Toast.makeText(this, "The author of " + bookname + " : " + authername, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "No this Book,plz check again!", Toast.LENGTH_SHORT).show();	
		}
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		mCursor.moveToPosition(position);
		BOOK_ID = mCursor.getInt(0);	
		BookName.setText(mCursor.getString(1));
		BookAuthor.setText(mCursor.getString(2));
		
	}
    
    public class BooksListAdapter extends BaseAdapter{
    	private Context mContext;
    	private Cursor mCursor;
    	public BooksListAdapter(Context context,Cursor cursor) {
			
    		mContext = context;
    		mCursor = cursor;
		}
		@Override
		public int getCount() {
			return mCursor.getCount();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView mTextView = new TextView(mContext);
			mCursor.moveToPosition(position);
			mTextView.setText("Book Name : " + mCursor.getString(1) + "   Author Name : " + mCursor.getString(2));
			return mTextView;
		}
    	
    }
}