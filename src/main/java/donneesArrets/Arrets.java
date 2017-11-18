package donneesArrets;

public class Arrets {

	private int numero;
	//private static final String IPQuentin="10.212.115.127";
	private static final String IPQuentin="172.20.10.7";
	private String nom;
	private int port; 
	private double latitude;
	private double longitude;
  
	
	public Arrets(){} 
  

	/**
	 * ToString...
	 */
	public String toString(){ 
		return "{\"Arret\": \""+numero+"\",\"Nom\":\""+nom+"\",\"IP\":\""+IPQuentin+"\",\"Port\":\""+port+"\",\"Latitude\":\""+latitude+"\",\"Longitude\":\""+longitude+"\"}"; 
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public static String getIpquentin() {
		return IPQuentin;
	}

}