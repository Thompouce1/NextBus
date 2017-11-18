package arret;

import java.net.Socket;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.json.JSONObject;

/**
 * Class qui permet de creer un Thread pour communiquer avec un client spécifique en TCP.
 * 
 * @author Quentin Laborde
 *
 */
public class ThreadServerTCPArret extends Thread{
    
    private double perimetreTerre = 40007864;
    
    boolean connecté = true;


    /////////////////////////// info Bus 

    private Arret arret;
    
    private String nomArretArrive = "";
    
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

    public ThreadServerTCPArret(Arret arret,Socket socket){
        socketConnection = socket;
        this.arret = arret;
        
        
        requeteJSON = new JSONObject();
        requeteJSON.put("ArretArrive", "Luciole");

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
            System.out.println("Deconnection.................");
            connecté = false;
            socketConnection.close();
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

        while(connecté){
            
            if(!recupererRequete()){
                deconnection();
                break;
            }

            switch((String)requeteJSON.get("Requete")){
            case "BUS":
                choisirBus();
                envoyerReponse();
                break;
            case "LISTBUS": // Si on utilise cette commande, on ne peut plus utiliser les autre sans faire avant une deconnection
                ListerBus();
                envoyerReponse();
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
        
        System.out.println("\n\n\n\n Sortie du while \n\n\n\n");
    }
    
    private void envoyerReponse(){
        try{
            reponse = reponseJSON.toString();
            System.out.println("Arret "+ arret.getNumArret()+" server TCP : Envoie reponse → "+ reponse);
            out.println(reponse);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie reponse : "+ reponse);
        }
        
    }
    
    private void envoyerReponse(String typeReponse,JSONObject jsonReponse){
        try{
            reponseJSON = new JSONObject();
            reponseJSON.put(typeReponse, jsonReponse);
            reponse = reponseJSON.toString();
            System.out.println("\n Arret "+ arret.getNumArret()+" server TCP : Envoie reponse → "+ reponse);
            out.println(reponse);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie reponse : "+ reponse);
        }
    }
    
    private boolean recupererRequete(){
        try{
            requete = in.readLine();
            System.out.println("\n Arret " + arret.getNumArret() + " server TCP : Requete client :" + requete);
            requeteJSON = new JSONObject(requete);
            return true;
        }
        catch(Exception e){
            System.out.println("Probleme recupération requete");
            return false;
            
        }
        
    }
    
    ////////////////////////////////// Services //////////////////////////////////
    
    /**
     * On retourne le numero du bus qui est le plus proche de l'arret de depart, peut importe si il est dans le bon sense.
     * Retourne aussi la distance entre le bus et l'arret et la distance entre les deux arret.
     * 
     * @return tableau qui contient le numero du bus et la distance
     */
    private double[] BusAvecLeTrajetLePlusCourt(JSONObject listeBus){
        double res[] = new double[2];
        
        double distanceMin = perimetreTerre;
        double distanceBusArret = 0.0;
        double distanceArretAArret = 0.0;
        
        Iterator<String> iterator = listeBus.keys();
        
        for(;iterator.hasNext();){
            
            String cle = iterator.next();
 
            System.out.println("cle = "+cle);
            
            JSONObject jsonBus = listeBus.getJSONObject(cle).getJSONObject("BUS");
            int numBus = jsonBus.getInt("Bus");
            distanceBusArret = jsonBus.getDouble("DistanceBusArret");
            distanceArretAArret = jsonBus.getDouble("DistanceArretAArret");

            System.out.println("numBus = "+numBus+" et distanceBusArret = "+distanceBusArret + " et distanceArretAArret = "+ distanceArretAArret + " et distanceMin = "+distanceMin);

            if(distanceBusArret + distanceArretAArret < distanceMin){
                res[0] = numBus;
                res[1] = distanceBusArret + distanceArretAArret;
                distanceMin = distanceBusArret + distanceArretAArret;
            }
                        
        }
        
        return res;
    }
    

    
    /**
     * 
     */
    public void choisirBus(){
        nomArretArrive = (String)requeteJSON.get("ArretArrive");
        
        JSONObject listeBus = arret.getClient().informationBus(nomArretArrive);
        
        System.out.println("\nReponse choisir Bus " + listeBus);
                
        double resLePluscourtTrajet[] = BusAvecLeTrajetLePlusCourt(listeBus);
        
        System.out.println("zqeffzef = "+(int)resLePluscourtTrajet[0]);
        
        reponseJSON = (JSONObject)listeBus.get(((int)resLePluscourtTrajet[0]) + "");

        connecté = false;

    }
    
    /**
     * “{\”Bus\”:\”nomBus\”,\”IP\”:\”123.222.222.222\”,\”Port\”:\”1234\”,\”vitesse\”:\”25\”,\”Noeud\”:\”3\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”}”
     */
    public void ListerBus(){
        nomArretArrive = (String)requeteJSON.get("ArretArrive");
        System.out.println("arret final = "+nomArretArrive);
        System.out.println("salut ..............");
        JSONObject listeBus = arret.getClient().informationBus(nomArretArrive);
        System.out.println("\nReponse lister Bus " + listeBus);
        reponseJSON = listeBus;

    }


    
}