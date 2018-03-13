package walkingschoolbus.cmpt276.ca.dataObjects;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Jonathan Chen on 2018/3/9.
 */

public class UserManager {
    private  User user;
    private String token;

    //singleton
    private static UserManager instance;
    private UserManager(){
         user = user.getInstance();
    }
    public  static UserManager getInstance(){
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User newUser){
        user = newUser;
    }

    public long getUserId(){
        return user.getId();
    }

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
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public void setMonitorUser(List<User> list){
        user.setMonitorsUsers(list);
    }
    public void setMonitoredByUser(List<User> list){
        user.setMonitoredByUsers(list);
    }

}
