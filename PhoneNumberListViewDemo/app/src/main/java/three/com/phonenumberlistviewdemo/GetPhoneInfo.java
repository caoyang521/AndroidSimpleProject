package three.com.phonenumberlistviewdemo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/9/22.
 */
public class GetPhoneInfo {

    private static List<PhoneInfo> phoneInfoList = new ArrayList<PhoneInfo>();

    public static List<PhoneInfo> getPhoneinfo(Context context){
        Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI,null,null,null,null);
        String PhoneNumber="";
        String PhoneName="";
        Bitmap PhotoIcon = null;
        while(cursor.moveToNext()){
            PhoneNumber=cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
            PhoneName=cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));

            PhoneInfo phoneInfo=new PhoneInfo();
            phoneInfo.setPhoneNumber(PhoneNumber);
            phoneInfo.setPhoneName(PhoneName);

            phoneInfoList.add(phoneInfo);
        }
        return phoneInfoList;
    }
}
