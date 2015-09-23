package three.com.listvieweventdemo.Model;

/**
 * Created by Administrator on 2015/9/23.
 */
public class DetailModel extends BaseModel {
    private String context="";
    private String detail="";

    public DetailModel(int type) {
        super(type);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
