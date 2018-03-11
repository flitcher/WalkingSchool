package walkingschoolbus.cmpt276.ca.dataObjects;

import java.util.Arrays;
import java.util.List;

/**
 * Created by seungdobaek on 2018-03-08.
 */

public class WalkingGroups {
    private Long id;
    private String groupDescription;
    private double[] routeLatArray;
    private double[] routeLngArray;
    private User leader;
    private List<User> membersUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public double[] getRouteLatArray() {
        return routeLatArray;
    }

    public void setRouteLatArray(double[] routeLatArray) {
        this.routeLatArray = routeLatArray;
    }

    public double[] getRouteLngArray() {
        return routeLngArray;
    }

    public void setRouteLngArray(double[] routeLngArray) {
        this.routeLngArray = routeLngArray;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMembersUsers() {
        return membersUsers;
    }

    public void setMembersUsers(List<User> membersUsers) {
        this.membersUsers = membersUsers;
    }

    @Override
    public String toString() {
        return "WalkingGroups{" +
                "id=" + id +
                ", groupDescription='" + groupDescription + '\'' +
                ", routeLatArray=" + Arrays.toString(routeLatArray) +
                ", routeLngArray=" + Arrays.toString(routeLngArray) +
                ", leader=" + leader +
                ", membersUsers=" + membersUsers +
                '}';
    }
}
