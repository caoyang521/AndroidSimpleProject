package three.com.listvieweventdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import three.com.listvieweventdemo.Model.BaseModel;
import three.com.listvieweventdemo.Model.DetailModel;
import three.com.listvieweventdemo.Model.HeaderModel;
import three.com.listvieweventdemo.R;


/**
 * Created by Administrator on 2015/9/23.
 */
public class MyAdapter extends BaseAdapter {

    private Context context=null;
    private List<BaseModel> models =null;

    public MyAdapter(Context context, List<BaseModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        BaseModel baseModel=models.get(position);
        int type=baseModel.getType();
        return type;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseModel baseModel=models.get(position);
        int type=getItemViewType(position);
        HeaderViewHolder headerViewHolder=null;
        DetailViewHolder detailViewHolder=null;
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            if(type==0){
                headerViewHolder=new HeaderViewHolder();
                convertView=layoutInflater.inflate(R.layout.header_item,null);
                headerViewHolder.headerText= (TextView) convertView.findViewById(R.id.headerText);
                convertView.setTag(headerViewHolder);
            }
            else if(type==1){
                detailViewHolder=new DetailViewHolder();
                convertView=layoutInflater.inflate(R.layout.detail_item,null);
                detailViewHolder.contentText= (TextView) convertView.findViewById(R.id.contentText);
                detailViewHolder.detailText=(TextView) convertView.findViewById(R.id.detailText);
                detailViewHolder.clickBtn= (Button) convertView.findViewById(R.id.clickButton);
                convertView.setTag(detailViewHolder);
            }
        }
        else  {
            if(type==0)
                headerViewHolder= (HeaderViewHolder) convertView.getTag();
            else if(type==1)
                detailViewHolder= (DetailViewHolder) convertView.getTag();
        }

        if(type==0){
            HeaderModel headerModel= (HeaderModel) baseModel;
            headerViewHolder.headerText.setText(headerModel.getHeader());
        }
        else if(type==1){
            final DetailModel detailModel= (DetailModel) baseModel;
            detailViewHolder.contentText.setText(detailModel.getContext());
            detailViewHolder.detailText.setText(detailModel.getDetail());
            detailViewHolder.clickBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,detailModel.getContext()+"被点击",Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    private static class HeaderViewHolder {
        TextView headerText=null;
    }

    private static class DetailViewHolder {
        TextView contentText=null;
        TextView detailText=null;
        Button clickBtn=null;
    }
}
