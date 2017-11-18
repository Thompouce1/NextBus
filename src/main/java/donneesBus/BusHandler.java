package donneesBus;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BusHandler extends DefaultHandler{

	//résultats de notre parsing 
	private List<Bus> listBus; 
	private Bus bus;
	//flags nous indiquant la position du parseur 
	private boolean inJava, inTrkpt, inNum, inPort, inSpeed, inDir; 
	//buffer nous permettant de récupérer les données  
	private StringBuffer buffer; 
  
	/**
	 * Constructeur simple 
	 */
	public BusHandler(){ 
		super(); 
	} 
	
	/**
	 * Détection d'ouverture de la balise XML
	 */
	public void startElement(String uri, String localName, 
			String qName, Attributes attributes) throws SAXException{ 
		if(qName.equals("java")){  
			listBus = new LinkedList<Bus>();
			inJava = true; 
		}else if(qName.equals("trkpt")){
			bus = new Bus();
			inTrkpt = true;
		}
		else { 
			buffer = new StringBuffer(); 
			if(qName.equals("numero")){ 
				inNum = true; 
			}else if(qName.equals("port")){ 
				inPort = true; 
			}else if(qName.equals("vitesse")){ 
				inSpeed = true; 
			}else if(qName.equals("direction")){
				inDir = true;
			}else{ 			
				//erreur, on peut lever une exception 
				throw new SAXException("Balise "+qName+" inconnue."); 
			} 
		} 
	} 
	
	/**
	 * Détection fin de balise 
	 */
	public void endElement(String uri, String localName, String qName) 
			throws SAXException{ 
		if(qName.equals("java")){ 
			inJava = false; 
		}else if(qName.equals("trkpt")){ 
			listBus.add(bus); 
			bus = null; 
			inTrkpt = false; 
		}else if(qName.equals("numero")){ 
			bus.setNumero(Integer.parseInt(buffer.toString())); 
			buffer = null; 
			inNum = false; 
		}else if(qName.equals("port")){ 
			bus.setPort(Integer.parseInt(buffer.toString())); 
			buffer = null; 
			inPort = false; 
		}else if(qName.equals("vitesse")){ 
			bus.setVitesse(Integer.parseInt(buffer.toString())); 
			buffer = null; 
			inSpeed = false; 
		}else if(qName.equals("direction")){ 
			bus.setDirection(buffer.toString()); 
			buffer = null; 
			inDir = false; 
		}else{ 
			//erreur, on peut lever une exception 
			throw new SAXException("Balise "+qName+" inconnue."); 
		}           
	} 
	
	/**
	 * Détection de caractères 
	 */
	public void characters(char[] ch,int start, int length) 
			throws SAXException{ 
		String lecture = new String(ch,start,length); 
		if(buffer != null) buffer.append(lecture);        
	} 
	
	/**
	 * Début du parsing 
	 */
	public void startDocument() throws SAXException { 

	} 
	
	/**
	 * Fin du parsing 
	 */
	public void endDocument() throws SAXException { 
		
	}
	
	/**
	 * Méthode pour accéder à la liste des noeuds
	 * @return la carte
	 */
	public List<Bus> getListBus(){
		return listBus;		
	}
}