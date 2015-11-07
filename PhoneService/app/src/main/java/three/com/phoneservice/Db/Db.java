package three.com.phoneservice.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import three.com.phoneservice.CallBack;
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

//    public void savePerson(PeopleInfo peopleInfo){
//        if(peopleInfo!=null){
//            ContentValues values =new ContentValues();
//            values.put("peopleName", peopleInfo.getPhoneNumber());
//            values.put("phoneNumber", peopleInfo.getPeopleName());
//            Sqldb.insert("Person", null, values);
//        }
//    }

    public void savePerson(JSONArray jsonArray, ArrayList<PeopleInfo> phoneInfos, CallBack callBack) {

        JSONObject peopleObj ;

        try {
            Sqldb.beginTransaction();
            phoneInfos.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                peopleObj = jsonArray.getJSONObject(i);

                PeopleInfo peopleInfo = new PeopleInfo();
                peopleInfo.setPeopleName(peopleObj.getString("学生姓名"));
                peopleInfo.setPhoneNumber(peopleObj.getString("手机短号"));
                peopleInfo.setClassName(peopleObj.getString("班级名称"));
                peopleInfo.setSchoolNumber(peopleObj.getString("学号"));
                phoneInfos.add(peopleInfo);
                if (peopleInfo != null) {
                    ContentValues values = new ContentValues();
                    values.put("phoneNumber", peopleInfo.getPhoneNumber());
                    values.put("peopleName", peopleInfo.getPeopleName());
                    values.put("schoolNumber",peopleInfo.getSchoolNumber());
                    values.put("className",peopleInfo.getClassName());
                    Sqldb.insert("Person", null, values);
                }

            }
            callBack.onFinsh(null);
            Sqldb.setTransactionSuccessful();

        } catch (JSONException e) {

            e.printStackTrace();
        }
        finally {
            Sqldb.endTransaction();
        }



    }

    public PeopleInfo loadPersonByNumber(String number){
        Cursor cursor= Sqldb.query("Person", null, "Number=?", new String[]{number}, null, null, null);
        PeopleInfo peopleInfo =new PeopleInfo();
        if(cursor.moveToFirst()){
            do{
                peopleInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                peopleInfo.setPeopleName(cursor.getString(cursor.getColumnIndex("peopleName")));
                peopleInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
                peopleInfo.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                peopleInfo.setSchoolNumber(cursor.getString(cursor.getColumnIndex("schoolNumber")));
            }while(cursor.moveToNext());
        }
        else
            return null;

        if(cursor!=null){
            cursor.close();
        }
        return peopleInfo;
    }

    public boolean loadPhoneInfo(ArrayList<PeopleInfo> phoneInfos,CallBack callBack) {
        Cursor cursor= Sqldb.query("Person", null, null,null, null, null, null);
        int flag=0;
        phoneInfos.clear();
        if(cursor.moveToFirst()){
            callBack.onStart();
            do{
                flag=1;

                PeopleInfo phoneInfo=new PeopleInfo();
                phoneInfo.setSchoolNumber(cursor.getString(cursor.getColumnIndex("schoolNumber")));
                phoneInfo.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                phoneInfo.setPeopleName(cursor.getString(cursor.getColumnIndex("peopleName")));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));

                phoneInfos.add(phoneInfo);
            }while(cursor.moveToNext());

            if(cursor!=null){
                cursor.close();
            }
            if(flag==1){
                callBack.onFinsh(null);
                return true;
            }

        }
        return false;
    }

    public boolean  loadPhoneInfoByName(String name, ArrayList<PeopleInfo> phoneInfos) {
        Cursor cursor= Sqldb.query("Person", null, "peopleName like ?",new String[]{"%"+String.valueOf(name)+"%"}, null, null, null);
        int flag=0;
        phoneInfos.clear();
        if(cursor.moveToFirst()){
            do{
                flag=1;

                PeopleInfo phoneInfo=new PeopleInfo();
                phoneInfo.setSchoolNumber(cursor.getString(cursor.getColumnIndex("schoolNumber")));
                phoneInfo.setClassName(cursor.getString(cursor.getColumnIndex("className")));
                phoneInfo.setPeopleName(cursor.getString(cursor.getColumnIndex("peopleName")));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));

                phoneInfos.add(phoneInfo);
            }while(cursor.moveToNext());

            if(cursor!=null){
                cursor.close();
            }
            if(flag==1){
                return true;
            }

        }
        return false;
    }

    public void clear(String tableName){
        Sqldb.execSQL("DELETE FROM " +tableName);
    }

}
