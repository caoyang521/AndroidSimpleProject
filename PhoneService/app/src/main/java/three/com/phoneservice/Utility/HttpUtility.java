package three.com.phoneservice.Utility;

import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import three.com.phoneservice.CallBack;
import three.com.phoneservice.Params.AppParams;

/**
 * Created by Administrator on 2015/9/28.
 */
public class HttpUtility {


    static StringBuilder response = new StringBuilder("");

    public static boolean sendHttpRequest() {

        if (TextUtils.isEmpty(response)) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(AppParams.phoneAddress);
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

    public static void loginHttpRequest(final String url, final String username, final String password,final CallBack callBack)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                callBack.onStart();
                final DefaultHttpClient httpClient = new DefaultHttpClient();
                try {
                    String content =null;
                    HttpPost post = new HttpPost(url);
                    post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
                    post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

                    List params = new ArrayList();

                    params.add(new BasicNameValuePair("name",username));
                    params.add(new BasicNameValuePair("pass",password));

                    post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                    HttpResponse response = httpClient.execute(post);
                    if(response.getStatusLine().getStatusCode() ==200){
                        content = EntityUtils.toString(response.getEntity());
                        HandleResponseUtility.handleLoginResponse(content);
                        callBack.onFinsh("ok");
                    }
                } catch (Exception e) {
                    callBack.onFinsh(null);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}