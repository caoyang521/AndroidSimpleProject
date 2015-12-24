package three.com.phoneservice.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import three.com.phoneservice.CallBack;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Params.AppParams;
import three.com.phoneservice.R;
import three.com.phoneservice.Utility.ViewHolder;

/**
 * Created by Administrator on 2015/11/6.
 */
public class PhoneAdapter extends BaseAdapter{


    private Context context;
    private List<PeopleInfo> phoneInfos =new ArrayList<PeopleInfo>();
    private String type;
    private ArrayList<PeopleInfo> addToNewClassPeople=null;
    private CallBack callBack=null;

    public PhoneAdapter(Context context, List<PeopleInfo> phoneInfos,String type) {
        this.context = context;
        this.phoneInfos = phoneInfos;
        this.type=type;
    }
    public PhoneAdapter(Context context, List<PeopleInfo> phoneInfos,String type,ArrayList<PeopleInfo> addToNewClassPeople,CallBack callBack) {
        this.context = context;
        this.phoneInfos = phoneInfos;
        this.type=type;
        this.addToNewClassPeople=addToNewClassPeople;
        this.callBack=callBack;
    }

    @Override
    public int getCount() {
        return phoneInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.phone_item_list, null);
            holder.head_name_tv = (TextView) convertView.findViewById(R.id.head_name_tv);
            holder.class_name_tv = (TextView) convertView.findViewById(R.id.class_name_tv);
            holder.phone_number_tv = (TextView) convertView.findViewById(R.id.phone_number_tv);
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.school_number_tv= (TextView) convertView.findViewById(R.id.school_number_tv);
            holder.selected= (CheckBox) convertView.findViewById(R.id.list_select);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }



        Random random = new Random();
        int circleShape = 0;
        int count=random.nextInt(10);
        int color = 0;

        if (count>=8) {
            circleShape = R.drawable.circle_deep_orange;
            color = context.getResources().getColor(R.color.deep_orange_500);
        } else if (count>=6 &&count<8) {
            circleShape = R.drawable.circle_teal;
            color = context.getResources().getColor(R.color.indigo_500);
        } else if (count>=4 &&count<6) {
            circleShape = R.drawable.circle_cyan;
            color = context.getResources().getColor(R.color.cyan_500);
        } else if (count>=2 &&count<4) {
            circleShape = R.drawable.circle_blue;
            color = context.getResources().getColor(R.color.blue_500);
        } else  {
            circleShape = R.drawable.circle_amber;
            color = context.getResources().getColor(R.color.amber_500);
        }
        if(AppParams.SEDN_TYPE.equals(type)){
            holder.selected.setVisibility(View.VISIBLE);
        }
        final PeopleInfo phoneInfo= phoneInfos.get(position);
        holder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                phoneInfo.setIsChecked(cb.isChecked());
                if (addToNewClassPeople != null) {
                    if (cb.isChecked()) {
                        addToNewClassPeople.add(phoneInfo);
                    } else {
                        IsInTheNewPeoPle(phoneInfo);
                    }
                    if (callBack != null) {
                        callBack.onStart();
                    }
                }
            }
        });
        boolean isChecked=false;
        if(addToNewClassPeople !=null){
            isChecked = findIsChecked(phoneInfo, isChecked);
        }

        if(isChecked){
            holder.selected.setChecked(isChecked);
        }
        else{
            holder.selected.setChecked(phoneInfo.isChecked());
        }
        holder.head_name_tv.setBackgroundResource(circleShape);
        holder.head_name_tv.setText(phoneInfo.getPeopleName().substring(0,1));
        holder.class_name_tv.setText(phoneInfo.getClassName());
        holder.phone_number_tv.setText(phoneInfo.getPhoneNumber());
        holder.name_tv.setText(phoneInfo.getPeopleName());
        holder.school_number_tv.setText(phoneInfo.getSchoolNumber());

        return convertView;
    }

    private void IsInTheNewPeoPle(PeopleInfo phoneInfo) {
        for(PeopleInfo newPeople : addToNewClassPeople){
            if(newPeople.getSchoolNumber().equals(phoneInfo.getSchoolNumber())){
                addToNewClassPeople.remove(newPeople);
                break;
            }
        }
    }

    private boolean findIsChecked(PeopleInfo phoneInfo, boolean isChecked) {
        for(PeopleInfo newPeople : addToNewClassPeople){
            if(newPeople.getSchoolNumber().equals(phoneInfo.getSchoolNumber())){
                isChecked=true;
                break;
            }
        }
        return isChecked;
    }

}