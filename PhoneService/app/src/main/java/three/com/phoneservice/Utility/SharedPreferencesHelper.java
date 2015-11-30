package three.com.phoneservice.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import three.com.phoneservice.Params.AppParams;

/**
 * Created by Administrator on 2015/11/19.
 */
public class SharedPreferencesHelper {

    public static void PhoneHasImported(Context context){

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("IsPhoneImport",true);
        editor.commit();

    }

    public static boolean IsPhoneImported(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("IsPhoneImport", false);
    }

    public static void saveStdInfo(Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("name", AppParams.name);
        editor.putString("school", AppParams.School);
        editor.putString("class", AppParams.className);
        editor.putString("schoolNumber", AppParams.SchoolNumber);
        editor.commit();
    }

    public static boolean getStdInfo(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        AppParams.name=prefs.getString("name", null);
        AppParams.School=prefs.getString("school", null);
        AppParams.className =prefs.getString("class", null);
        AppParams.SchoolNumber=prefs.getString("schoolNumber",null);
        if(AppParams.name==null)
            return false;
        else
            return true;
    }
}
