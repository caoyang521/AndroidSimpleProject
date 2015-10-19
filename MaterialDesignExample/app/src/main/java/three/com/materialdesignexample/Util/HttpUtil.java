package three.com.materialdesignexample.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import three.com.materialdesignexample.Adapter.NewsAdapter;
import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Framgment.NewsFramgment;
import three.com.materialdesignexample.JsHelper.HexMd5;
import three.com.materialdesignexample.Models.Course;
import three.com.materialdesignexample.Models.News;

/**
 * Created by Administrator on 2015/10/8.
 */
public class HttpUtil  {

    public static LinkedHashMap<String,News> datamap=new LinkedHashMap<String,News>();

    public static String path;

    private static boolean isGBK=false;

    public static String cookie="";

    public static void getHtmlUtil( Context context,String url, final CallBack callBack,final int method,
                                    final Map<String, String> headers
                                    ){

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

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    String result = "";
                    try {
                        if(isGBK){
                            result = new String(response.data, "GB2312");
                            isGBK=false;
                        }
                        else
                            result = new String(response.data, "utf-8");
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
        mQueue.start();

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
            NewsFramgment.adapter=new NewsAdapter(new ArrayList<News>(datamap.values()), NewsAdapter.context);
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
    public static String LoginId="";

    public static void login(final CallBack callBack){


        String Digest="";
        String plainText="";

        plainText=userName+LoginId+password+passport;
        Digest= HexMd5.getMD5(plainText);
        Log.d("TAG", "next url");

        final String finalDigest = Digest;

        new Thread(new Runnable() {
            @Override
            public void run() {
                getLoginCookie(finalDigest,callBack);
            }
        }).start();

    }

    @SuppressWarnings("deprecation")
    public static void getCourseHtml(final Context context, final CallBack callBack)
    {
        login(new CallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
                String url = "http://10.22.151.40/scripts/wzw_jspk.asp?WHO=" + "144173551" + "&year=2014&term=2&STUDENTNAME=" + "张坚";
                try {
                    url = "http://10.22.151.40/scripts/wzw_jspk.asp?WHO=144173551&year=2014&term=2&STUDENTNAME=" + URLEncoder.encode("张坚", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Map<String, String> headers = new HashMap<String, String>();
                //设置referer
                headers.put("Referer", "http://10.22.151.40/scripts/login.exe/login?");
                headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                Log.d("TAG", cookie);
                headers.put("Cookie", cookie);
                isGBK = true;
                getHtmlUtil(context, url, new CallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinsh(String response) {
                        handleCourseHtmlStr(response, callBack);

                    }
                }, Request.Method.GET, headers);
            }

            @Override
            public void onFinsh(String response) {

            }
        });
    }

    public static ArrayList<List<Course>> courseData=new ArrayList<List<Course>>();
    public static void handleCourseHtmlStr(String htmlStr, final CallBack callback) {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {

                Boolean result=false;
                List<Course> list1 = new ArrayList<Course>();
                List<Course> list2 = new ArrayList<Course>();
                List<Course> list3 = new ArrayList<Course>();
                List<Course> list4 = new ArrayList<Course>();
                List<Course> list5 = new ArrayList<Course>();
                List<Course> list6 = new ArrayList<Course>();
                List<Course> list7 = new ArrayList<Course>();
                int flag=0;
                int flagcount=0;
                String[] res = null;
                try {
                    Document document = Jsoup.parse(params[0]);
                    Elements pElements = document.select("td[rowSpan=2] > font");
                    for(int i=0;i<pElements.size();i++){
                        Element e=pElements.get(i);
                        int count=(i+1)%7;
                        int timeCount=(i)/7;
                        String response=  e.text();
                        if(response.length()>120){
                            if(flag==0){
                                res= response.split("上课周次：111111111111111111");
                                flag=res.length;
                            }
                            response=res[flagcount];
                            flagcount++;
                            if(flagcount<flag)
                                i--;
                            else{
                                flagcount=0;
                                flag=0;
                            }
                        }

                        Course course =new Course();
                        if(!TextUtils.isEmpty(response)){
                            switch (timeCount){
                                case 0:
                                    course.setTime("1-2");
                                    break;
                                case 1:
                                    course.setTime("3-4");
                                    break;
                                case 2:
                                    course.setTime("5-6");
                                    break;
                                case 3:
                                    course.setTime("7-8");
                                    break;
                                case 4:
                                    course.setTime("9-10");
                                    break;
                                case 5:
                                    course.setTime("11-12");
                                    break;
                                default:
                                    break;
                            }

                            Pattern pattern = Pattern.compile("\\[+单?双?周?\\]+.*:");
                            Matcher matcher = pattern.matcher(response);

                            String find = null;
                            if (matcher.find()) {
                                find = matcher.group();
                                Log.d("TAG", matcher.group());
                            }
                            String week = find.substring(0, 4);
                            String name = find.substring(4, find.length() - 1);
                            Log.d("TAG", name);
                            course.setWeek(week);
                            course.setCourseName(name);

                            Pattern patternNum = Pattern.compile(":\\w*");
                            Matcher matcherNum = patternNum.matcher(response);
                            String findNum = null;
                            if (matcherNum.find()) {
                                findNum = matcherNum.group();
                            }
                            String number = findNum.substring(1);
                            Log.d("TAG", number);
                            course.setNumber(number);

                            Pattern patternTea = Pattern.compile("：\\[\\D*\\] 课");
                            Matcher matcherTea = patternTea.matcher(response);
                            String findTea = null;
                            if (matcherTea.find()) {
                                findTea = matcherTea.group();
                            }
                            String teacher = findTea.substring(2,findTea.length()-3);
                            Log.d("TAG", teacher);
                            course.setTeacher(teacher);

                            Pattern patternRoom = Pattern.compile("室：\\[.*\\]");
                            Matcher matcherRoom = patternRoom.matcher(response);
                            String findRoom = null;
                            if (matcherRoom.find()) {
                                findRoom = matcherRoom.group();
                            }
                            String room = findRoom.substring(3,findRoom.length()-1);
                            Log.d("TAG", room);
                            course.setClassroom(room);

                            Pattern patternCate = Pattern.compile("别：\\[.*\\] 教");
                            Matcher matcherCate = patternCate.matcher(response);
                            String findCate = null;
                            if (matcherCate.find()) {
                                findCate = matcherCate.group();
                            }
                            String Cate = findCate.substring(3,findCate.length()-4);
                            Log.d("TAG", Cate);
                            course.setCategory(Cate);

                        }
                        if(course.getClassroom()!=null){
                            switch (count){
                                case 1:
                                    list1.add(course);
                                    break;
                                case 2:
                                    list2.add(course);
                                    break;
                                case 3:
                                    list3.add(course);
                                    break;
                                case 4:
                                    list4.add(course);
                                    break;
                                case 5:
                                    list5.add(course);
                                    break;
                                case 6:
                                    list6.add(course);
                                    break;
                                case 0:
                                    list7.add(course);
                                    break;
                            }
                        }
                    }
                    
                    courseData.add(list1);
                    courseData.add(list2);
                    courseData.add(list3);
                    courseData.add(list4);
                    courseData.add(list5);
                    courseData.add(list6);
                    courseData.add(list7);

                    result=true;
                } catch (Exception e) {
                    result=false;
                    Log.d("winson", "解析错误： " + e);
                }

                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result)
                    callback.onFinsh("");
                else
                    Log.d("TAG","handle course failed");
            }
        }.execute(htmlStr);
    }

    @SuppressWarnings("deprecation")
    public static void getLoginCookie(String Digest,CallBack callBack){

        final String url = "http://10.22.151.40/scripts/login.exe/login?";
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        StringBuilder ckSb=new StringBuilder(cookie);

        try {
            //String content =null;
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            post.setHeader("Cookie",cookie );
            List params = new ArrayList();

            params.add(new BasicNameValuePair("UserName",userName));
            params.add(new BasicNameValuePair("Digest",Digest));

            post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() ==200){
                // content = EntityUtils.toString(response.getEntity(), "GBK");
                // Log.d("TAG",content);
                // get cookieStore
                CookieStore cookieStore = httpClient.getCookieStore();
                // get Cookies
                List<Cookie> cookies = cookieStore.getCookies();
                if(cookies!=null){
                    ckSb.append(";SessionID=");
                    ckSb.append(cookies.get(1).getValue());
                    ckSb.append(";UserGroup=");
                    ckSb.append(cookies.get(2).getValue());
                    ckSb.append(";UserRoles=");
                    ckSb.append(cookies.get(3).getValue());
                    cookie=ckSb.toString();
                    callBack.onStart();
                }
                else {
                    Log.d("TAG","获取Cookie失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
