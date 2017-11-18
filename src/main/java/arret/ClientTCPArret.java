package arret;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import route.Ligne;
import route.Noeud;
import route.NoeudArret;
import java.util.Set;
import org.json.JSONException;


/**
 * Class qui permet de creer un client pour un arret de bus. 
 * Ce client permet de transmetre des requete en json avec le protocole TCP vers un server définie dans le constructeur.
 * 
 * @author Quentin Laborde
 *
 */
public class ClientTCPArret{

    
    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    
    String requete;
    String reponse;
    
    JSONObject requeteJSON = new JSONObject();
    JSONObject reponseJSON = new JSONObject();
    
    private String IPServer;
    private int portServer;
    
    private Arret arret;
    private Ligne ligne;
    
    public ClientTCPArret(Arret arret, String IPServer, int portServer){
        this.arret = arret;
        this.ligne = arret.getLigne();
        this.IPServer = IPServer;
        this.portServer = portServer;
        
    }

    void connection(){   
        try{
            socket = new Socket(IPServer, portServer);
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
   

    private void envoyerRequete(String typeRequete){
        try{
            requeteJSON = new JSONObject();
            requeteJSON.put("Requete", typeRequete);
            requete = requeteJSON.toString();
            System.out.println("\n Arret " + arret.getNumArret() + " client TCP : requete vers le server :" + requete);
            out.println(requete);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie requete →"+ e);
        }
        
    }
    
    private void envoyerRequete(String typeRequete, String[]...parametre){
        try{
            requeteJSON = new JSONObject();
            requeteJSON.put("Requete", typeRequete);
            
            for(int i = 0; i < parametre.length ; i++){
                requeteJSON.put(parametre[i][0], parametre[i][1]);
            }
            
            requete = requeteJSON.toString();
            System.out.println("\nRequete vers le server → " + requete);
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
            System.out.println("\n Arret " + arret.getNumArret() + " client TCP : reponse server :" + reponse);
            reponseJSON = new JSONObject(reponse);
        }
        catch(Exception e){
            System.out.println("Probleme recupération reponse");
        }
        
    }
    
    
    ///////////////////////////////// Service pour les Bus /////////////////////////////
    
    public JSONObject informationBus(String arretArrive){
        System.out.println(arret.getNomArret());
        String nomNoeud1[] = {"NomArret1",arret.getNomArret()};
        System.out.println(nomNoeud1[1]);
        String nomNoeud2[] = {"NomArret2",arretArrive};

        envoyerRequete("BUS",nomNoeud1,nomNoeud2);
        recupererReponse();
        return reponseJSON;
        
    }
    
    
    ///////////////////////////////// Service pour le server d'information  /////////////////////////////

    public JSONObject informationStaticBus(){
        envoyerRequete("LISTBUS");
        recupererReponse();
        return reponseJSON;
    }
    

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
        
        NoeudArret noeudPositionArret = null;
        
        envoyerRequete("LISTARRETS");
        recupererReponse();
        
        
        try{
            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numNoeud = (String) iterator.next();
                JSONObject noeud = (JSONObject) reponseJSON.get(numNoeud);
                NoeudArret noeudArret = new NoeudArret(noeud.getString("Arret"),noeud.getString("Nom"), Double.parseDouble(noeud.getString("Longitude")),Double.parseDouble(noeud.getString("Latitude")));
                ligne.addArret(noeudArret);
                
                if(noeudArret.equals(arret.getNumArret())){
                    noeudPositionArret = noeudArret;
                }
           }
            
            lisaisonEtOrigine(noeudPositionArret);

        }
        catch(JSONException e){
            System.out.println("probleme Json non conforme → "+e);
        }


    }
    

    private void lisaisonEtOrigine(NoeudArret noeudArret){
        HashMap<String,Noeud> listeNoeud = ligne.getListeNoeud();
        
        Set<String> set = listeNoeud.keySet();
                
        for(Iterator<String> it = set.iterator(); it.hasNext();){
  
            String numNoeud = (String) it.next();
            
            String precedent = (Integer.parseInt(numNoeud) - 1)+"";
            String suivant = (Integer.parseInt(numNoeud) + 1)+"";
            
            if(precedent.equals("0")){
                System.out.println("premier noeud");
                ligne.getNoeud(numNoeud).setLiaison1(ligne.getNoeud("268"));
                ligne.getNoeud(numNoeud).setLiaison2(ligne.getNoeud(suivant));
            }
            if(suivant.equals(listeNoeud.size() + 1)){
                System.out.println("dernier noeud");
                ligne.getNoeud(numNoeud).setLiaison1(ligne.getNoeud(precedent));
                ligne.getNoeud(numNoeud).setLiaison2(ligne.getNoeud("1"));
            }
            else{
                ligne.getNoeud(numNoeud).setLiaison1(ligne.getNoeud(precedent));
                ligne.getNoeud(numNoeud).setLiaison2(ligne.getNoeud(suivant));
            }

        }
        

        ligne.setPositionObjetConnecte(noeudArret);
    }
    
    /**
     * methode qui permet de récuperer le le nom/num et le port de l'arret
     * @param numArret
     */
    public void initArret(String numArret){
                
        envoyerRequete("LISTARRETS");
        recupererReponse();
        
        arret.setNumArret(((JSONObject)reponseJSON.get(numArret)).getString("Arret"));
        arret.setPort(((JSONObject)reponseJSON.get(numArret)).getInt("Port"));
        arret.setNomArret(((JSONObject)reponseJSON.get(numArret)).getString("Nom"));
        
        System.out.println(" eeddzef "+arret.getNomArret());

    }
    


}
