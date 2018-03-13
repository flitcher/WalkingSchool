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
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class AddActivity extends AppCompatActivity {
    private static String listType;
    private final String CHILDLIST = "childList";
    private final String PARENTLIST = "parentList";
    private User userManager = User.getInstance();

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

                EditText editText = (EditText) findViewById(R.id.AddActivity_getEmailEditText);

                    if(listType.equals(CHILDLIST)) {
                        String email = editText.getText().toString();
                        if(email != userManager.getEmail())
                            ServerManager.addMonitorUser(email);
                        else
                            Toast.makeText(AddActivity.this, "cannot add themselve", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                    else if(listType.equals(PARENTLIST)) {
                        String email = editText.getText().toString();

                        ServerManager.addMonitedByUser(email);

                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK,intent);
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


}
