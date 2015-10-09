package three.com.materialdesignexample.Util;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Framgment.NewsFramgment;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.MyAdapter;

/**
 * Created by Administrator on 2015/10/8.
 */
public class HttpUtil  {

    public static LinkedHashMap<String,News> datamap=new LinkedHashMap<String,News>();

    public static String path;

    public static void getHtmlUtil( Context context,String url,final int type, final CallBack callBack){

        if(url.equals(News.NEWS_INDEX)==false){
            path=url;
            url=News.INDEX+path;
        }

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        switch (type){
                            case News.TITLE:
                                callBack.onStart();
                                parseTitleData(response);
                                callBack.onFinsh();
                                break;
                            case News.CONTENT:
                                callBack.onStart();
                                parseContentData(response);
                                callBack.onFinsh();
                                break;
                            default:
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(stringRequest);
    }

    private static void parseTitleData(String response){

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

    private static void parseContentData(String response){

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

}
