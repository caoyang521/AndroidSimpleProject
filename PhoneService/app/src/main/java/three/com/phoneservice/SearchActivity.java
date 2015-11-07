package three.com.phoneservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;

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