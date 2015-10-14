package three.com.materialdesignexample.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Framgment.NewsFramgment;
import three.com.materialdesignexample.JsHelper.HexMd5;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.MyAdapter;

/**
 * Created by Administrator on 2015/10/8.
 */
public class HttpUtil  {

    public static LinkedHashMap<String,News> datamap=new LinkedHashMap<String,News>();

    public static String path;

    private static boolean isFirst=true;

    private static String cookie="";

    public static void getHtmlUtil( Context context,String url, final CallBack callBack,final int method,
                                    final Map<String, String> headers,
                                    final Map<String, String> params){

        StringRequest stringRequest = null;

        if(url.substring(0,1).equals("/")==true){
            path=url;
            url=News.INDEX+path;
        }

        RequestQueue mQueue = Volley.newRequestQueue(context);
        if(method== Request.Method.GET){
            stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                    Log.d("TAG", response);
                                    callBack.onStart();
                                    callBack.onFinsh(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });

        }
        else if(method== Request.Method.POST) {

            stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //callBack.onStart();
                        Log.d("TAG","response succeed");

                        callBack.onFinsh(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                })
                {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers != null) {
                        return headers;
                    } else {
                        return super.getHeaders();
                    }
                }

                //若为post请求则在此添加参数
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (params != null && method == Method.POST) {
                        return params;
                    } else {
                        return super.getParams();
                    }
                }

                @Override
                public String getUrl() {
                    return super.getUrl();
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    //取得cookie
                    if (isFirst == true) {
                        cookie = response.headers.get("Set-Cookie");
                        isFirst = false;
                    }

                    String result = "";
                    try {
                       // result = new String(response.data, "GB2312");
                        result = new String(response.data, "GBK");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
        }


        //设置20秒的超时
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(stringRequest);
    }

    public static void parseTitleData(String response){

        if (response!=null&&!"".equals(response)) {
            Document doc = Jsoup.parse(response);
            Elements linksElements = doc.select("div[class=article_list mtop10]>ul>li>a");
            // class为“article_list mtop10”的div里面ul里面li里面a标签
            for (Element ele:linksElements) {
                String href = ele.attr("href");
                String title = ele.text();
                News news =new News();
                news.setPath(href);
                news.setTitle(title);
                datamap.put(news.getPath(), news);
            }
            NewsFramgment.adapter=new MyAdapter(new ArrayList<News>(datamap.values()),MyAdapter.context);
            NewsFramgment.recyclerView.setAdapter(NewsFramgment.adapter);
            NewsFramgment.adapter.notifyDataSetChanged();
        }
    }

    public static void parseContentData(String response){

        if (response!=null&&!"".equals(response)) {
            Document document = Jsoup.parse(response);
            Elements pElements =  pElements = document.select("p");

            StringBuilder sb = new StringBuilder();
            for (Element e : pElements) {
                String str = e.text();
                sb.append(str + "\n");
            }
            datamap.get(path).setContent(sb.toString());
        }
    }

    public static void handleNewsHtmlStr(String htmlStr, final CallBack callback) {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {

                String result = "";
                try {
                    Document document = Jsoup.parse(params[0]);
                    Elements pElements = document.select("div[class=article_content]");

                    Elements pngs = document.select("img[src]");
                    for (Element element : pngs) {
                        String imgUrl = element.attr("src");
                        if (imgUrl.trim().startsWith("/")) {
                            imgUrl = News.INDEX + imgUrl;
                            element.attr("src", imgUrl);
                        }
                    }

                    Elements iElements=null;
                    StringBuilder sb = new StringBuilder();
                    for (Element e : pElements) {
                        String str = e.html();
                        sb.append(str + "\n");
                    }
                    result = sb.toString();

                } catch (Exception e) {
                    result = "";
                    Log.d("winson", "解析错误： " + e);
                }

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                callback.onFinsh(s);
            }
        }.execute(htmlStr);
    }
    public static String userName="";
    public static String password="";
    public static String passport="";
    public static void login(final Context context, final CallBack callBack) throws Exception {

        String LoginId="";

        String Digest="";
        String plainText="";

        List<String> loginCookie=null;
       // loginCookie= SecurityCodeHelper.getLoginCookie(userName);

        LoginId=getLoginId(loginCookie.get(0));
        cookie=loginCookie.get(1);
        //passport= SecurityCodeHelper.getSafeCode(loginCookie.get(2), cookie);

        plainText=userName+LoginId+password+passport;
        Digest= HexMd5.getMD5(plainText);
        Log.d("TAG", "next url");
        Map<String, String> newHeader = new HashMap<String, String>();
        //设置referer
        newHeader.put("Referer", "http://10.22.151.40/scripts/login.exe/getPassport?");
        //必须设置cookie
        newHeader.put("Cookie", cookie);

        newHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240");

        Map<String, String> params=new HashMap<String, String>();
        params.put("UserName",userName);
        params.put("Digest",Digest);
        getHtmlUtil(context, "http://10.22.151.40/scripts/login.exe/login?",
                callBack, Request.Method.POST, newHeader, params);



    }


    public static void test(final Context context, final CallBack callBack)
    {

        try {
            login(context, callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getLoginId(String response){
        Document doc = Jsoup.parse(response);
        Elements Elements = doc.select("script");
        Element jsElements = Elements.get(1);

        Pattern pattern = Pattern.compile("\"\\{.*\\}\"");
        Matcher matcher = pattern.matcher(jsElements.toString());
        String findid = null;
        if (matcher.find()) {
            findid = matcher.group();
            Log.d("TAG", matcher.group());
        }

        String loginID = findid.substring(2, findid.length() - 2);
        Log.d("TAG", loginID);

        return  loginID;
    }
}
