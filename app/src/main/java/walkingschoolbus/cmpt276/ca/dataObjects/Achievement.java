package walkingschoolbus.cmpt276.ca.dataObjects;

/**
 * Created by Kawai on 4/7/2018.
 */

public class Achievement {

    String milestoneInString;
    Integer achievementSticker;
    String achievementState;

    public Achievement(String milestoneInString, Integer achievementSticker, String achievementState) {
        this.milestoneInString = milestoneInString;
        this.achievementSticker = achievementSticker;
        this.achievementState = achievementState;
    }

    public String getMilestoneInString() {
        return milestoneInString;
    }

    public void setMilestoneInString(String milestoneInString) {
        this.milestoneInString = milestoneInString;
    }

    public Integer getAchievementSticker() {
        return achievementSticker;
    }

    public void setAchievementSticker(Integer achievementSticker) {
        this.achievementSticker = achievementSticker;
    }

    public String getAchievementState() {
        return achievementState;
    }

    public void setAchievementState(String achievementState) {
        this.achievementState = achievementState;
    }
}
