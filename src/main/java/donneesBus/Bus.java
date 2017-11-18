package donneesBus;

public class Bus {

	private int numero;
	//private static final String IPQuentin="10.212.115.127";
	private static final String IPQuentin="172.20.10.7";
	private int port; 
	private int vitesse;
	private String direction;
  
	/**
	 * Setter pour le numéro du noeud
	 * @param nbr
	 */
	public Bus(){} 
  
	
  
	public int getNumero() {
		return numero;
	}



	public void setNumero(int numero) {
		this.numero = numero;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public int getVitesse() {
		return vitesse;
	}



	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}



	public String getDirection() {
		return direction;
	}



	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * ToString...
	 */
	public String toString(){ 
		return "{\"Bus\": \""+numero+"\",\"IP\":\""+IPQuentin+"\",\"Port\":\""+port+"\",\"Vitesse\":\""+vitesse+"\",\"Direction\":\""+direction+"\"}"; 
	}

}