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
    private Token token = Token.getInstance();

    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();
    private List<WalkingGroups> memberOfGroups = new ArrayList<>();
    private List<WalkingGroups> leadsGroups = new ArrayList<>();

    private String href;

    private static User instance;
    private User(){};
    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }

    public void setToken(String token){

        this.token.setToken(token);
    }
    public String getToken()
    {
        return token.getToken();
    }
    public void setUser(User newUser){
        this.setName(newUser.getName());
        this.setEmail(newUser.getEmail());
        this.setId(newUser.getId());
        this.setHref(newUser.getHref());
        this.setPassword(newUser.getPassword());
        this.setMonitoredByUsers(newUser.getMonitoredByUsers());
        this.setMonitorsUsers(newUser.getMonitorsUsers());
        this.setWalkingGroups(newUser.getWalkingGroups());
    }
    public User getUser(){
        return this;
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

    public List<WalkingGroups> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<WalkingGroups> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public List<WalkingGroups> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<WalkingGroups> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public User getOneMonitorUserByIndex(int index){
        List<User> newList = this.getMonitorsUsers();
        return newList.get(index);
    }

    public User getOneMonitoredByUserByIndex(int index){
        List<User> newList = this.getMonitoredByUsers();
        return newList.get(index);
    }
    public int getMonitorUserSize(){
        return this.getMonitorsUsers().size();
    }

    public int getMonitoredByUserSize(){

        return this.getMonitoredByUsers().size();
    }

    public String[] displayMonitorUser(){
        String[] listOfMoniter = new String[this.getMonitorUserSize()];
        for(int i = 0; i <this.getMonitorUserSize();i++)
        {
            listOfMoniter[i]= "Name: " + this.getName() + "Email: " +this.getEmail();
        }
        return listOfMoniter;
    }

    public String[] displayMonitoredByUser(){
        String[] listOfMoniter = new String[this.getMonitoredByUserSize()];
        for(int i = 0; i <this.getMonitoredByUserSize();i++)
        {
            listOfMoniter[i]= "Name: " + this.getName() + "Email: " +this.getEmail();
        }
        return listOfMoniter;
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
                ", memberOfGroups=" + memberOfGroups +
                ", leadsGroups=" + leadsGroups +
                ", href='" + href + '\'' +
                '}';
    }
}

