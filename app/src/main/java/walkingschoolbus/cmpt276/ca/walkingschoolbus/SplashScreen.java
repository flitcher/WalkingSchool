package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
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
