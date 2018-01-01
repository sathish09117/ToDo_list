package firebase.smdeveloper.vvsn.todo_list.Model;

/**
 * Created by sathish kumar on 29-11-2017.
 */

public class ToDo {
    private  String id,title,desc;

    public ToDo() {
    }

    public ToDo(String id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
