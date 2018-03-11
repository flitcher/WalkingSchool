package walkingschoolbus.cmpt276.ca.dataObjects;

/**
 * Created by seungdobaek on 2018-03-10.
 */

public class Token {
    String token;

    private static Token instance;
    private Token(){};
    public static Token getInstance(){
        if (instance == null){
            instance = new Token();
        }
        return instance;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
