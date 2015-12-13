package three.com.phoneservice.Activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;

import three.com.phoneservice.Adapter.PhoneAdapter;
import three.com.phoneservice.CallBack;
import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Params.AppParams;
import three.com.phoneservice.R;
import three.com.phoneservice.Service.PhoneServer;
import three.com.phoneservice.Utility.HandleResponseUtility;
import three.com.phoneservice.Utility.HttpUtility;
import three.com.phoneservice.Utility.SharedPreferencesHelper;

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
    private ProgressDialog progressDialog;
    private TextView noPhoneHistory_tv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("three", "oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_activity);
        UmengUpdateAgent.update(this);
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        Runnable addAliasRunnable = new Runnable() {
            public void run() {
                try {

                    mPushAgent.addAlias(AppParams.SchoolNumber, "KYZS");
                    //mPushAgent.getTagManager().add(AppParams.className);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(addAliasRunnable).start();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        import_btn= (Button) findViewById(R.id.import_btn);
        noPhoneHistory_tv= (TextView) findViewById(R.id.no_phone_history);
        phone_lv= (ListView) findViewById(R.id.phone_lv);
        phoneAdapter=new PhoneAdapter(this,phoneInfos,null);
        DbHolder.db=Db.getInstance(this);
        if(!isWorked()) {
            Intent phoneservice = new Intent(this, PhoneServer.class);
            startService(phoneservice);
        }
        openOptionsMenu();

        //设置actionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
        actionBar.setTitle("搜索联系人");

        getSupportActionBar().setElevation(0);

        swipeRefreshLayout.setColorSchemeColors(R.color.mainColor);
        emptyLayout= (LinearLayout) findViewById(R.id.empty_layout);




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AlertDialog dialog =  new AlertDialog.Builder(PhoneActivity.this)
                        .setTitle("更新")
                        .setMessage("是否需要更新通讯录，流量较大，请确认在Wifi下！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        findFromHttp();
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("取消",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        })
                        .show();



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
                                progressDialog.setMessage("正在导入...");
                                progressDialog.show();
                            }
                        });
                        findFromHttp();
                    }
                }).start();
            }
        });

        phone_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number= ((PeopleInfo) parent.getItemAtPosition(position)).getPhoneNumber();
                Log.d("tag", number);
                String flag=number.substring(0,2);
                if(!TextUtils.isEmpty(flag) && !flag.equals("  ")){
                    showDeleteDialog(PhoneActivity.this,number);
                }
                else{
                    new AlertDialog.Builder(PhoneActivity.this)
                            .setTitle("善意的提醒")
                            .setPositiveButton("确定", null)
                            .setMessage("未查询到电话号码")
                            .show();
                }
            }
        });

        Log.d("three", "oncreate over");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    public void showDeleteDialog(Context context, final String number) {
        android.app.AlertDialog alertDialog = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage("是否要拨打电话?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        phoneCall(number);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void phoneCall(String number) {
        DbHolder.db.loadPersonByNumber(number);
        Uri uri = Uri.parse("tel:"+number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

    private void findFromHttp() {

        if (HttpUtility.sendHttpRequest()) {
            HandleResponseUtility.handlePersonResponse(DbHolder.db, phoneInfos, new CallBack() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinsh(String response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                            handler.sendEmptyMessage(AppParams.HttpSuccess);
                            SharedPreferencesHelper.PhoneHasImported(PhoneActivity.this);
                        }
                    });
                }
            });
        }
        else
        {
            handler.sendEmptyMessage(AppParams.HttpTimeOut);

        }
    }

    private void initView(){
        Log.d("TAG", "initview");
        noPhoneHistory_tv.setVisibility(View.GONE);
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

                if(DbHolder.db!=null){
                    DbHolder.db.loadPhoneInfo(phoneInfos, new CallBack() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinsh(String response) {
                            if(response==null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initView();
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        noPhoneHistory_tv.setVisibility(View.VISIBLE);
                                        phoneAdapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        }
                    });
                }
            }

        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("three", "onResume");
        MobclickAgent.onResume(this);
        if(SharedPreferencesHelper.IsPhoneImported(this)){
            emptyLayout.setVisibility(View.GONE);
            findFromDb();
        }
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
            SearchActivity.startSearchStatusActivity(this, DbHolder.db);
            return true;
        }
        else if(id==R.id.action_login){
            Intent loginIntent =new Intent(PhoneActivity.this,WebLoginActivity.class);
            startActivity(loginIntent);
            return true;
        }
        else if(id==R.id.action_send){
            Intent sendIntent =new Intent(PhoneActivity.this,SendActivity.class);
            startActivity(sendIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isWorked()) {
            Intent phoneservice = new Intent(this, PhoneServer.class);
            startService(phoneservice);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isWorked()) {
            Intent phoneservice = new Intent(this, PhoneServer.class);
            startService(phoneservice);
        }
    }

    public boolean isWorked()
    {
        ActivityManager myManager=(ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for(int i = 0 ; i<runningService.size();i++)
        {
            if(runningService.get(i).service.getClassName().toString().equals("three.com.phoneservice.Service.PhoneServer"))
            {
                return true;
            }
        }
        return false;
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case AppParams.HttpTimeOut:
                    progressDialog.dismiss();
                    Toast.makeText(PhoneActivity.this, "通讯录无法下载，请检查网络！", Toast.LENGTH_SHORT).show();
                    break;
                case AppParams.HttpSuccess:
                    Toast.makeText(PhoneActivity.this, "通讯录下载成功！", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    break;
            }
        }
    };
}