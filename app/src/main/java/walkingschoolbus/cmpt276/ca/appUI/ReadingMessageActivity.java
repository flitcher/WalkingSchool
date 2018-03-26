package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

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
    private final static String GROUPMESSAGE = "GROUPMESSAGE";
    private static final String STATES = "STATES";
    private static final String POSITION= "POSITION";
    private static Message temp = null;
    private static List<Message> tempList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_message);
        ServerManager.connectToServerWithToken(ReadingMessageActivity.this);
        extractIntent();
        initilize();
    }
    private void initilize(){
        TextView Id = (TextView) findViewById(R.id.ReadingMessage_ID);
        TextView content = (TextView) findViewById(R.id.ReadingMessage_content);
        TextView fromUser = (TextView) findViewById(R.id.ReadingMessage_FromUser);
        TextView fromGroup = (TextView) findViewById(R.id.ReadingMessage_FromGroup);
        if(state.equals(UNREAD)){
             temp = tempList.get(position);

             Id.setText("Message ID: "+ temp.getId());
             content.setText(temp.getText());
             fromUser.setText("From User: "+temp.getFromUser().getId());
             fromGroup.setText("From Group: "+temp.getFromGroup().getId());

             ProxyBuilder.SimpleCallback<User> callback = returnedUser->responseMarking(returnedUser);
             ServerManager.markUnreadMessage(userManager.getId(),temp.getId(),callback);
        }
        else if (state.equals(READ)){
             temp = tempList.get(position);
             Id.setText("Message ID: "+ temp.getId());
             content.setText(temp.getText());
             fromUser.setText("From User: "+temp.getFromUser().getId());
             fromGroup.setText("From Group: "+temp.getFromGroup().getId());
        }
        else if (state.equals(GROUPMESSAGE)){
            temp = tempList.get(position);
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
    public static Intent makeIntent(Context context,int position,String states,List<Message> messageList){
        Intent intent = new Intent(context, ReadingMessageActivity.class);
        intent.putExtra(STATES,states);
        intent.putExtra(POSITION,position);
        tempList = messageList;
        return intent;
    }

    //return
    private void responseMarking(User user){
        ProxyBuilder.SimpleCallback<List<Message>> callback = returnedUnreadList->responseUnread(returnedUnreadList);
        ServerManager.refreshUnreadMessage(userManager.getId(),callback);
    }
    private void responseUnread(List<Message> unreadList){
        userManager.setUnreadMessages(unreadList);
        ProxyBuilder.SimpleCallback<List<Message>> callback = returnedReadList->responseRead(returnedReadList);
        ServerManager.refreshReadMessage(userManager.getId(),callback);
    }
    private void responseRead(List<Message> readList){
        userManager.setReadMessages(readList);
    }




}
