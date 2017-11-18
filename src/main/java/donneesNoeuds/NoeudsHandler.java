package donneesNoeuds;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NoeudsHandler extends DefaultHandler{

	//résultats de notre parsing 
	private List<Noeuds> carte; 
	private Noeuds noeud; 
	private int i = 1;
	//flags nous indiquant la position du parseur 
	private boolean inJava, inTrkpt, inEle, inSpeed, inTime; 
	//buffer nous permettant de récupérer les données  
	private StringBuffer buffer; 
  
	/**
	 * Constructeur simple 
	 */
	public NoeudsHandler(){ 
		super(); 
	} 
	
	/**
	 * Détection d'ouverture de la balise XML
	 */
	public void startElement(String uri, String localName, 
			String qName, Attributes attributes) throws SAXException{ 
		if(qName.equals("java")){  
			inJava = true; 
			carte = new LinkedList<Noeuds>();
		}else if(qName.equals("trkpt")){
			noeud = new Noeuds(i);
			i++;
			try{ 
				String lat = attributes.getValue("lat"); 
				String lon = attributes.getValue("lon");
				noeud.setLat(lat); 
				noeud.setLon(lon);
				noeud.setVitesseC((int)( Math.random()*( 50 - 15 + 1 ) ) + 15);
				noeud.setVitesseD((int)( Math.random()*( 50 - 15 + 1 ) ) + 15);
			}catch(Exception e){ 
				//erreur, le contenu de lat et lon ne sont pas des entiers 
				throw new SAXException(e); 
			} 
			inTrkpt = true;
		}
		else { 
			buffer = new StringBuffer(); 
			if(qName.equals("ele")){ 
				inTrkpt = true; 
			}else if(qName.equals("speed")){ 
				inEle = true; 
			}else if(qName.equals("time")){ 
				inSpeed = true; 
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
			carte.add(noeud); 
			noeud = null; 
			inTrkpt = false; 
		}else if(qName.equals("ele")){ 
			noeud.setEle(buffer.toString()); 
			buffer = null; 
			inEle = false; 
		}else if(qName.equals("speed")){ 
			noeud.setSpeed(buffer.toString()); 
			buffer = null; 
			inSpeed = false; 
		}else if(qName.equals("time")){ 
			noeud.setTime(buffer.toString()); 
			buffer = null; 
			inTime = false; 
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
	public List<Noeuds> getCarte(){
		return carte;		
	}
}