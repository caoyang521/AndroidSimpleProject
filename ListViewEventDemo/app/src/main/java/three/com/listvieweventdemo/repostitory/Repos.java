package three.com.listvieweventdemo.repostitory;

import java.util.ArrayList;
import java.util.List;

import three.com.listvieweventdemo.Model.BaseModel;
import three.com.listvieweventdemo.Model.DetailModel;
import three.com.listvieweventdemo.Model.HeaderModel;

/**
 * Created by Administrator on 2015/9/23.
 */
public class Repos {
    public  static List<BaseModel> getData(){
        List<BaseModel> models =new ArrayList<BaseModel>();
        for(int i=1;i<=3;i++){
            HeaderModel headerModel=new HeaderModel(0);
            headerModel.setHeader("第"+i+"章");
            models.add(headerModel);
            for(int j=1;j<=5;j++){
                DetailModel detailModel=new DetailModel(1);
                detailModel.setContext("数据项"+j);
                detailModel.setDetail("属于第"+i+"章");
                models.add(detailModel);
            }
        }
        return  models;
    }
}
