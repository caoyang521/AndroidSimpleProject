package three.com.phoneservice.Model;

/**
 * Created by Administrator on 2015/9/24.
 */
public class PeopleInfo {

    private int id=0;
    private String Number="";
    private String Name="";

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
