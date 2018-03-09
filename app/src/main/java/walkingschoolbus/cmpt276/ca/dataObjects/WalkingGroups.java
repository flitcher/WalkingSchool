package walkingschoolbus.cmpt276.ca.dataObjects;

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
}
