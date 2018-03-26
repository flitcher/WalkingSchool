package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class MemberReportActivity extends AppCompatActivity {
    private static final String GROUPID = "groupid";
    private Long groupId;
    private String message;
    private User userManager = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_report);

        ServerManager.connectToServerWithToken(MemberReportActivity.this);
        setUpButton();
        extractIntent();
    }
    private void setUpButton() {
        CheckBox toParent = (CheckBox)findViewById(R.id.MemberReportActivity_checkToParent);
        Button send = (Button) findViewById(R.id.MemberReportActivity_send);
        EditText text = (EditText) findViewById(R.id.MemberReportActivity_Message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = text.getText().toString();
                if (toParent.isChecked())
                {
                    ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseMessageToParent(returnedMessage);
                    ServerManager.sendMessageToParent(userManager.getId(),message,callback);
                }
                else{
                    ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseMessageToGroup(returnedMessage);
                    ServerManager.sendMessageToGroup(groupId,message,callback);
                }
            }
        });

    }
    private void extractIntent(){
        Intent intent = getIntent();
        groupId = intent.getLongExtra(GROUPID,-1);
    }
    public static Intent makeIntent(Context context,Long groupID){
        Intent intent = new Intent(context,MemberReportActivity.class);
        intent.putExtra(GROUPID,groupID);
        return intent;
    }
    //return
    private void responseMessageToParent(Message returnMessage){
        ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseMessageToGroup(returnedMessage);
        ServerManager.sendMessageToGroup(groupId,message,callback);
    }
    private void responseMessageToGroup(Message retueMessage){
        Log.i("User","send successful!");
        finish();
    }
}
