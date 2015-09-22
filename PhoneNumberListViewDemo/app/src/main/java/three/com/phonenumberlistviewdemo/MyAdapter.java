package three.com.phonenumberlistviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class MyAdapter extends BaseAdapter {

    List<PhoneInfo> phoneInfoList =null;
    Context context=null;

    public MyAdapter(Context context,List<PhoneInfo> phoneInfoList ) {
        this.phoneInfoList = phoneInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return phoneInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        View rootView=convertView;
        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            rootView = layoutInflater.inflate(R.layout.phoneinfo_item, parent, false);
            holder=new ViewHolder();
            holder.nameText= (TextView) rootView.findViewById(R.id.nameText);
            holder.numberText= (TextView) rootView.findViewById(R.id.numberText);
            rootView.setTag(holder);
        }
        else {
            holder= (ViewHolder) rootView.getTag();
        }
        PhoneInfo phoneInfo = phoneInfoList.get(position);
        holder.nameText.setText(phoneInfo.getPhoneName());
        holder.numberText.setText(phoneInfo.getPhoneNumber());

        return rootView;
    }

    private static class ViewHolder {
        public TextView nameText =null;
        public TextView numberText=null;
    }
}
