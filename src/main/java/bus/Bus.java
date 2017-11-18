package bus;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.Random;
import bus.etat.*;
import exception.CommandeBusException;
import route.Ligne;

/**
 * Class qui permet de simuler le comportement d'un bus sur une ligne de bus 
 * avec ces différente communication avec les arrets, le server d'information et l'application android.
 * 
 * @author Quentin Laborde
 *
 */
public class Bus extends Thread{

    private String IP = "10.212.115.127";
    private int port = 1234;

    private String IPServerInfo = "10.212.115.218";
    //private String IPServerInfo = "10.212.115.127";
    //private String IPServerInfo = "10.188.232.162";

    //private String IPServerInfo = "localhost";
    private int portServerInfo = 4444;

    ////////////////////////// Static ///////////////////////////////

    static private int nombrePlace = 100;

    ///////////////////////// Données Bus //////////////////////////

    private int nombrePassagerBus = 0;
    private int nombrePlaceRestante = nombrePlace;
    private int vitesse = 25;
    private boolean senseDirect = true;

    private String numBus;

    /////////////////////// Ligne ///////////////////////////////////

    private Ligne ligne;

    /////////////////////// Etat //////////////////////////////////

    private EtatBus etat = new EtatEnRoute();

    /////////////////////////// Client TCP //////////////////

    ClientTCPBus client;

    /////////////////////////// Constructeurs ////////////////////////

    public Bus(String numBus){
        this.numBus = numBus;
        this.ligne = new Ligne();         

        //Client pour que le Bus puis ce connecter au server central pour récuperer les infos sur la route.
        client = new ClientTCPBus(this,IPServerInfo,portServerInfo);
        client.connection();
        client.initBus(numBus);
        client.coordonnerGPSNoeud();
        client.coordonnerGPSArret();
        //client.coordonnerGPSZonneInteret();
        client.lisaisonEtOrigine();

        new ServerMultTCPBus(this,port).start();     
        
 
    } 

    /////////////////////////// Methodes ///////////////////////////


    /**
     * Permet de lancer le Bus sur la ligne  de bus
     */
    public void run(){
        Random rad = new Random();

        //Boucle de déplacement du bus
        while(true){
            //vitesse = client.trafficNoeud(ligne.getPositionObjetConnecte().getNom(),senseDirect);

            //Distance en KM
            double distance = getLigne().distance(senseDirect)/1000;
            
            double tempsHeur = distance/vitesse;
            double tempsSeconde = tempsHeur*3600;
   
            //Plus la vitesse du bus est grande, moins le temps entre chaque "saut" l'est.
            attente(tempsSeconde*1000);

            //Si le bus est devant un arret, il débarque et embarque des passagers.
            if(estDevantUnArret()){
                arreter();
                descendreBus(rad.nextInt(10));
                monterBus(rad.nextInt(10));
                System.out.println("Bus num : " + numBus + " → nombre de passager = "+nombrePassagerBus);
                avancer();
            }

            if(senseDirect){
                ligne.prochainNoeud();

            }
            else{
                ligne.precedentNoeud();
            }

            System.out.println("Bus num : " 
                    + numBus + " → prochain noeud : " 
                    + ligne.getPositionObjetConnecte().getNum() + " → "
                    + ligne.getPositionObjetConnecte().getPosition()); // On affiche les coordonées GPS du bus.

        }
    }

    public void avancer(){
        try{
            etat = etat.avancer(estPlein());  
            System.out.println("Bus num : " + numBus + " → Avancer\n");

        }
        catch(CommandeBusException e){
            System.out.println("Probleme : action impossible sans cette etat → " + e);
        }
    }

    public void arreter(){
        try{
            etat = etat.arreter();
            System.out.println("\nBus num : " + numBus + " → Arret");

        }
        catch(CommandeBusException e){
            System.out.println("Probleme : action impossible sans cette etat → " + e);
        }
    }

    public void monterBus(int nbPersonneQuiMonte){

        try{
            etat = etat.remplireBus();

            if(nbPersonneQuiMonte > nombrePlaceRestante){
                nbPersonneQuiMonte = nombrePlaceRestante;
                nombrePassagerBus = nombrePlace;
                nombrePlaceRestante = 0;

            }
            else{
                nombrePassagerBus += nbPersonneQuiMonte;
                nombrePlaceRestante -= nbPersonneQuiMonte;
            }

            System.out.println(nbPersonneQuiMonte + " monte dans le bus");  

        }
        catch(CommandeBusException e){
            System.out.println("Probleme : action impossible sans cette etat → " + e);

        }

    }

    public void descendreBus(int nbPersonneQuiDescende){
        try{
            etat = etat.viderBus();

            if(nbPersonneQuiDescende > nombrePassagerBus){
                nbPersonneQuiDescende = nombrePassagerBus;
                nombrePassagerBus = 0;
                nombrePlaceRestante = nombrePlace;

            }
            else{
                nombrePassagerBus -= nbPersonneQuiDescende;
                nombrePlaceRestante += nbPersonneQuiDescende;
            }

            System.out.println(nbPersonneQuiDescende + " descende du bus");  

        }
        catch(CommandeBusException e){
            System.out.println("Probleme : action impossible sans cette etat → " + e);

        }
    }


    private boolean estPlein(){
        int placeRestante = nombrePlace - nombrePassagerBus;
        return (placeRestante <= 0);
    }

    private boolean estDevantUnArret(){
        return ligne.getListeArret().containsValue(ligne.getPositionObjetConnecte());
    }

    private void attente(double time){
        try{
            Thread.sleep((int)time);
        }
        catch(InterruptedException e){
            System.out.println("Probleme attente temps");
        }
    }




    //////////////////////////////////: Getters et Setters ///////////////////////////////


    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getNombrePlaceRestante() {
        return nombrePlaceRestante;
    }

    public void setNombrePlaceRestante(int nombrePlaceRestante) {
        this.nombrePlaceRestante = nombrePlaceRestante;
    }

    public int getVitesse() {
        return vitesse;
    }

    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }

    public boolean getSansDirecte() {
        return senseDirect;
    }

    public void setSansDirecte(boolean sansDirecte) {
        this.senseDirect = sansDirecte;
    }

    public String getNumBus() {
        return numBus;
    }

    public void setNumBus(String numBus) {
        this.numBus = numBus;
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }


}
