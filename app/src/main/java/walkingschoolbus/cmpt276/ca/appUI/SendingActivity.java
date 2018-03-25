package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class SendingActivity extends AppCompatActivity {

    private Long groupID;
    private static final String GROUPID = "GROUPID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        ServerManager.connectToServerWithToken(SendingActivity.this);
        extractIntent();
        setUpButton();
    }

    private void setUpButton() {
        CheckBox toParent = (CheckBox)findViewById(R.id.SendingActivity_checkToParent);
        Button send = (Button) findViewById(R.id.SendingActivity_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toParent.isChecked())
                {
                    ProxyBuilder.SimpleCallback<List<User>> callback = returnedList->responseMember(returnedList);
                    ServerManager.getGroupMember(groupID,callback);
                }
                else{
                    ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseMessageToGroup(returnedMessage);
                    ServerManager.sendMessageToGroup(groupID,callback);
                }
            }
        });
        ;

    }

    private void extractIntent(){
        Intent intent = getIntent();
        groupID = intent.getLongExtra(GROUPID,-1);
    }

    public static Intent makeIntent(Context context,Long groupID){
        Intent intent = new Intent(context,SendingActivity.class);
        intent.putExtra(GROUPID,groupID);
        return  intent;
    }

    //return thing
    private void responseMessageToGroup(Message message){
        Log.i("User","send successful!");
    }

    private void responseMember(List<User> memberList){
        ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseMessageToGroupParent(returnedMessage);
        ServerManager.sendMessageToGroup(groupID,callback);
    }
    private void responseMessageToGroupParent(Message message){

    }
}
