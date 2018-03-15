package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class DeleteActivity extends AppCompatActivity {
    private static User user;
    private static User userManager = User.getInstance();
    private static String listType;
    private final String CHILDLIST = "childList";
    private final String PARENTLIST = "parentList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        ServerManager.connectToServerWithToken(DeleteActivity.this);
        setDeleteBtn();
        setCancelBtn();
        setTextView();
    }

    private void setTextView() {
        TextView name =(TextView) findViewById(R.id.Delete_Name);
        TextView email =(TextView) findViewById(R.id.Delete_Email);
        TextView id =(TextView) findViewById(R.id.Delete_ID);
        name.setText("Name: "+user.getName());
        email.setText("Email: "+user.getEmail());
        id.setText("ID      : "+user.getId());
    }

    private void setCancelBtn() {
        Button btn = (Button) findViewById(R.id.Delete_canelbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDeleteBtn() {
        Button btn = (Button) findViewById(R.id.Delete_deletebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(listType.equals(PARENTLIST))
               {
                   ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> deleteParent(returnedNothing);
                   ServerManager.deleteMonitoredByUser(user.getId(),callback);

               }
               else if(listType.equals(CHILDLIST))
               {
                   ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> deleteChild(returnedNothing);
                   ServerManager.deleteMoniterUser(user.getId(),callback);
               }
            }
        });
    }

    public static Intent makeIntent(Context context,User tempUser,String newListType){
        user = tempUser;
        listType = newListType;
        return new Intent(context, DeleteActivity.class);
    }

    //return things
    private void deleteChild(Void Nothing){
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList -> resetChildList(returnedList);
        ServerManager.deleteChild(callback);
    }
    private void resetChildList(List<User> list) {
        userManager.setMonitorsUsers(list);
        finish();
    }
    private void  deleteParent(Void Nothing){
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList -> resetParentList(returnedList);
        ServerManager.deleteParent(callback);
    }
    private void resetParentList(List<User> list) {
        userManager.setMonitoredByUsers(list);
        finish();
    }
}
