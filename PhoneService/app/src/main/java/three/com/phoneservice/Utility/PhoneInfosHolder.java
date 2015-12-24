package three.com.phoneservice.Utility;

import java.util.ArrayList;

import three.com.phoneservice.Model.PeopleInfo;

/**
 * Created by Zj on 2015/12/24.
 */
public class PhoneInfosHolder {

    private static ArrayList<PeopleInfo> phoneInfos =null;

    public static ArrayList<PeopleInfo> getPhoneInfos(){
        if(phoneInfos==null){
            phoneInfos=new ArrayList<PeopleInfo>();
            return phoneInfos;
        }
        else{
            return phoneInfos;
        }
    }
}
