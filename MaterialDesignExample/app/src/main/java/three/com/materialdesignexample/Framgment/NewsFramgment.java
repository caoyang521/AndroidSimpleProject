package three.com.materialdesignexample.Framgment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import three.com.materialdesignexample.MyAdapter;
import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NewsFramgment extends Fragment {

    private RecyclerView recyclerView=null;
    private RecyclerView.LayoutManager layoutManager=null;
    public static MyAdapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);

        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MyAdapter(HttpUtil.dataset);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
