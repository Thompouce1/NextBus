package serverInformationStatic;

import donneesArrets.Arrets;
import donneesArrets.ArretsHandler;
import donneesArrets.EnrArret;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom2.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import donneesBus.Bus;
import donneesBus.BusHandler;
import donneesBus.EnrBus;
import donneesNoeuds.EnrNoeud;
import donneesNoeuds.Noeuds;
import donneesNoeuds.NoeudsHandler;

/**
 * Basic class to handle a request
 */
public class RequestHandler
{
    private JSONObject donneesNoeuds;
    private JSONObject donneesBus = new JSONObject();;
    private JSONObject donneesArrets;
    private JSONObject zones;
    private boolean listening;
	private JSONObject noeudTraffic;
	private JSONObject nbrBus;
	private JSONObject nbrArrets;
    
	/**
	 * Contructeur
	 * @param listening
	 */
    public RequestHandler(boolean listening){
        donneesNoeuds = new JSONObject();
        donneesBus = new JSONObject();
        donneesArrets = new JSONObject();
        this.listening=listening;
    }

    /**
     * Return la liste des noeuds
     * @return liste des noeuds
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     */
    public JSONObject listNoeuds() throws ParserConfigurationException, SAXException, IOException
    {
    	SAXParserFactory fabrique = SAXParserFactory.newInstance(); 
		SAXParser parseur = fabrique.newSAXParser(); 
		  
		File fichier = new File("Donnees/Track1.xml"); 
		DefaultHandler gestionnaire = new NoeudsHandler(); 
		parseur.parse(fichier, gestionnaire);
    	int i = 1;
    	for(Noeuds p : ((NoeudsHandler) gestionnaire).getCarte()){ 
    		donneesNoeuds.put(""+i,new JSONObject(p.toString()));
    		i++;
		} 
        return donneesNoeuds;
    }
    
    /**
     * Return la liste des bus
     * @return liste des bus
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     */
    public JSONObject listBus() throws ParserConfigurationException, SAXException, IOException{
    	
    	SAXParserFactory fabrique1 = SAXParserFactory.newInstance(); 
		SAXParser parseur1 = fabrique1.newSAXParser(); 
    	
    	File fichier1 = new File("Donnees/ListeBus.xml"); 
		DefaultHandler gestionnaire1 = new BusHandler(); 
		parseur1.parse(fichier1, gestionnaire1);
		
    	int i = 1;   	
    	for(Bus pe : ((BusHandler) gestionnaire1).getListBus()){ 
    		donneesBus.put(""+i,new JSONObject(pe.toString()));
    		i++;
		} 
        return donneesBus;
    }
    
    /**
     * Return la liste des arrêts
     * @return liste des arrets
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     */
    public JSONObject listArrets() throws ParserConfigurationException, SAXException, IOException
    {
    	SAXParserFactory fabrique2 = SAXParserFactory.newInstance(); 
		SAXParser parseur2 = fabrique2.newSAXParser();
		  
		File fichier2 = new File("Donnees/ListeArrets.xml"); 
		DefaultHandler gestionnaire2 = new ArretsHandler(); 
		parseur2.parse(fichier2, gestionnaire2);
		
    	int i = 1;
    	for(Arrets pe : ((ArretsHandler) gestionnaire2).getListArrets()){ 
    		donneesArrets.put(""+i,new JSONObject(pe.toString()));
    		i++;
		} 
        return donneesArrets;
    }

    /**
     * Déconnection
     * @return
     */
	public String deconnection() {
		listening = false;
		return "client deconnecte";
	}

	/**
	 * Return la liste du traffic
	 * @param requete
	 * @return blabla
	 */
	public JSONObject traffic(JSONObject requete) {
		String cle = requete.getString("numNoeud");
		System.out.println(donneesNoeuds);
		noeudTraffic = (JSONObject) donneesNoeuds.get(cle);
		System.out.println(noeudTraffic);
		return noeudTraffic;
	}
	
	/**
	 * Return la liste des zones d'intérets
	 * @return zones d'interets
	 */
	public JSONObject zonesDinteret(){
	 	String zone1 = "{\"Nom\": \""+"Monument"+"\",\"numero\":\""+1+"\",\"Description\":\""+"Sur votre gauche vous pouvez voir un monument aux morts de la seconde guerre mondiale."+"\"}";
	 	String zone2 = "{\"Nom\": \""+"Cathédrale"+"\",\"numero\":\""+2+"\",\"Description\":\""+"La cathédrale date du 15 ième sièle. Monument emblématique de la ville."+"\"}";
	 	String zone3 = "{\"Nom\": \""+"Stade"+"\",\"numero\":\""+3+"\",\"Description\":\""+"Sur la droite voici le stade d'Antibes."+"\"}";
	 	String zone4 = "{\"Nom\": \""+"Golf"+"\",\"numero\":\""+4+"\",\"Description\":\""+"Ici vous pouvez voir le golf où viennent jouer beaucoup d'enseignants après les heures de cours."+"\"}";
		zones.put(""+1, new JSONObject(zone1));
		zones.put(""+2, new JSONObject(zone2));
		zones.put(""+3, new JSONObject(zone3));
		zones.put(""+4, new JSONObject(zone4));
	 	return zones;
	}

