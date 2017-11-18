package route;

/**
 * Class qui permet de creer une position, cad un couple : latitude et longetude.
 * 
 * @author Quentin Laborde
 *
 */
public class Position {
    
    private double longitude = 0;
    private double latitude = 0;
    
    
    public Position(double lon, double lat){
        this.latitude = lat;
        this.longitude = lon;
    }


    public double getLongitude() {
        return longitude;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    
    
    public String toString() {
        return "longitude = "+ longitude+ " et latitude = "+latitude;
    }
    

    
    

}
