package walkingschoolbus.cmpt276.ca.dataObjects;

/**
 * Created by Kawai on 4/7/2018.
 */

public class Gamification {
    float totalDistanceTravelled;

    public float getTotalDistanceTravelled() {
        return totalDistanceTravelled;
    }

    public void setTotalDistanceTravelled(float distanceTravelled) {
        this.totalDistanceTravelled = this.totalDistanceTravelled + distanceTravelled;
    }
}
