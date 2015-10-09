package three.com.phoneservice.Utility;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Model.PeopleInfo;

/**
 * Created by Administrator on 2015/9/24.
 */
public class Utility {

//    public static void createPersonInfo(Db db){
//        for(int i=1;i<=10;i++){
//            PeopleInfo peopleInfo=new PeopleInfo();
//            peopleInfo.setNumber(Integer.toString(i));
//            peopleInfo.setName("a" + i);
//            if(i==5){
//                peopleInfo.setNumber("663085");
//                peopleInfo.setName("Zmn");
//            }
//            db.savePerson(peopleInfo);
//        }
//    }

    public synchronized static boolean handlePersonResponse(Db db){

        boolean isover=false;
        if (TextUtils.isEmpty(HttpUtility.response))
            return isover;
        try{

            {
                PeopleInfo peopleInfo =new PeopleInfo();
                peopleInfo.setName("张特");
                peopleInfo.setNumber("13967860592");
                db.savePerson(peopleInfo);

            }
            JSONObject jsonObject = new JSONObject(String.valueOf(HttpUtility.response));
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            Log.i("test","begin");
            db.savePerson(jsonArray);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject peopleObj =jsonArray.getJSONObject(i);
//                PeopleInfo peopleInfo =new PeopleInfo();
//                peopleInfo.setName(peopleObj.getString("学生姓名"));
//                peopleInfo.setNumber(peopleObj.getString("手机短号"));
//                db.savePerson(peopleInfo);
//            }
            Log.i("test","emd");
            isover=true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return isover;
    }

}
