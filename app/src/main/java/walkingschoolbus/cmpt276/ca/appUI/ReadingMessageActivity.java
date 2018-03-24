package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ReadingMessageActivity extends AppCompatActivity {
    private static int text;
    private static String state;
    User userManager = User.getInstance();
    private static final String READ = "READ";
    private static final String UNREAD = "UNREAD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_message);
        initilize();
    }
    private void initilize(){
        Message temp = null;
        TextView Id = (TextView) findViewById(R.id.ReadingMessage_ID);
        TextView content = (TextView) findViewById(R.id.ReadingMessage_content);
        TextView fromUser = (TextView) findViewById(R.id.ReadingMessage_FromUser);
        TextView fromGroup = (TextView) findViewById(R.id.ReadingMessage_FromGroup);
        if(state.equals(UNREAD)){
             temp = userManager.getUnreadMessages().get(text);
        }
        else if (state.equals(READ)){
             temp = userManager.getReadMessages().get(text);
        }
        Id.setText(""+ temp.getId());
        content.setText(temp.getText());
        fromUser.setText(""+temp.getFromUser().getName());
        fromGroup.setText(""+temp.getFromGroup().getId());
    }
    public static Intent makeIntent(Context context,int position,String states){
        text = position;
        state = states;
        return  new Intent(context, UnreadMessageActivity.class);
    }
}
