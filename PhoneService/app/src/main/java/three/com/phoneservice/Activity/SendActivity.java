package three.com.phoneservice.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.jinwang.umthink.swipemenu.SwipeMenu;
import com.jinwang.umthink.swipemenu.SwipeMenuAdapter;
import com.jinwang.umthink.swipemenu.SwipeMenuCreator;
import com.jinwang.umthink.swipemenu.SwipeMenuItem;
import com.jinwang.umthink.swipemenu.SwipeMenuListView;
import com.melnykov.fab.FloatingActionButton;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.HashMap;
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

    private SwipeMenuListView mListView;

    public static ArrayList<HashMap<String, Object>> mListItem;  //分组信息

    public static ArrayList<ArrayList<HashMap<String, Object>>> mChildListItem; // 设备分类信息

    private SwipeMenuAdapter mAdapter;

    private FloatingActionButton fab;

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
        fab = (FloatingActionButton) findViewById(R.id.send_add_fab);

        //指示条颜色
        tabs.setIndicatorColor(R.color.blue_500);
        //指示条高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));

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

        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("youmeng", String.valueOf(mPushAgent.getTagManager().list()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



        mListView = new SwipeMenuListView(this);
        mListView.setDivider(getResources().getDrawable(android.R.color.darker_gray));
        mListView.setDividerHeight(1);

        fab.attachToListView(mListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAddActivity = new Intent(SendActivity.this,AddActivity.class);
                startActivity(startAddActivity);
            }
        });
        initEvens();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void initEvens() {
        // TODO Auto-generated method stub

        //mListView=(SwipeMenuListView)findViewById(R.id.main_listview);

        SwipeMenuCreator creator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // TODO Auto-generated method stub
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Change");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                com.jinwang.umthink.swipemenu.SwipeMenuItem deleteItem = new com.jinwang.umthink.swipemenu.SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));//249,63,37
                // set item width
                deleteItem.setWidth(dp2px(80));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);


            }
        };
        mListView.setMenuCreator(creator);

        initDatas();

        //mAdapter = new SwipeMenuAdapter(this,mListItem,mChildListItem);
//    	mAdapter = new MainActivityListViewAdapter(this,mListItem);
        mListView.setAdapter(mListItem,mChildListItem);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnSwipeMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "修改他", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "就删除了", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }
    private void initDatas() {
        // TODO Auto-generated method stub
        mListItem=new ArrayList<HashMap<String,Object>>();
        mChildListItem=new ArrayList<ArrayList<HashMap<String,Object>>>();

//        for(int i=0;i<3;i++){
//            HashMap<String, Object> listMap= new HashMap<String, Object>();
//            ArrayList<HashMap<String, Object>> childList=new ArrayList<HashMap<String,Object>>();
//            listMap.put("groupName", "组名"+i);
//            listMap.put("groupNumber", "项目数[" + i + "/10]");
//            mListItem.add(listMap);
//
//            for(int j=0;j<2;j++){
//                HashMap<String, Object> childMap=new HashMap<String, Object>();
//                childMap.put("lamp_headPhoto", j==0?R.drawable.main_lamp:R.drawable.main_dehumidifier);
//                childMap.put("lamp_name", j==0?"卧室灯":"除湿器");
//                childMap.put("lamp_place", "卧室");
//                childMap.put("lamp_date", j==0?"亮度":"湿度");
//                childMap.put("lamp_content", "已开");
//                childMap.put("lamp_date_explain", 57+"%");
//                childList.add(childMap);
//            }
//            mChildListItem.add(childList);
//        }
    }
    private void initViewPager() {
        //ViewPager init
        vpAdapter= new ViewPagerAdapter(this);
        pager.setAdapter(vpAdapter);
        // Bind the tabs to the ViewPager
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.setVisibility(View.GONE);
                } else
                    fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private Context context;

        private ViewPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "本班学生";
            } else {
                return "分类";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            if(position==0){
                ListView listView = getDataListView(context, PeopleData);
                container.addView(listView);
                return listView;
            }
            else{
                container.addView(mListView);
                return  mListView;
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public static PhoneAdapter phoneAdapter=null;

    private ListView getDataListView(Context context, List<PeopleInfo> list) {
        if(list!=null){
            phoneAdapter=new PhoneAdapter(context, list,"send");
            ListView stdListView = new ListView(context);
            stdListView.setDivider(getResources().getDrawable(android.R.color.transparent));
            stdListView.setDividerHeight(0);
            stdListView.setAdapter(phoneAdapter);
            return stdListView;
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
                alias.append(PeopleData.get(i).getSchoolNumber()+",");
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UmengHelper umengHelper = new UmengHelper(AppParams.Appkey, AppParams.AppMasterSecret);
                    //umengHelper.sendAndroidUnicast();
                    umengHelper.sendAndroidCustomizedcast(alias.substring(0,alias.length()-1).toString(),content_et.getText().toString());
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
