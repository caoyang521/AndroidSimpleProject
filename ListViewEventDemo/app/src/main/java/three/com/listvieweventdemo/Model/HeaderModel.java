package three.com.listvieweventdemo.Model;

/**
 * Created by Administrator on 2015/9/23.
 */
public class HeaderModel extends BaseModel {
    private String header="";

    public HeaderModel(int type) {
        super(type);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
