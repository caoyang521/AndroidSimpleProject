package three.com.phoneservice.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
