package donneesNoeuds;

public class Noeuds {

	private int nbr;
	private String lat;
	private String lon; 
	private String ele, speed, time;
	private int vitC; 
	private int vitD;
  
	/**
	 * Setter pour le numéro du noeud
	 * @param nbr
	 */
	public Noeuds(int nbr){
		this.nbr=nbr;
	} 
  
	/**
	 * Méthode que retourne la latitude
	 * @return latitude
	 */
	public String getLat(){
		return lat;
	} 
	
	/**
	 * Méthode que retourne la longitude
	 * @return longitude
	 */
	public String getLon(){
		return lon;
	}
	
	/**
	 * Méthode que retourne l'élévation
	 * @return elevation
	 */
	public String getEle(){
		return ele;
	} 
	
	/**
	 * Méthode que retourne la vitesse
	 * @return vitesse
	 */
	public String getSpeed(){
		return speed;
	} 
	
	/**
	 * Méthode que retourne le temps
	 * @return
	 */
	public String getTime(){
		return time;
	} 
  
	/**
	 * Setter pour modifier la latitude
	 * @param lat
	 */
	public void setLat(String lat){
		this.lat = lat;
	} 
	
	/**
	 * Setter pour modifier la longitude
	 * @param lon
	 */
	public void setLon(String lon){
		this.lon = lon;
	}
	
	/**
	 * Setter pour modifier l'élévation
	 * @param ele
	 */
	public void setEle(String ele){
		this.ele = ele;
	} 
	
	/**
	 * Setter pour modifier la vitesse
	 * @param speed
	 */
	public void setSpeed(String speed){
		this.speed = speed;
	} 
	
	/**
	 * Setter pour modifier le temps
	 * @param time
	 */
	public void setTime(String time){
		this.time = time;
	} 
  
	/**
	 * ToString...
	 */
	public String toString(){ 
		return "{\"Noeud\":\""+nbr+"\",\"Latitude\":\""+lat+"\",\"Longitude\":\""+lon+"\",\"VitesseCroissante\":\""+vitC+"\",\"VitesseDecroissante\":\""+vitD+"\"}"; 
	}

	/**
	 * Setter pour modifier la vitesse croissante
	 * @param vitC
	 */
	public void setVitesseC(int vitC) {
		this.vitC= vitC;		
	} 
	
	/**
	 * Setter pour modifier la vitesse décroissante
	 * @param vitD
	 */
	public void setVitesseD(int vitD) {
		this.vitD= vitD;		
	}
	
	/**
	 * Méthode que retourne la vitesse croissante
	 * @return vitesse croissante
	 */
	public int getVitC(){
		return vitC;
	}
	
	/**
	 * Méthode que retourne la vitesse décroissante
	 * @return vitesse décroissante
	 */
	public int getVitD(){
		return vitD;
	}
}