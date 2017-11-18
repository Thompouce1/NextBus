package arret;

import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

/**
 * Class qui permet la gestion de multiple client pour l'arret de bus : 
 * un client pour le serveur d'information et un client pour chaques bus de la ligne
 * 
 * @author Quentin Laborde
 *
 */
public class ClientMultTCPArret {
    
    ////////////////////////////// liste client de l'arret /////////////////////////::::
    
    HashMap<String,ClientTCPArret> listeClientVersBus = new HashMap<String,ClientTCPArret>();

    ///////////////////////////// info arret ///////////////////////:
    
    private Arret arret;

    //////////////////////////
    boolean enFonction = false;
    
    ///////////////////////////////// info server information ////////////////////////
    
    private ClientTCPArret clientVersServerInfo;
    
    ////////////////////////////////// Constructeur ///////////////////////////////////
    
    public ClientMultTCPArret(Arret arret,String IPServerInformation, int portServerInformation){
        this.arret = arret;

        clientVersServerInfo = new ClientTCPArret(arret,IPServerInformation, portServerInformation);

        initialisationDesClient();
        

        
    }
    
    /////////////////////////////////// Methode ///////////////////////////////////////

    /**
     * Permet d'ajouter des client dans la liste des client. 
     * 
     * @param nomBus
     * @param IPBus
     * @param portBus
     */
    private void ajouterClientVersBus(String nomBus,String IPBus, int portBus){
        listeClientVersBus.put(nomBus, new ClientTCPArret(arret,IPBus, portBus));
    }
    
    public JSONObject informationBus(String nomArretArrive){
        JSONObject listeInfoBus = new JSONObject();
        for(int i = 1; i <= listeClientVersBus.size(); i++){
            listeClientVersBus.get(i+"").connection();
            System.out.println("\n\n\nConnection vers le server du bus numero "+i+"\n\n\n");
            listeInfoBus.put(i+"",listeClientVersBus.get(i+"").informationBus(nomArretArrive));
            listeClientVersBus.get(i+"").deconnection();
        }
        return listeInfoBus;
    }
    
    /**
     * On demande au server d'information la liste des bus sur la ligne 
     * pour initialiser les client qui communique avec chaque bus.
     */
    public void initialisationDesClient(){

        clientVersServerInfo.connection();
        JSONObject listeBus = clientVersServerInfo.informationStaticBus();
        //clientVersServerInfo.deconnection();
        
        for(Iterator<String> iterator = listeBus.keys(); iterator.hasNext();) {
            String numBus = (String) iterator.next();
            JSONObject bus = (JSONObject)listeBus.get(numBus);
          
            ajouterClientVersBus(bus.getString("Bus"),bus.getString("IP"),bus.getInt("Port"));
       }  
    }

    public ClientTCPArret getClientVersServerInfo() {
        return clientVersServerInfo;
    }

    public void setClientVersServerInfo(ClientTCPArret clientVersServerInfo) {
        this.clientVersServerInfo = clientVersServerInfo;
    }


    
    
    


}