package three.com.materialdesignexample.Db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/10/20.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private Context mcontext=null;

    public static final String CREATE_NEWS ="create table News ("
            + "id integer primary key autoincrement,"
            + "path text,"
            + "title text,"
            + "content text)";

    public static final String CREATE_COURSE ="create table Course ("
            + "id integer primary key autoincrement,"
            + "Count integer,"
            + "CourseName text,"
            + "Number text,"
            + "Week text,"
            + "Teacher text,"
            + "Classroom text,"
            + "Category text,"
            + "Time text)";

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
        db.execSQL(CREATE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
