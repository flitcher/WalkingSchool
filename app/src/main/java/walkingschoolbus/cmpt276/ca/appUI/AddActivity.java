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

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.UserManager;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class AddActivity extends AppCompatActivity {
    private ApiInterface proxy;
    private static String listType;
    private final String CHILDLIST = "childList";
    private final String PARENTLIST = "parentList";
    private long userId = -1;
    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), userManager.getToken());

        setContentView(R.layout.activity_add);
        setOKBtn();
        setCancelBtn();
    }

    private void setOKBtn() {
        Button btn = (Button) findViewById(R.id.AddActivity__okbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText) findViewById(R.id.AddActivity_getEmailEditText);

                    if(listType.equals(CHILDLIST)) {
                        String email = editText.getText().toString();

                        Call<User> callerForEmail = proxy.getUserByEmail(email);
                        ProxyBuilder.callProxy(AddActivity.this, callerForEmail, returnedUser -> response(returnedUser));
                        //Call<List<User>> callerForAdd = proxy.addMonitorUsers(userId);
                        //ProxyBuilder.callProxy(AddActivity.this, callerForAdd, returnedList -> responseChild(returnedList));

                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                    else if(listType.equals(PARENTLIST)) {
                        String email = editText.getText().toString();

                        Call<User> callerForEmail = proxy.getUserByEmail(email);
                        ProxyBuilder.callProxy(AddActivity.this, callerForEmail, returnedUser -> response(returnedUser));

                        //Call<List<User>> callerForAdd = proxy.addMonitoredByUsers(userId);
                        //ProxyBuilder.callProxy(AddActivity.this, callerForAdd, returnedList -> responseParent(returnedList));

                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }

        });
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
    private void response(User user) {
        userId = user.getId();
        if(listType.equals(CHILDLIST)) {
            Call<List<User>> callerForAdd = proxy.addMonitorUsers(userId,userId);
            ProxyBuilder.callProxy(AddActivity.this, callerForAdd, returnedList -> responseChild(returnedList));
        }
        else if(listType.equals(PARENTLIST)){

            Call<List<User>> callerForAdd = proxy.addMonitoredByUsers(userId,userId);
            ProxyBuilder.callProxy(AddActivity.this, callerForAdd, returnedList -> responseParent(returnedList));
        }
    }
    private void responseChild(List<User> list) {
        userManager.setMonitorUser(list);
    }
    private void responseParent(List<User> list) {
        userManager.setMonitoredByUser(list);
    }
}
