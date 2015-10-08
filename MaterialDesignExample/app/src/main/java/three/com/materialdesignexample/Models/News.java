package three.com.materialdesignexample.Models;

/**
 * Created by Administrator on 2015/10/8.
 */
public class News {

    private String path="";
    private String title="";
    private String content="";

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
