package three.com.materialdesignexample.Models;

/**
 * Created by Administrator on 2015/10/8.
 */
public class News  {

    private String path="";
    private String title="";
    private String content="";

    public static final int TITLE=0;

    public static final int CONTENT=1;

    public  static String INDEX="http://www.kyren.net";

    public  static String NEWS_INDEX="http://www.kyren.net/Category_17/Index.aspx";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
