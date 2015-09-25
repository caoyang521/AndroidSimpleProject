package three.com.phoneservice.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/9/24.
 */
public class DbHelper extends SQLiteOpenHelper {

    private Context context=null;

    public static final String CREATE_PERSON ="create table Person ("
            + "id integer primary key autoincrement,"
            + "Number text,"
            + "Name text)";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
