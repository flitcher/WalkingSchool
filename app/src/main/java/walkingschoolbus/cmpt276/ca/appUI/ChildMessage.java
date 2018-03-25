package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ChildMessage extends AppCompatActivity {
    private User myUser = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_message);


        initialize();
        emergencyMessage();

    }

    private void initialize() {
        EditText message = (EditText) findViewById(R.id.ChildMessage_Message);
        Button button = (Button)findViewById(R.id.ChildMessage_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendEmergencyMessage(message.getText().toString());

            }
        });

    }

    private void emergencyMessage(){
        EditText message = (EditText) findViewById(R.id.ChildMessage_Message);
        String text = "Warning!!! "+ myUser.getName()+ " need help!! "+"ID: "+myUser.getId()+" Lat: "+ myUser.getLastGpsLocation().getLat()+" Lng: "+ myUser.getLastGpsLocation().getLng();
        message.setText(text);
    }

    private void sendEmergencyMessage(String text){

        for(int i = 0; i < myUser.getMemberOfGroups().size(); i++) {
            ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseCallGroup(returnedMessage);
            ServerManager.callToGroup(myUser.getMemberOfGroups().get(i).getId(),text,callback);
        }
        ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseCallGroup(returnedMessage);
        ServerManager.callToParent(myUser.getId(),text,callback);
        finish();
    }

    public static Intent makeIntent(Context context){
      return new Intent(context,ChildMessage.class);

    }
    //return
    private void responseCallGroup(Message message){
        Log.i("User","send successful!");
    }
}
