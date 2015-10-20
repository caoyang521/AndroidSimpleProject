package three.com.materialdesignexample.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import three.com.materialdesignexample.Models.Course;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.Util.HandleResponseUtil;

/**
 * Created by Administrator on 2015/10/20.
 */
public class Db {

    public static final String DB_NAME="school_info.db";
    public static final int VERSION=1;
    private static Db db;
    private SQLiteDatabase sqlDb;

    private Db(Context context) {
        DbOpenHelper dbHelper=new DbOpenHelper(context,DB_NAME , null, VERSION);
        sqlDb=dbHelper.getWritableDatabase();
    }

    public synchronized static Db getInstance(Context context){
        if(db==null){
            db=new Db(context);
        }
        return db;
    }

    public void saveNews(News news){

        if(news!=null){

            ContentValues values =new ContentValues();
            values.put("path", news.getPath());
            values.put("title", news.getTitle());
            values.put("content", news.getContent());
            sqlDb.insert("News", null, values);
        }

    }

    public void saveCourse(Course course){

        if(course!=null){
            ContentValues values =new ContentValues();
            values.put("Count", course.getCount());
            values.put("CourseName", course.getCourseName());
            values.put("Number", course.getNumber());
            values.put("Week", course.getWeek());
            values.put("Teacher", course.getTeacher());
            values.put("Classroom", course.getClassroom());
            values.put("Time", course.getTime());
            values.put("Category", course.getCategory());
            sqlDb.insert("Course", null, values);
        }
    }

    public boolean loadCourse(){

        Cursor cursor= sqlDb.query("Course", null, null,null, null, null, null);
        int flag=0;
        if(cursor.moveToFirst()){
            do{
                flag=1;
                Course course =new Course();
                int count=cursor.getInt(cursor.getColumnIndex("Count"));
                course.setCount(count);
                course.setCourseName(cursor.getString(cursor.getColumnIndex("CourseName")));
                course.setNumber(cursor.getString(cursor.getColumnIndex("Number")));
                course.setWeek(cursor.getString(cursor.getColumnIndex("Week")));
                course.setTeacher(cursor.getString(cursor.getColumnIndex("Teacher")));
                course.setClassroom(cursor.getString(cursor.getColumnIndex("Classroom")));
                course.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                course.setTime(cursor.getString(cursor.getColumnIndex("Time")));

                HandleResponseUtil.addToList(count, course);
            }while(cursor.moveToNext());
            HandleResponseUtil.addToCourseData();

            if(cursor!=null){
                cursor.close();
            }
            if(flag==1)
                return true;
        }

        return false;

    }
}
