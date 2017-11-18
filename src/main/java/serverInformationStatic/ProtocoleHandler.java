package serverInformationStatic;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

/**
 * Basic class to handle our protocole
 */
public class ProtocoleHandler
{
    private RequestHandler requests;

    public ProtocoleHandler(boolean listening)
    {
        requests = new RequestHandler(listening);
    }
    /**
     * Fonction permettant de gerer une requete du serveur
     * @param requete La requete a traiter
     * @return La reponse générée par cette requete
     * @throws IOException 
     * @throws JSONException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws JDOMException 
     */
    public String gererRequete(String requete) throws JSONException, IOException, ParserConfigurationException, SAXException, JDOMException
    {

        if (requete.contains("LISTNOEUDS")) {
            return requests.listNoeuds().toString();
        } else if (requete.contains("LISTBUS")) {
            return requests.listBus().toString();
        } else if (requete.contains("LISTARRETS")) {
            return requests.listArrets().toString();
        } else if(requete.contains("DECONNECTION")){
        	return requests.deconnection();
        } else if(requete.contains("TRAFFIC")){
        	return requests.traffic(new JSONObject(requete)).toString();
        } else if (requete.contains("ZONE")){
        	return requests.zonesDinteret().toString();
        } else if(requete.contains("NBBUS")){
        	return requests.nbrBus().toString();
        } else if(requete.contains("NBARRETS")){
        	return requests.nbrArrets().toString();
        } else if(requete.contains("ADDBUS")){
        	JSONObject requ = new JSONObject(requete);
        	requests.addBus(requ.get("Vitesse"),requ.get("Direction"));
        	return  "{\"Reponse\":\"Bus ajouté\"}";
        } else if(requete.contains("ADDARRET")){
        	JSONObject requ = new JSONObject(requete);
        	requests.addArret(requ.get("Nom"),requ.get("Lat"),requ.get("Lon"));
        	return  "{\"Reponse\":\"Arret ajouté\"}";
        } else if(requete.contains("DELBUS")){
        	JSONObject requ = new JSONObject(requete);
        	requests.delBus(requ.get("Numero"));
        	return "{\"Reponse\":\"Bus "+ requ.get("Numero") +" supprimé\"}";
        } else {        
            return "Requete inexistante";
        }

    }
}
