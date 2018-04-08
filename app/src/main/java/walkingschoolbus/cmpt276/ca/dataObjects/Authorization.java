package walkingschoolbus.cmpt276.ca.dataObjects;

import java.util.List;

/**
 * Created by Jonathan Chen on 2018/4/7.
 */

public class Authorization {
    private List<User> users;
    private PermissionStatus status;
    private User whoApprovedOrDenied;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public PermissionStatus getStatus() {
        return status;
    }

    public void setStatus(PermissionStatus status) {
        this.status = status;
    }

    public User getWhoApprovedOrDenied() {
        return whoApprovedOrDenied;
    }

    public void setWhoApprovedOrDenied(User whoApprovedOrDenied) {
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }
}
