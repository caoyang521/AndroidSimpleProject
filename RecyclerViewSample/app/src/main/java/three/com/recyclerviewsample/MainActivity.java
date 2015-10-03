package three.com.recyclerviewsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity {

    private RecyclerView recyclerview=null;
    private RecyclerView.LayoutManager layoutManager=null;
    private RecyclerView.Adapter adapter=null;
    private String[] dataset={"java","python","c++","c#","c","go","git"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview= (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerview.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        adapter=new MyAdapter(dataset);
        recyclerview.setAdapter(adapter);
    }


}
