package walkingschoolbus.cmpt276.ca.dataObjects;

import java.util.Date;

/**
 * Created by Jonathan Chen on 2018/3/24.
 */

public class Location {
    private double lat;
    private double lng;
    private String timestamp;


    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

   public void setLoction(Location location){
        this.setLat(location.getLat());
        this.setLng(location.getLng());
        this.setTimestamp(location.getTimestamp());
   }


}