	public JSONObject nbrBus() throws ParserConfigurationException, SAXException, IOException{
		int i = 0;
		for (Iterator iterator = listBus().keys(); iterator.hasNext();) {
			i++;
			Object o = iterator.next();
		}
		String nbr = "{\"NOMBREBUS\": \""+i+"\"}";
		nbrBus = new JSONObject(nbr);
		return nbrBus;
	}

	public JSONObject nbrArrets() throws ParserConfigurationException, SAXException, IOException{
		int i = 0;
		for (Iterator iterator = listArrets().keys(); iterator.hasNext();) {
			i++;
			if(iterator.hasNext()){
				Object o = iterator.next();
			}
		}
		String nbr = "{\"NOMBREARRETS\": \""+i+"\"}";
		nbrArrets = new JSONObject(nbr);
		return nbrArrets;
	}

	public void addBus(Object object, Object object2) throws IOException, NumberFormatException, JSONException, ParserConfigurationException, SAXException {
		int nbr = Integer.parseInt(""+nbrBus().getString("NOMBREBUS")) + 1;
		int port = 1234 + nbr -1;
		EnrBus b = new EnrBus();
		b.lire("Donnees/ListeBus.xml");
		b.ecrire(nbr,port,object,object2);
		b.enregistrer();
	}

	public String addArret(Object nom, Object lat, Object lon) throws IOException, NumberFormatException, JSONException, ParserConfigurationException, SAXException {
		
	    JSONObject listeArret = listArrets();
	    JSONObject listeNoeud = listNoeuds();
	    
	    EnrArret enrArret = new EnrArret();
	    enrArret.lire("Donnees/ListeArrets.xml");

	    Iterator<String> iterator = listeArret.keys();
	    
		for (; iterator.hasNext();) {
		    
			if(iterator.hasNext()){
				String o = iterator.next();		
				JSONObject arret = listeArret.getJSONObject(o);
				
				if(arret.getString("Nom").equals((String)nom)){
					return "Nom déjà pris";
				}else if((arret.getString("Latitude").equals((String)lat))&&(arret.getString("Latitude").equals((String)lon))){
				    return "Coordonnées GPS déjà attribuées pour cet arrêt";
				}
				
			}
		}
		
		int i = 0;
		int possitionNouveauArret = 0;
		
		iterator = listeNoeud.keys();
		
		for (;iterator.hasNext();) {
			String o1 = iterator.next();
			JSONObject noeud = listeNoeud.getJSONObject(o1);
			
			if(iterator.hasNext()){
				i++;
				if((noeud.getString("Latitude").equals((String)lat))&&(noeud.getString("Longitude").equals((String)lon))){
					possitionNouveauArret = i;
				}
			}
		}
		
		if(possitionNouveauArret == 0){
			double distance = 1000000000;
			
			iterator = listeNoeud.keys();
			
			for (; iterator.hasNext();) {
				String o1 = iterator.next();
				if(iterator.hasNext()){
				    JSONObject noeud = listeNoeud.getJSONObject(o1);		        
			        double res = distance(noeud, (String)lat, (String)lon);
			        
			        if(res<=distance){
			        	distance = res;
			        	possitionNouveauArret = Integer.parseInt(o1);
			        }
				}
			}
			
			
			int p = 0;
			if(possitionNouveauArret%2==1){
				p = possitionNouveauArret*2 -1;
			} else {
				p = possitionNouveauArret*2;
			}			
			addNoeud(lat, lon, p);
			enrArret.incrementerArretPosSupp(possitionNouveauArret);
			enrArret.enregistrer();
			
			
		}
		
		int port = 1240 + Integer.parseInt(""+nbrArrets().getString("NOMBREARRETS"));
		enrArret.ecrire(possitionNouveauArret,nom,port,lat,lon);
		enrArret.enregistrer();
		return "Arret ajouté";
	}
	
	public void addNoeud(Object lat, Object lon, int position) throws IOException{
		EnrNoeud n = new EnrNoeud();
		n.lire("Donnees/Track1.xml");
		n.ecrire(lat, lon,position);
		n.enregistrer();
	}

	public void delBus(Object numero) throws JDOMException, IOException {
		EnrBus b = new EnrBus();
		b.lire("Donnees/ListeBus.xml");
		b.supprimer((String) numero);
		b.enregistrer();
	}
	
	private double distance(JSONObject noeud, String lat, String lon){
        double lat1 = Double.parseDouble(noeud.getString("Latitude"));
        double lon1 = Double.parseDouble(noeud.getString("Longitude"));
        double lat2 = Double.parseDouble(lat);
        double lon2 = Double.parseDouble(lon);
        double puissanceLat = pow(lat2 - lat1, 2);
        double puissanceLon = pow(lon2 - lon1, 2);                  
        
        return sqrt(puissanceLat + puissanceLon);

	}
}
