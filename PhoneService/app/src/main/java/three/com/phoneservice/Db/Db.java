package three.com.phoneservice.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import three.com.phoneservice.Model.PeopleInfo;


/**
 * Created by Administrator on 2015/9/24.
 */
public class Db {

    public static final String DB_NAME="person.db";
    public static final int VERSION=1;
    private static Db db=null;
    private SQLiteDatabase Sqldb;

    private Db(Context context) {
        DbHelper dbHelper=new DbHelper(context,DB_NAME , null, VERSION);
        Sqldb=dbHelper.getWritableDatabase();
    }

    public synchronized static Db getInstance(Context context){
        if(db==null){
            db=new Db(context);
        }
        return db;
    }

    public void savePerson(PeopleInfo peopleInfo){
        if(peopleInfo!=null){
            ContentValues values =new ContentValues();
            values.put("Number", peopleInfo.getNumber());
            values.put("Name", peopleInfo.getName());
            Sqldb.insert("Person", null, values);
        }
    }

    public PeopleInfo loadPerson(String number){
        Cursor cursor= Sqldb.query("Person", null, "Number=?", new String[]{number}, null, null, null);
        PeopleInfo peopleInfo =new PeopleInfo();
        if(cursor.moveToFirst()){
            do{
                peopleInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                peopleInfo.setName(cursor.getString(cursor.getColumnIndex("Name")));
                peopleInfo.setNumber(cursor.getString(cursor.getColumnIndex("Number")));
            }while(cursor.moveToNext());
        }
        else
            return null;

        if(cursor!=null){
            cursor.close();
        }
        return peopleInfo;
    }
}
