package social.graphics;

import java.util.ArrayList;

public class Group {
    public String sid, name;
    public ArrayList<User> accounts;

    public Group(String sid, String name, ArrayList<User> accounts) {
        this.sid = sid;
        this.name = name;
        this.accounts = accounts;
    }
    public User getUserByName(String name){
        for(User user : accounts){
            if(user.name.equals(name)){
                return user;
            }
        }
        return null;
    }
}
