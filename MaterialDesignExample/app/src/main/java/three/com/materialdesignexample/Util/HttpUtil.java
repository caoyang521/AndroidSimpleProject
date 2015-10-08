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

import three.com.materialdesignexample.DrawerLayoutActivity;
import three.com.materialdesignexample.Framgment.NewsFramgment;
import three.com.materialdesignexample.Models.News;

/**
 * Created by Administrator on 2015/10/8.
 */
public class HttpUtil  {
    public static ArrayList<News> dataset=new ArrayList<News>();

    public static void getHtmlUtil( Context context){

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest("http://www.kyren.net/Category_17/Index.aspx",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        parseData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(stringRequest);
    }

    private static void parseData(String response){

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
                dataset.add(news);
            }
            closeProgressDialog();
            NewsFramgment.adapter.notifyDataSetChanged();
        }
    }

    private static void closeProgressDialog() {
        if (DrawerLayoutActivity.progressDialog != null) {
            DrawerLayoutActivity.progressDialog.dismiss();
        }
    }
}
