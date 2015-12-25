package three.com.phoneservice.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import three.com.phoneservice.Adapter.PhoneAdapter;
import three.com.phoneservice.CallBack;
import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Params.AppParams;
import three.com.phoneservice.R;
import three.com.phoneservice.Utility.GroupHolder;
import three.com.phoneservice.Utility.PhoneInfosHolder;

/**
 * Created by Zj on 2015/12/24.
 */
public class AddActivity extends AppCompatActivity {

    private ArrayList<PeopleInfo> phoneInfos =null;
    private PhoneAdapter phoneAdapter;
    private ListView std_list;
    private EditText et_search;
    private ArrayList<PeopleInfo> addToNewClassPeople=new ArrayList<PeopleInfo>();
    private Button okBtn;
    private static ArrayList<ArrayList<PeopleInfo>> groupItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if(phoneInfos==null){
            phoneInfos= PhoneInfosHolder.getPhoneInfos();
        }
        if(groupItem==null){
            groupItem=GroupHolder.getGroupItem();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                findPesonFromDbByQuery("");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                });
            }
        }).start();

    }

    private void init() {

        //设置actionBar的标题
        setActionBar();
        std_list= (ListView) findViewById(R.id.std_list);
        et_search= (EditText) findViewById(R.id.et_search);
        okBtn= (Button) findViewById(R.id.ok_add_btn);

        phoneAdapter=new PhoneAdapter(this, phoneInfos, AppParams.SEDN_TYPE, addToNewClassPeople, new CallBack() {
            @Override
            public void onStart() {
                okBtn.setText("确定"+"("+addToNewClassPeople.size()+")");
            }

            @Override
            public void onFinsh(String response) {

            }
        });
        std_list.setAdapter(phoneAdapter);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findPesonFromDbByQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupItem.add(addToNewClassPeople);
                finish();
            }
        });
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
        actionBar.setTitle("增加新的类别");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void findPesonFromDbByQuery(String query) {
        if(DbHolder.db==null){
            DbHolder.db= Db.getInstance(this);
        }

        if(DbHolder.db.loadPhoneInfoByName(query, phoneInfos)){
            if(phoneAdapter==null){
                phoneAdapter=new PhoneAdapter(this,phoneInfos,null);
            }

            phoneAdapter.notifyDataSetChanged();
        }
    }


}
