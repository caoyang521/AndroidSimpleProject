package three.com.phoneservice;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Utility.HttpUtility;
import three.com.phoneservice.Utility.Utility;

/**
 * Created by Administrator on 2015/11/6.
 */
public class PhoneActivity extends AppCompatActivity{

    private SwipeRefreshLayout swipeRefreshLayout;
    private Button import_btn;
    private ListView phone_lv;
    private ArrayList<PeopleInfo> phoneInfos =new ArrayList<PeopleInfo>();
    private PhoneAdapter phoneAdapter;
    private LinearLayout emptyLayout;
    private Db db= DbHolder.db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("three", "oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_activity);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        import_btn= (Button) findViewById(R.id.import_btn);
        phone_lv= (ListView) findViewById(R.id.phone_lv);
        phoneAdapter=new PhoneAdapter(this,phoneInfos);
        db=Db.getInstance(this);

        openOptionsMenu();

        //设置actionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
        actionBar.setTitle("搜索联系人");

        getSupportActionBar().setElevation(0);

        swipeRefreshLayout.setColorSchemeColors(R.color.mainColor);
        emptyLayout= (LinearLayout) findViewById(R.id.empty_layout);

        findFromDb();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findFromHttp();
            }
        });

        import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialogHelper.showProgressDialog(PhoneActivity.this, "正在导入...");
                            }
                        });
                        findFromHttp();
                    }
                }).start();
            }
        });
        Log.d("three", "oncreate over");
    }

    private void findFromHttp() {

        HttpUtility.sendHttpRequest(db);
        Utility.handlePersonResponse(db, phoneInfos, new CallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinsh(String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                        ProgressDialogHelper.closeProgressDialog();
                    }
                });
            }
        });

    }

    private void initView(){
        Log.d("TAG", "initview");
        Log.d("TAG",phoneInfos.get(0).getClassName().toString());
        phoneAdapter.notifyDataSetChanged();
        phone_lv.setAdapter(phoneAdapter);
        phone_lv.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

    }

    private void findFromDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(phoneInfos.size()==0){
                    if(db!=null){
                        db.loadPhoneInfo(phoneInfos, new CallBack() {
                            @Override
                            public void onStart() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                swipeRefreshLayout.setRefreshing(true);
                                            }
                                        });
                                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                                        emptyLayout.setVisibility(View.GONE);
                                    }
                                });
                            }

                            @Override
                            public void onFinsh(String response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initView();
                                    }
                                });
                            }
                        });
                    }
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.phone_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        if(id == R.id.action_search) {
            SearchActivity.startSearchStatusActivity(this,db);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
