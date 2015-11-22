package three.com.phoneservice.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Adapter.PhoneAdapter;
import three.com.phoneservice.R;

/**
 * Created by Administrator on 2015/11/7.
 */
public class SearchActivity extends AppCompatActivity {
    public static void startSearchStatusActivity(Context context, Db mdb) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private SearchView searchView;
    private ListView search_list;
    private PhoneAdapter phoneAdapter;
    private ArrayList<PeopleInfo> phoneInfos =new ArrayList<PeopleInfo>();
    private Db db;
    private TextView no_preson_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_status);

        search_list= (ListView) findViewById(R.id.search_listView);
        no_preson_tv= (TextView) findViewById(R.id.no_person);
        phoneAdapter=new PhoneAdapter(this,phoneInfos);
        search_list.setAdapter(phoneAdapter);

        //设置actionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
        actionBar.setTitle("搜索联系人");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = ((PeopleInfo) parent.getItemAtPosition(position)).getPhoneNumber();
                Log.d("tag", number);
                String flag = number.substring(0, 2);
                if (!TextUtils.isEmpty(flag) && !flag.equals("  ")) {
                    showDeleteDialog(SearchActivity.this, number);
                } else {
                    new AlertDialog.Builder(SearchActivity.this)
                            .setTitle("善意的提醒")
                            .setPositiveButton("确定", null)
                            .setMessage("未查询到电话号码")
                            .show();
                }
            }
        });
    }

    private void showDeleteDialog(Context context, final String number) {
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
        if(DbHolder.db==null)
            DbHolder.db=Db.getInstance(this);
        DbHolder.db.loadPersonByNumber(number);
        Uri uri = Uri.parse("tel:"+number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seach_menu, menu);
        initSearchView(menu);
        return true;
    }

    private void initSearchView(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("请输入要搜索的姓名");
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                findPesonFromDb(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                no_preson_tv.setVisibility(View.GONE);
                findPesonFromDb(newText);
                return false;
            }
        });
    }

    private void findPesonFromDb(String query) {
        if(DbHolder.db==null){
            DbHolder.db= Db.getInstance(SearchActivity.this);
        }

        if(DbHolder.db.loadPhoneInfoByName(query, phoneInfos)){
            phoneAdapter.notifyDataSetChanged();
        }
        else
            no_preson_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}