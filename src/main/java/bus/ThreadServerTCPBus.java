package bus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

/**
 * Class qui permet de creer un serveur pour communique avec client spécifique 
 * 
 * @author Quentin Laborde
 *
 */
public class ThreadServerTCPBus extends Thread{

    
    /////////////////////////// info Bus 
    private Bus bus;
    
    ////////////////////////// Socket/Entre/Sortie //////////////////
    private Socket socketConnection;
    private PrintWriter out;
    private BufferedReader in;
    
    ////////////////////////// Requete/Reponse  /////////////////////:
    
    String requete;
    String reponse;
    
    JSONObject requeteJSON = new JSONObject();
    JSONObject reponseJSON = new JSONObject();
    
    ///////////////////////// Constructeur //////////////////////////

    public ThreadServerTCPBus(Bus bus,Socket socket){
        socketConnection = socket;
        this.bus = bus;
        

    }
    
    /////////////////////////// Methodes //////////////////////////////
    
    public void creationEntreSortie(){
        try{
            out = new PrintWriter(socketConnection.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
        }
        catch(IOException e){
            System.out.println("probleme creation entre sortie");
            System.exit(0);
        }

    }
    
    
    public void deconnection(){
        try{
            out.close();
            in.close();

        }
        catch(IOException e){
            System.out.println("Probleme deconnection");
            System.exit(0);
        }

    }
    

    public void run(){
        
        creationEntreSortie();

        boolean connecté = true;
        
        while(connecté){
            recupererRequete();
            switch((String)requeteJSON.get("Requete")){
            case "BUS":
                informationBus();
                envoyerReponse("BUS", reponseJSON);
                break;
            case "DECONNECTION":
                deconnection();
                connecté = false;
                break;
            default:
                System.out.println("Requete inconnue");
                envoyerReponse("INCONNUE",new JSONObject());
                break;
           
            }
        }
    }
    
    private void envoyerReponse(String typeReponse,JSONObject jsonReponse){
        try{
            reponseJSON = new JSONObject();
            reponseJSON.put(typeReponse, jsonReponse);
            reponse = reponseJSON.toString();
            System.out.println("Bus "+ bus.getNumBus()+" : Envoie reponse → "+ reponse);
            out.println(reponse);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie reponse →"+ e);
        }
        
    }
    
    private void recupererRequete(){
        try{
            requete = in.readLine();
            System.out.println("\n Bus " + bus.getNumBus() + ": Requete client :" + requete);
            requeteJSON = new JSONObject(requete);

        }
        catch(Exception e){
            System.out.println("Probleme recupération requete → "+ e);
        }
        
    }
    
    ////////////////////////////////// Services //////////////////////////////////::
    
    /**
     * “{\”Bus\”:\”nomBus\”,\”Vitesse\”:\”25\”,\”Noeud\”:\”3\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”}”
     */
    private void informationBus(){
        
        reponseJSON = new JSONObject();
        
        reponseJSON.put("Bus", bus.getNumBus());
        reponseJSON.put("IP", bus.getIP());
        reponseJSON.put("Port", bus.getPort());
        reponseJSON.put("Vitesse", bus.getVitesse());
        reponseJSON.put("Noeud", bus.getLigne().getPositionObjetConnecte().getNum());
        reponseJSON.put("Latitude", bus.getLigne().getPositionObjetConnecte().getPosition().getLatitude());
        reponseJSON.put("Longitude", bus.getLigne().getPositionObjetConnecte().getPosition().getLongitude());
        reponseJSON.put("DistanceBusArret", bus.getLigne().distanceEntreBusEtArret(requeteJSON.getString("NomArret1"),bus.getSansDirecte()));
        reponseJSON.put("DistanceArretAArret", bus.getLigne().distanceEntreDeuxArrets(requeteJSON.getString("NomArret1"),requeteJSON.getString("NomArret2"), bus.getSansDirecte()));
        reponseJSON.put("DistanceBusArretArrive", bus.getLigne().distanceEntreBusEtArret(requeteJSON.getString("NomArret2"),bus.getSansDirecte()));
        reponseJSON.put("PlacesRestantes", bus.getNombrePlaceRestante());
        
        
    }
    

    
}

