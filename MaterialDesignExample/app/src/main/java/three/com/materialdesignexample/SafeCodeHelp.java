package three.com.materialdesignexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/14.
 */
public class SafeCodeHelp {

    private static Bitmap codeBitmap;

    private static StringBuilder cookie=new StringBuilder();
    private static String url;

    static Bitmap getSafeCodePic(){
        try {

            //******** 取得的是InputStream，直接从InputStream生成bitmap ***********/
            codeBitmap = BitmapFactory.decodeStream(getImageStream());
            if (codeBitmap != null) {
                return codeBitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InputStream getImageStream() throws Exception {

        HttpClient httpClient = new DefaultHttpClient();

        HttpGet getMethod = new HttpGet(url);
        getMethod.setHeader("Cookie",cookie.toString());
        getMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        getMethod.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        try {
            HttpResponse response = httpClient.execute(getMethod, new BasicHttpContext());
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            return instream;
        } finally {

        }

    }

    public static void getLoginCookie(final String username){

        final String url = "http://10.22.151.40/scripts/login.exe/getPassport?";
        final DefaultHttpClient httpClient = new DefaultHttpClient();


        try {
            String content =null;
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("UserName",username));
            post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() ==200){
                content = EntityUtils.toString(response.getEntity(), "GBK");
                // 获取url
                getUrl(content);
                // get cookieStore
                CookieStore cookieStore = httpClient.getCookieStore();
                // get Cookies
                List<Cookie> cookies = cookieStore.getCookies();
                if(cookies!=null){
                    cookie.append("UserName=");
                    cookie.append(cookies.get(0).getValue());
                    cookie.append(";LOGINID=");
                    cookie.append(cookies.get(1).getValue());

                }
                else {
                    Log.d("TAG","获取Cookie失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private static void getUrl(String content) throws UnsupportedEncodingException {

        Document doc = Jsoup.parse(content);
        Elements linksElements = doc.select("img");
        url =linksElements.get(2).attr("src");

        url="http://10.22.151.40/scripts/login.exe/stamp?id="+ URLEncoder.encode(url.substring(28), "UTF-8");

    }
}
