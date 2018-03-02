package walkingschoolbus.cmpt276.ca.walkingschoolbusLogic;

/**
 * Created by Kawai on 3/1/2018.
 */

public class User{

    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(){

    }
}
