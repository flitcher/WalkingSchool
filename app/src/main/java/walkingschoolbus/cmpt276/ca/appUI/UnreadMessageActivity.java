package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class UnreadMessageActivity extends AppCompatActivity {
    User userManager = User.getInstance();
    List<Message> unreadMessage;
    private final static String UNREAD = "UNREAD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unread_message);
        ServerManager.connectToServerWithToken(UnreadMessageActivity.this);
        initialize();
        clickMessage();
    }
    private void initialize(){
        ProxyBuilder.SimpleCallback<List<Message>> callback = returnedListMessage->responseUnreadMessage(returnedListMessage);
        ServerManager.refreshUnreadMessage(userManager.getId(),callback);
        unreadMessage = userManager.getUnreadMessages();
        updateList();
    }
    private void updateList(){
        ArrayAdapter<Message> adapter = new MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.UnreadMessage_list);
        listView.setAdapter(adapter);
    }
    private void clickMessage(){
        ListView listView = findViewById(R.id.UnreadMessage_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ReadingMessageActivity.makeIntent(UnreadMessageActivity.this,position,UNREAD);
                startActivity(intent);
            }
        });
    }
    private class MyListAdapter extends ArrayAdapter<Message> {
        public MyListAdapter(){
            super(UnreadMessageActivity.this,R.layout.message_layout, unreadMessage);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View messageView = convertView;
            if(messageView  == null){
                messageView  = getLayoutInflater().inflate(R.layout.message_layout,parent,false);
            }


            TextView ID = (TextView) messageView .findViewById(R.id.message_layout_ID);
            ID.setText("ID: "+ unreadMessage.get(position).getId());

            TextView message = (TextView) messageView .findViewById(R.id.message_layour_Mesage);
            message.setText("Content: "+unreadMessage.get(position).getShortMessage());

            ImageView Emergency = (ImageView) messageView .findViewById(R.id.message_layout_Emergency);
            if(unreadMessage.get(position).getEmergency())
                Emergency.setImageResource(android.R.drawable.btn_star_big_on);
            else
                Emergency.setImageResource(android.R.drawable.btn_star_big_off);
            return messageView;
        }
    }
    public static Intent makeIntent(Context context){
        return new Intent(context,UnreadMessageActivity.class);
    }

    //return
    private void responseUnreadMessage(List<Message> messageList){
        userManager.setUnreadMessages(messageList);
    }


    @Override
    protected void onResume() {
        initialize();
        super.onResume();
    }
}
