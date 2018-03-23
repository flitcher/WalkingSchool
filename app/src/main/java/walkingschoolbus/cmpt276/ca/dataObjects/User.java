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
    private Long birthYear;
    private Long birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;

    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();
    private List<WalkingGroups> memberOfGroups = new ArrayList<>();
    private List<WalkingGroups> leadsGroups = new ArrayList<>();
    private List<Message> unreadMessages = new ArrayList<>();
    private List<Message> readMessages = new ArrayList<>();
    private String href;

    private static User instance;
    private User(){};
    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }


    //set section
    public void setToken(String token){

        this.token.setToken(token);
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }
    public void setLeadsGroups(List<WalkingGroups> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }
    public void setMonitoredByUsers(List<User> monitoredByUsers) {this.monitoredByUsers = monitoredByUsers;}
    public void setHref(String href) {
        this.href = href;
    }
    public void setMemberOfGroups(List<WalkingGroups> memberOfGroups) {this.memberOfGroups = memberOfGroups;}
    public void setBirthYear(Long newBirthYear){ this.birthYear = newBirthYear;}
    public void setBirthMonth(Long newBirthMonth){this.birthMonth = newBirthMonth;}
    public void setAddress(String newAddress){this.address = newAddress;}
    public void setCellPhone(String newCellphone){this.cellPhone = newCellphone;}
    public void setHomePhone(String newHomePhone){this.homePhone = newHomePhone;}
    public void setGrade(String newGrad){this.}

    public void setUser(User newUser){
        this.setName(newUser.getName());
        this.setEmail(newUser.getEmail());
        this.setId(newUser.getId());
        this.setHref(newUser.getHref());
        this.setPassword(newUser.getPassword());
        this.setMonitoredByUsers(newUser.getMonitoredByUsers());
        this.setMonitorsUsers(newUser.getMonitorsUsers());
        this.setMemberOfGroups(newUser.getMemberOfGroups());
        this.setLeadsGroups(newUser.getLeadsGroups());
    }
    public User getUser(){
        return this;
    }
    public Long getId() {
        return id;
    }


    public String getToken()
    {
        return token.getToken();
    }
    public String getName() {
        return name;
    }



    public String getEmail() {
        return email;
    }



    public String getPassword() {
        return password;
    }



    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }



    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }



    public List<WalkingGroups> getMemberOfGroups() {
        return memberOfGroups;
    }



    public List<WalkingGroups> getLeadsGroups() {
        return leadsGroups;
    }



    public String getHref() {
        return href;
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

