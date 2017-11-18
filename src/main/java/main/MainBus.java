package main;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import bus.Bus;
import cucumber.api.java.en_au.Tooright;

/**
 * Class qui permet de creer les bus et de lancer leur simulations respectives.
 * 
 * @author Quentin Laborde
 *
 */
public class MainBus {
    
    

	public static void main(String[] args) throws InterruptedException {
	    
        double R = 6371000;

        System.out.println("distance = "+ R*acos(sin(Math.toRadians(43.617467))*sin(Math.toRadians(43.617016))+cos(Math.toRadians(43.617467))*cos(Math.toRadians(43.617016))*cos(Math.toRadians(7.070682-7.074180))));

	    
	    ClientTCPInitialisation client = new ClientTCPInitialisation("10.212.115.218", 4444);
	    //ClientTCPInitialisation client = new ClientTCPInitialisation("10.212.115.127", 4444);
        //ClientTCPInitialisation client = new ClientTCPInitialisation("10.188.232.162", 4444);

	    client.connection();
	    

	    int nbBus = client.nombreBus();
	    
        
        //client.ajouterBus("25", "Croissant");
        //client.supprimerBus("2");
        
        //client.ajouterArret("Paris", "48.853" , "2.35");
        client.ajouterArret("BIBI", "43.616125" , "7.06672");
;
	    
	    nbBus = client.nombreBus();

	    System.out.println("Nombre bus : " + nbBus);

	    Thread.sleep(1000);

	    
	    for(int i = 1 ; i <= nbBus; i++){
	        new Bus(i+"").start();
	    }

	    client.deconnection();

	}

}
