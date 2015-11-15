package three.com.phoneservice.Utility;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.params.AppParams;

/**
 * Created by Administrator on 2015/9/28.
 */
public class HttpUtility {


    static StringBuilder response = new StringBuilder("");

    public static boolean sendHttpRequest() {

        if (TextUtils.isEmpty(response)) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(AppParams.address);
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
                return true;
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return false;
    }
}