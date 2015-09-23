package three.com.listvieweventdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import three.com.listvieweventdemo.Adapter.MyAdapter;
import three.com.listvieweventdemo.Model.BaseModel;
import three.com.listvieweventdemo.Model.HeaderModel;
import three.com.listvieweventdemo.repostitory.Repos;

public class MainActivity extends AppCompatActivity {

    private List<BaseModel> models=null;
    private ListView listView=null;
    private MyAdapter adapter=null;
    private TextView textView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        models= Repos.getData();
        adapter = new MyAdapter(this,models);
        listView= (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        textView= (TextView) findViewById(R.id.textView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object dataObj=adapter.getItem(position);
                if(dataObj.getClass()== HeaderModel.class){
                    textView.setText(String.format("第%s行被点触,其标题为：%s",position,
                            ((HeaderModel)dataObj).getHeader()));
                }
                else{
                    textView.setText(String.format("第%s行被点触",position));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
