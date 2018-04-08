package walkingschoolbus.cmpt276.ca.dataObjects;

import java.util.List;

/**
 * Created by Jonathan Chen on 2018/4/7.
 */

public class Permission {

    private Long id;
    private String action;
    private PermissionStatus status;
    private User userA;
    private User userB;
    private WalkingGroups groupG;
    private User requestingUser;
    private List<Authorization> authorizors;
    private String message;
    private String href;


    public Long getId() {
        return id;}
    public String getAction() {
        return action;
    }
    public PermissionStatus getStatus() {
        return status;
    }
    public User getUserA() {
        return userA;
    }
    public User getUserB() {
        return userB;
    }
    public WalkingGroups getGroupG() {
        return groupG;
    }
    public User getRequestingUser() {
        return requestingUser;
    }
    public List<Authorization>getAuthorizors() {
        return authorizors;
    }
    public String getMessage() {
        return message;
    }
    public String getHref() {
        return href;
    }


    public void setId(Long id) {
        this.id = id;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public void setStatus(PermissionStatus status) {
        this.status = status;
    }
    public void setUserA(User userA) {
        this.userA = userA;
    }
    public void setUserB(User userB) {
        this.userB = userB;
    }
    public void setGroupG(WalkingGroups groupG) {
        this.groupG = groupG;
    }
    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }
    public void setAuthorizors(List<Authorization> authorizors) {
        this.authorizors = authorizors;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setHref(String href) {
        this.href = href;
    }

    public void setPermission(Permission newPermission){
        this.setId(newPermission.getId());
        this.setAction(newPermission.getAction());
        this.setStatus(newPermission.getStatus());
        this.setUserA(newPermission.getUserA());
        this.setUserB(newPermission.getUserB());
        this.setGroupG(newPermission.getGroupG());
        this.setRequestingUser(newPermission.getRequestingUser());
        this.setAuthorizors(newPermission.getAuthorizors());
        this.setMessage(newPermission.getMessage());
        this.setHref(newPermission.getHref());

    }

}

