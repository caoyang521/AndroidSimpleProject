package three.com.recyclerviewsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/10/3.
 */
public class MyAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private String[] dataset;

    public MyAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_textview,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mholder= (ViewHolder) holder;
        mholder.mTextView.setText(dataset[position]);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.mtextView);
        }
    }

}
