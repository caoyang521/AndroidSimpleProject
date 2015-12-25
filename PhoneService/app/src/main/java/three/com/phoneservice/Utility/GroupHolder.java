package three.com.phoneservice.Utility;

import java.util.ArrayList;

import three.com.phoneservice.Model.PeopleInfo;

/**
 * Created by Zj on 2015/12/25.
 */
public class GroupHolder {

    private static ArrayList<String> groupName;
    private static ArrayList<ArrayList<PeopleInfo>> groupItem;

    public static ArrayList<String> getGroupName(){
        if(groupName==null){
            groupName=new ArrayList<String>();
        }
        return groupName;
    }

    public static ArrayList<ArrayList<PeopleInfo>> getGroupItem(){
        if(groupItem==null){
            groupItem=new ArrayList<ArrayList<PeopleInfo>>();
        }
        return groupItem;
    }
}
