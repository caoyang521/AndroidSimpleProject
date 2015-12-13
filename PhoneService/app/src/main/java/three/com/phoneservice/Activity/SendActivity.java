package three.com.phoneservice.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import three.com.phoneservice.Adapter.PhoneAdapter;
import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Params.AppParams;
import three.com.phoneservice.R;
import three.com.phoneservice.Utility.UmengHelper;

/**
 * Created by Administrator on 2015/12/6.
 */
public class SendActivity extends AppCompatActivity {
    private ViewPager pager=null;
    private PagerSlidingTabStrip tabs=null;
    private ViewPagerAdapter vpAdapter=null;
    private ArrayList<PeopleInfo> PeopleData=new ArrayList<PeopleInfo>();
    private ArrayList<String> classData=null;
    private EditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        //设置actionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
        actionBar.setTitle("推送");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        if(DbHolder.db==null){
            DbHolder.db= Db.getInstance(SendActivity.this);
        }
        pager = (ViewPager) findViewById(R.id.viewPager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tab_indicator);

        content_et= (EditText) findViewById(R.id.content_et);

//        //文字大小
//        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
//        //文字颜色
//        tabs.setTextColor(Color.parseColor("#88ffffff"));
        //指示条颜色
        tabs.setIndicatorColor(Color.WHITE);
        //指示条高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        //最下面的分隔条
//        pagerSlidingTabStrip.setUnderlineHeight(0);
        tabs.setUnderlineColorResource(R.color.blue_500);
        //字体样式
//        pagerSlidingTabStrip.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        //分隔条颜色
        //tabs.setDividerColor(getResources().getColor(R.color.blue_500));

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(PeopleData.size()==0)
                    findPesonFromDb();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViewPager();
                    }
                });
            }
        }).start();



    }

    private void initViewPager() {
        //ViewPager init
        vpAdapter= new ViewPagerAdapter(this);

        pager.setAdapter(vpAdapter);
        // Bind the tabs to the ViewPager
        tabs.setViewPager(pager);

        pager.setCurrentItem(0);
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private Context context;

        private ViewPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "学生";
            } else {
                return "班级";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ListView listView=null;
            if(position==0){
                listView = getDataListView(context, PeopleData);
                container.addView(listView);
            }
//            else{
//                listView = getClassListView(context, classData);
//                container.addView(listView);
//            }

            return listView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private ListView getClassListView(Context context, List<String> classData) {
        return null;
    }

    public static PhoneAdapter phoneAdapter=null;

    private ListView getDataListView(Context context, List<PeopleInfo> list) {
        if(list!=null){
            phoneAdapter=new PhoneAdapter(context, list,"send");
            ListView coursesList = new ListView(context);
            coursesList.setDivider(getResources().getDrawable(android.R.color.transparent));
            coursesList.setDividerHeight(0);
            coursesList.setAdapter(phoneAdapter);
            return coursesList;
        }
        else
            return null;
    }

    private void findPesonFromDb() {

        if(DbHolder.db.loadPhoneInfoByName("", PeopleData)){
            if(phoneAdapter!=null)
                phoneAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        if(id == R.id.action_send) {
            sendMessage();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage() {
        if (TextUtils.isEmpty(content_et.getText().toString().trim())) {
            Toast.makeText(this, "内容不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final StringBuilder alias=new StringBuilder();
        for(int i=0;i<PeopleData.size();i++){
            if(PeopleData.get(i).isChecked()){
                alias.append(PeopleData.get(i).getSchoolNumber());
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UmengHelper umengHelper = new UmengHelper(AppParams.Appkey, AppParams.AppMasterSecret);
                    //umengHelper.sendAndroidUnicast();
                    umengHelper.sendAndroidCustomizedcast(alias.toString(),content_et.getText().toString());
                    //umengHelper.sendAndroidBroadcast();
                    //umengHelper.sendAndroidGroupcast();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
