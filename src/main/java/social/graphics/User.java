package social.graphics;

public class User {
    public String name, sid;
    public boolean selected = true;
    public User(String name, String sid, boolean selected) {
        this.name = name;
        this.sid = sid;
        this.selected = selected;
    }
}
