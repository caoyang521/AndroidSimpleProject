package three.com.phoneservice.Utility;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Model.PeopleInfo;

/**
 * Created by Administrator on 2015/9/24.
 */
public class Utility {

    public static void createPersonInfo(Db db){
        for(int i=1;i<=10;i++){
            PeopleInfo peopleInfo=new PeopleInfo();
            peopleInfo.setNumber(Integer.toString(i));
            peopleInfo.setName("a" + i);
            if(i==5){
                peopleInfo.setNumber("13958839460");
                peopleInfo.setName("Zmn");
            }
            db.savePerson(peopleInfo);
        }
    }
}
