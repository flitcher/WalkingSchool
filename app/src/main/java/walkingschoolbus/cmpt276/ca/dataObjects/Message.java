package walkingschoolbus.cmpt276.ca.dataObjects;

/**
 * Created by Jonathan Chen on 2018/3/22.
 */

public class Message {
    private Long id;
    private String href;
    private String text;
    private Boolean emergency;
    public Long getId(){return  this.id;}
    public String getHref(){return  this.href;}
    public String getText(){return this.text;}
    public Boolean getEmergency(){return  this.emergency;}
    public void setId(Long newId){ this.id = newId;}
    public void setHref(String newHref){this.href = newHref;}
    public void setEmergency(Boolean newEmegency){this.emergency = newEmegency;}
}
