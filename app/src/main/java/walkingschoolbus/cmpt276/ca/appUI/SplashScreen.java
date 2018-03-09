package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(){
            @Override
            public void run(){
                try{

                    sleep(SPLASH_SCREEN_TIME);

                    //TODO: (if previously signed in) auto login after existing app
                    SharedPreferences info = getSharedPreferences(RegisterActivity.PREFS_USER_KEY, MODE_PRIVATE);
                    String password = info.getString(RegisterActivity.USERNAME_KEY, null);
                    String email = info.getString(RegisterActivity.EMAIL_KEY,null);

                    //check with db




                    Intent intent = new Intent(SplashScreen.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

}
