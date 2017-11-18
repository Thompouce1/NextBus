package bus;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import route.Ligne;
import route.Noeud;
import route.NoeudArret;
import route.NoeudZoneInteret;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * 
 * Deux phases : coonection au serveur pour pourvoire creer une ligne
 * @author user
 *
 */
public class ClientTCPBus{
    
    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    
    String requete;
    String reponse;
    
    JSONObject requeteJSON = new JSONObject();
    JSONObject reponseJSON = new JSONObject();
    
    private Ligne ligne;
    private Bus bus;
    private String IPServerInformation;
    private int portServerInformation;
    
    public ClientTCPBus(Bus bus, String IPServerInformation, int portServerInformation){
        this.bus = bus;
        this.ligne = bus.getLigne();
        this.IPServerInformation = IPServerInformation;
        this.portServerInformation = portServerInformation;
         
    }

    void connection(){   
        try{
            socket = new Socket(IPServerInformation, portServerInformation);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        }
        catch(Exception e){
               System.out.println("Probleme connection : " + e);
        }
    }
    
    void deconnection(){
        try{
            envoyerRequete("DECONNECTION");
            out.close();
            in.close();
            socket.close();
        }
        
        catch(Exception e){
            System.out.println("Probleme quit : " + e);
        }

    }
   
    
    /**
     * On demande les possitions GPS de tout les Noeuds avec comme cle la dessignation Noeud, Arret ou Zonne d'interet
     * 
     * On recupère : "{\”1\”:{... },\”2\”:{... },\”3\”:{\"Noeud\”:\"3\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”}}”
     * 
     * @return
     */
    public void coordonnerGPSNoeud(){
        
        envoyerRequete("LISTNOEUDS");
        recupererReponse();

        try{
           
            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numNoeud = (String) iterator.next();
                JSONObject noeud = (JSONObject) reponseJSON.get(numNoeud);        
                ligne.addNoeud(new Noeud(noeud.getString("Noeud"), Double.parseDouble(noeud.getString("Longitude")),Double.parseDouble(noeud.getString("Latitude"))));
            }
            
        }
        catch(JSONException e){
            System.out.println("probleme Json non conforme → "+e);
        }

    }
    
    /**
     * "{\”1\”:{\"Arrêt\”: \"3\”,\”Nom\”:\”Templier\”,\”IP\”:\” 123.345.434.12\”,\”Port\”:\” 1234\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”},\”2\”:{\"Arrêt\”: \"3\”,\”Nom\”:\”Templier\”,\”IP\”:\” 123.345.434.12\”,\”Port\”:\” 1234\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”},\”3\”:{\"Arrêt\”: \"3\”,\”Nom\”:\”Templier\”,\”IP\”:\” 123.345.434.12\”,\”Port\”:\” 1234\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”}}”

     * @return
     */
    public void coordonnerGPSArret(){

        envoyerRequete("LISTARRETS");
        recupererReponse();

        try{
            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numNoeud = (String) iterator.next();
                JSONObject noeud = (JSONObject) reponseJSON.get(numNoeud);
                NoeudArret arret = new NoeudArret(noeud.getString("Arret"), noeud.getString("Nom"), Double.parseDouble(noeud.getString("Longitude")),Double.parseDouble(noeud.getString("Latitude")));
                ligne.addArret(arret);
           }
            
            //lisaisonEtOrigine();

        }
        catch(JSONException e){
            System.out.println("probleme Json non conforme → "+e);
        }


    }
    
    /**
     * "{\"Requete\":\"TRAFFIC\",\"NumNoeud\":\"1\"}}"

     * @return
     */
    public int trafficNoeud(String numNoeud, boolean direction){
        
        //"{\"Vitesse\":\”25\"}"

        envoyerRequete("TRAFFIC",numNoeud);
        recupererReponse();
        if(direction){
            return reponseJSON.getInt("VitesseCroissante");
        }
        else{
            return reponseJSON.getInt("VitesseDirecte");
        }
    }
    

    public void lisaisonEtOrigine(){
        HashMap<String,Noeud> listeNoeud = ligne.getListeNoeud();
        
        Set<String> set = listeNoeud.keySet();
                        
        for(Iterator<String> it = set.iterator(); it.hasNext();){
  
            String numNoeud = (String) it.next();
            
            String precedent = (Integer.parseInt(numNoeud) - 1)+"";
            String suivant = (Integer.parseInt(numNoeud) + 1)+"";
                        
            if(precedent.equals("0")){
                ligne.getNoeud(numNoeud).setLiaison1(ligne.getNoeud("268"));
                ligne.getNoeud(numNoeud).setLiaison2(ligne.getNoeud(suivant));
            }
            if(suivant.equals((listeNoeud.size() + 1) + "")){
                ligne.getNoeud(numNoeud).setLiaison1(ligne.getNoeud(precedent));
                ligne.getNoeud(numNoeud).setLiaison2(ligne.getNoeud("1"));
            }
            else{
                ligne.getNoeud(numNoeud).setLiaison1(ligne.getNoeud(precedent));
                ligne.getNoeud(numNoeud).setLiaison2(ligne.getNoeud(suivant));
            }

        }

        //TODO temporaire ...
        ligne.setPositionObjetConnecte(ligne.getNoeud("1"));
    }

    
    /**
     * "{\"Nom\": \""+"Monument"+"\",\"numero\":\""+1+"\",\"Description\":\""+"Sur votre gauche vous pouvez voir un monument aux morts de la seconde guerre mondiale."+"\"}"
     */
    public void coordonnerGPSZonneInteret(){
        
        envoyerRequete("LISTZONE");
        recupererReponse();
        
        try{
            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numZone = (String) iterator.next();
                JSONObject zone = (JSONObject) reponseJSON.get(numZone);
                NoeudZoneInteret noeudZone = new NoeudZoneInteret(zone.getString("Arret"), Double.parseDouble(zone.getString("Longitude")),Double.parseDouble(zone.getString("Latitude")));
                ligne.addZoneInteret(noeudZone);
           }
 
        }
        catch(JSONException e){
            System.out.println("probleme Json non conforme → "+e);
        }


    }
    
    
    private void envoyerRequete(String typeRequete){
        try{
            requeteJSON = new JSONObject();
            requeteJSON.put("Requete", typeRequete);
            requete = requeteJSON.toString();
            System.out.println("\n Bus " + bus.getNumBus() + ": requete vers le server :" + requete);
            out.println(requete);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie requete : "+typeRequete);
        }
        
    }
    
    private void envoyerRequete(String typeRequete, String numNoeud){
        try{
            requeteJSON = new JSONObject();
            requeteJSON.put("Requete", typeRequete);
            requeteJSON.put("numNoeud", numNoeud);
            requete = requeteJSON.toString();
            System.out.println("\n Bus " + bus.getNumBus() + ": requete vers le server :" + requete);
            out.println(requete);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie requete : "+typeRequete);
        }
        
    }
    
    private void recupererReponse(){
        try{
            reponse = in.readLine();
            System.out.println("\n Bus " + bus.getNumBus() + ": reponse server :" + reponse);
            reponseJSON = new JSONObject(reponse);
        }
        catch(Exception e){
            System.out.println("Probleme recupération reponse");
        }
        
    }
    
    
    /**
     * methode qui permet de récuperer le le nom/num et le port du bus
     * @param numArret
     */
    public void initBus(String numBus){
                
        envoyerRequete("LISTBUS");
        recupererReponse();
        
        System.out.println("Initialisation Bus : port = "+((JSONObject)reponseJSON.get(numBus)).getInt("Port"));
        bus.setNumBus(((JSONObject)reponseJSON.get(numBus)).getString("Bus"));
        bus.setPort(((JSONObject)reponseJSON.get(numBus)).getInt("Port"));
        bus.setVitesse(((JSONObject)reponseJSON.get(numBus)).getInt("Vitesse"));

    }


}
