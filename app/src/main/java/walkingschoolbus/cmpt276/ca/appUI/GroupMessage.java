package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Allows for group messaging
 */
public class GroupMessage extends AppCompatActivity {

    private static List<Message> groupMessage;
    private final static String GROUPMESSAGE = "GROUPMESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        clickMessage();
        updateList();
    }

    private void clickMessage(){
        ListView listView = findViewById(R.id.GroupMessage_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ReadingMessageActivity.makeIntent(GroupMessage.this,position,GROUPMESSAGE,groupMessage);
                startActivity(intent);
            }
        });
    }
    private void updateList(){
        ArrayAdapter<Message> adapter = new GroupMessage.MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.GroupMessage_list);
        listView.setAdapter(adapter);
    }
    private class MyListAdapter extends ArrayAdapter<Message> {
        public MyListAdapter(){
            super(GroupMessage.this,R.layout.message_layout, groupMessage);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View messageView = convertView;
            if(messageView  == null){
                messageView  = getLayoutInflater().inflate(R.layout.message_layout,parent,false);
            }


            TextView ID = (TextView) messageView .findViewById(R.id.message_layout_ID);
            ID.setText("ID: "+ groupMessage.get(position).getId());

            TextView message = (TextView) messageView .findViewById(R.id.message_layour_Mesage);
            message.setText("Content: "+groupMessage.get(position).getShortMessage());

            ImageView Emergency = (ImageView) messageView .findViewById(R.id.message_layout_Emergency);

            if(groupMessage.get(position).getEmergency())
                Emergency.setImageResource(android.R.drawable.btn_star_big_on);
            else
                Emergency.setImageResource(android.R.drawable.btn_star_big_off);

            return messageView;
        }
    }
    public static Intent makeIntent(Context context,List<Message> messageList){
        groupMessage = messageList;
        return new Intent(context,GroupMessage.class);
    }
}
