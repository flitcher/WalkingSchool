package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.dataObjects.User.emailPattern;

public class AddActivity extends AppCompatActivity {
    private static String listType;
    private final String CHILDLIST = "childList";
    private final String PARENTLIST = "parentList";
    private User userManager = User.getInstance();

    EditText userEmail;
    String validateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServerManager.connectToServerWithToken(AddActivity.this);

        setContentView(R.layout.activity_add);
        setOKBtn();
        setCancelBtn();
    }

    private void setOKBtn() {
        Button btn = (Button) findViewById(R.id.AddActivity__okbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userEmail = (EditText) findViewById(R.id.AddActivity_getEmailEditText);
                initialize();
                    if(validate())
                    {
                        if(listType.equals(CHILDLIST)) {
                            String email = userEmail.getText().toString();

                            ProxyBuilder.SimpleCallback<User> callback = returnedUser -> addChild(returnedUser);
                            ServerManager.addMonitorUser(email,callback);

                        }
                        else if(listType.equals(PARENTLIST)) {
                            String email = userEmail.getText().toString();

                            ProxyBuilder.SimpleCallback<User> callback = returnedUser -> addParent(returnedUser);
                            ServerManager.addMonitedByUser(email,callback);

                        }
                    }
                }

        });
    }

    private boolean validate() {
        boolean valid = true;
        if(!validateEmail.matches(emailPattern)) {
            userEmail.setError("Please enter a valid email");
            valid = false;
        }
        return valid;
    }

    public void initialize() {
        validateEmail = userEmail.getText().toString().trim();
    }

    private void setCancelBtn() {
        Button btn = (Button) findViewById(R.id.AddActivity__cancelbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context, String newListType) {
        listType=newListType;
        Log.i("myapp",listType);
        return new Intent(context, AddActivity.class);

    }


    //return things

    //child
    private void addChild(User user) {
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList -> resetChildList(returnedList);
        ServerManager.addChild(user,callback);
    }
    private void resetChildList(List<User> list) {
        userManager.setMonitorsUsers(list);
        finish();
    }

    //parent
    private void addParent(User user) {

        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList -> resetParentList(returnedList);
        ServerManager.addParent(user,callback);

    }
    private void resetParentList(List<User> list) {
        userManager.setMonitoredByUsers(list);
        finish();
    }


}
