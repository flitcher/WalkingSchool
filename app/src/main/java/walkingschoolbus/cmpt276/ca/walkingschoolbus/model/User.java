package walkingschoolbus.cmpt276.ca.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan Chen on 2018/3/6.
 */

public class User {
    private String email;
    private String name;
    private List<User> parent = new ArrayList<User>();
    private List<User> children = new ArrayList<User>();
    public void removeParent(User rmParent){parent.remove(rmParent);}
    public void removeChildren(User rmChildren){children.remove(rmChildren);}
    public void addParent(User newParent){ parent.add(newParent);}
    public void addchildren(User newChildren){ children.add(newChildren);}
    public String getEmail(){return this.email;}
    public String getName(){return this.name;}
    public String[] getParentList(){
        String[] parentList = new  String[parent.size()];
        for(int i = 0; i < parent.size();i++){
            User user = parent.get(i);
            parentList[i] = user.getName()+"  "+user.getEmail();
        }
        return parentList;
    }

}
