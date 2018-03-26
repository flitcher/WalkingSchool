package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class readMessageActivity extends AppCompatActivity {
    User userManager = User.getInstance();
    List<Message> readMessage;
    private final static String READ = "READ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        ServerManager.connectToServerWithToken(readMessageActivity.this);
        initialize();
        clickMessage();

    }
    private void initialize(){
        ProxyBuilder.SimpleCallback<List<Message>> callback = returnedListMessage->responseReadMessage(returnedListMessage);
        ServerManager.refreshReadMessage(userManager.getId(),callback);
        readMessage = userManager.getReadMessages();
        updateList();
    }

    private void updateList(){
        ArrayAdapter<Message> adapter = new readMessageActivity.MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.ReadMessage_list);
        listView.setAdapter(adapter);
    }
    private void clickMessage(){
        ListView listView = findViewById(R.id.ReadMessage_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ReadingMessageActivity.makeIntent(readMessageActivity.this,position,READ);
                startActivity(intent);
            }
        });
    }
    private class MyListAdapter extends ArrayAdapter<Message> {
        public MyListAdapter(){
            super(readMessageActivity.this,R.layout.message_layout, readMessage);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View messageView = convertView;
            if(messageView  == null){
                messageView  = getLayoutInflater().inflate(R.layout.message_layout,parent,false);
            }


            TextView ID = (TextView) messageView .findViewById(R.id.message_layout_ID);
            ID.setText("ID: "+ readMessage.get(position).getId());

            TextView message = (TextView) messageView .findViewById(R.id.message_layour_Mesage);
            message.setText("Content: "+readMessage.get(position).getShortMessage());

            ImageView Emergency = (ImageView) messageView .findViewById(R.id.message_layout_Emergency);
            if(readMessage.get(position).getEmergency())
                Emergency.setImageResource(android.R.drawable.btn_star_big_on);
            else
                Emergency.setImageResource(android.R.drawable.btn_star_big_off);

            return messageView;
        }
    }
    public static Intent makeIntent(Context context){
        return new Intent(context,readMessageActivity.class);
    }

    //return
    private void responseReadMessage(List<Message> messageList){
        userManager.setReadMessages(messageList);
    }

}
