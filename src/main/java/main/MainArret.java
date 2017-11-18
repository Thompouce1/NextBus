package main;

import arret.Arret;

/**
 * Class qui permet de creer les arret et de lancer leur simulations respectives.
 * 
 * @author Quentin Laborde
 *
 */
public class MainArret {
   
    public static void main(String[] args) {
        
        ClientTCPInitialisation client = new ClientTCPInitialisation("10.212.115.218", 4444);
        //ClientTCPInitialisation client = new ClientTCPInitialisation("10.212.115.127", 4444);
        //ClientTCPInitialisation client = new ClientTCPInitialisation("10.188.232.162", 4444);

        client.connection();
        
        //client.ajouterArret("Paris", "48.853" , "2.35");
        client.ajouterArret("BIBI", "43.616125" , "7.06672");
        
        int nbArret = client.nombreArret();
        
        for(int i = 1 ; i <= nbArret; i++){
            new Arret(i+"").start();       
        }

        client.deconnection();
        
  


    }

}


