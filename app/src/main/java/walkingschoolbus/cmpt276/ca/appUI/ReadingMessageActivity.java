package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ReadingMessageActivity extends AppCompatActivity {
    private static int position;
    private static String state;
    User userManager = User.getInstance();
    private static final String READ = "READ";
    private static final String UNREAD = "UNREAD";
    private static final String STATES = "STATES";
    private static final String POSITION= "POSITION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_message);
        ServerManager.connectToServerWithToken(ReadingMessageActivity.this);
        extractIntent();
        initilize();
    }
    private void initilize(){
        Message temp = null;
        TextView Id = (TextView) findViewById(R.id.ReadingMessage_ID);
        TextView content = (TextView) findViewById(R.id.ReadingMessage_content);
        TextView fromUser = (TextView) findViewById(R.id.ReadingMessage_FromUser);
        TextView fromGroup = (TextView) findViewById(R.id.ReadingMessage_FromGroup);
        if(state.equals(UNREAD)){
             temp = userManager.getUnreadMessages().get(position);

             Id.setText("Message ID: "+ temp.getId());
             content.setText(temp.getText());
             fromUser.setText("From User: "+temp.getFromUser().getId());
             fromGroup.setText("From Group: "+temp.getFromGroup().getId());

             ProxyBuilder.SimpleCallback<User> callback = returnedUser->responseMarking(returnedUser);
             ServerManager.markUnreadMessage(userManager.getId(),temp.getId(),callback);
        }
        else if (state.equals(READ)){
             temp = userManager.getReadMessages().get(position);
             Id.setText("Message ID: "+ temp.getId());
             content.setText(temp.getText());
             fromUser.setText("From User: "+temp.getFromUser().getId());
             fromGroup.setText("From Group: "+temp.getFromGroup().getId());
        }

    }

    private void extractIntent(){
        Intent intent = getIntent();
        position = intent.getIntExtra(POSITION,-1);
        state = intent.getStringExtra(STATES);
    }
    public static Intent makeIntent(Context context,int position,String states){
        Intent intent = new Intent(context, ReadingMessageActivity.class);
        intent.putExtra(STATES,states);
        intent.putExtra(POSITION,position);
        return intent;
    }

    //return
    private void responseMarking(User user){
        userManager.setUser(user);
    }
}
