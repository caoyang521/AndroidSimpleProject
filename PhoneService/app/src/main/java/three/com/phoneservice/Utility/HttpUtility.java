package three.com.phoneservice.Utility;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import three.com.phoneservice.Db.Db;

/**
 * Created by Administrator on 2015/9/28.
 */
public class HttpUtility {

    static final String address = "http://Xg.ndky.edu.cn/android/GetAllstudenttelephone.aspx";
    static StringBuilder response = new StringBuilder("");

    public static void sendHttpRequest(final Db db) {

        if (TextUtils.isEmpty(response)) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}