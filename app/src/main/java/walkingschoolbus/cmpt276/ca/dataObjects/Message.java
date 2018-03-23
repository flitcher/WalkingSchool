package walkingschoolbus.cmpt276.ca.dataObjects;

/**
 * Created by Jonathan Chen on 2018/3/22.
 */

public class Message  {
    private Long id;
    private String href;
    private String text;
    private Boolean emergency;
    private Long timestamp;
    private User fromUser;
    private WalkingGroups fromGroup;

    public Long getId(){return  this.id;}
    public String getHref(){return  this.href;}
    public String getText(){return this.text;}
    public Boolean getEmergency(){return  this.emergency;}
    public Long getTimestamp(){return  this.timestamp;}
    public User getFromUser (){return  this.fromUser;}
    public WalkingGroups getFromGroup(){return this.fromGroup;}

    public String getShortMessage(){
        if(text.length()<10)
            return text;
        else
            return text.substring(0,9)+"...";
    }




    public void setId(Long newId){ this.id = newId;}
    public void setHref(String newHref){this.href = newHref;}
    public void setEmergency(Boolean newEmegency){this.emergency = newEmegency;}
    public void setText(String newText) {this.text = newText;}
    public void setTimestamp(Long newTimeStamp){this.timestamp = newTimeStamp;}
    public void setFromUser(User newFromUser){this.fromUser = newFromUser;}
    public void setFromGroup(WalkingGroups newFromGroup){this.fromGroup = newFromGroup;}


    public void setMessage(Message newMessage){
        this.setId(newMessage.getId());
        this.setHref(newMessage.getHref());
        this.setText(newMessage.getText());
        this.setEmergency(newMessage.getEmergency());
        this.setTimestamp(newMessage.getTimestamp());
        this.setFromUser(newMessage.getFromUser());
        this.setFromGroup(newMessage.getFromGroup());
    }
}
