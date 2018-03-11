package walkingschoolbus.cmpt276.ca.dataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kawai on 3/1/2018.
 */

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;

    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();
    private List<WalkingGroups> walkingGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED

    private String href;

    private static User instance;
    private User(){};
    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public void setMonitoredByUsers(List<User> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public List<WalkingGroups> getWalkingGroups() {
        return walkingGroups;
    }

    public void setWalkingGroups(List<WalkingGroups> walkingGroups) {
        this.walkingGroups = walkingGroups;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", monitoredByUsers=" + monitoredByUsers +
                ", monitorsUsers=" + monitorsUsers +
                ", walkingGroups=" + walkingGroups +
                '}';
    }
}

