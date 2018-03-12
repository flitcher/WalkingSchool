package walkingschoolbus.cmpt276.ca.dataObjects;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Jonathan Chen on 2018/3/9.
 */

public class UserManager {
    private  User user;
    private Token token ;

    //singleton
    private static UserManager instance;
    private UserManager(){
         user = user.getInstance();
         token = Token.getInstance();
    }
    public  static UserManager getInstance(){
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User newUser){
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setId(newUser.getId());
        user.setHref(newUser.getHref());
        user.setPassword(newUser.getPassword());
        user.setMonitoredByUsers(newUser.getMonitoredByUsers());
        user.setMonitorsUsers(newUser.getMonitorsUsers());
        user.setWalkingGroups(newUser.getWalkingGroups());
    }

    public Long getUserId(){
        return user.getId();
    }

    public User getUser(){return user;}

    public String getUserEmail(){return user.getEmail();}

    public String getUserPassword(){return user.getPassword();}

    public void setUserId(Long id ){user.setId(id);}

    public void setUserName(String name ){user.setName(name);}
    
    public void setUserPassword(String password){user.setPassword(password);}

    public void setUserEmail(String email){user.setEmail(email);}

    public void logInWithCurrentUser(User newUser){
        this.user = newUser;
    }

    public List<User> getMonitorUserList(){
        return user.getMonitorsUsers();
    }

    public List<User> getMonitorByUserList(){
        return user.getMonitoredByUsers();
    }

    public User getOneMonitorUserByIndex(int index){
        List<User> newList = this.getMonitorUserList();
        return newList.get(index);
    }

    public User getOneMonitoredByUserByIndex(int index){
        List<User> newList = this.getMonitorByUserList();
        return newList.get(index);
    }

    public int getMonitorUserSize(){
        return user.getMonitorsUsers().size();
    }

    public int getMonitoredByUserSize(){
        return user.getMonitoredByUsers().size();
    }

    public String[] displayMonitorUser(){
        String[] listOfMoniter = new String[this.getMonitorUserSize()];
        for(int i = 0; i <this.getMonitorUserSize();i++)
        {
            listOfMoniter[i]= this.getMonitorUserList().get(i).toString();
        }
        return listOfMoniter;
    }

    public String[] displayMonitoredByUser(){
        String[] listOfMoniter = new String[this.getMonitoredByUserSize()];
        for(int i = 0; i <this.getMonitoredByUserSize();i++)
        {
            listOfMoniter[i]= this.getMonitorByUserList().get(i).toString();
        }
        return listOfMoniter;
    }

    public void setToken(String token){
        this.token.setToken(token);
    }

    public String getToken(){
        return token.getToken();
    }

    public void setMonitorUser(List<User> list){
        user.setMonitorsUsers(list);
    }

    public void setMonitoredByUser(List<User> list){
        user.setMonitoredByUsers(list);
    }

}
