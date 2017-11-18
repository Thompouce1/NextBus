package donneesArrets;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArretsHandler extends DefaultHandler{


		//résultats de notre parsing 
		private List<Arrets> listArrets; 
		private Arrets arrets;
		//flags nous indiquant la position du parseur 
		private boolean inJava, inTrkpt, inNum, inPort, inNom, inLat, inLong; 
		//buffer nous permettant de récupérer les données  
		private StringBuffer buffer; 
	  
		/**
		 * Constructeur simple 
		 */
		public ArretsHandler(){ 
			super(); 
		} 
		
		/**
		 * Détection d'ouverture de la balise XML
		 */
		public void startElement(String uri, String localName, 
				String qName, Attributes attributes) throws SAXException{ 
			if(qName.equals("java")){  
				inJava = true; 
				listArrets = new LinkedList<Arrets>();
			}else if(qName.equals("trkpt")){
				arrets = new Arrets();
				inTrkpt = true;
			}
			else { 
				buffer = new StringBuffer(); 
				if(qName.equals("numero")){ 
					inNum = true; 
				}else if(qName.equals("port")){ 
					inPort = true; 
				}else if(qName.equals("nom")){ 
					inNom = true; 
				}else if(qName.equals("latitude")){
					inLat = true;
				}else if(qName.equals("longitude")){
					inLong = true;
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
				listArrets.add(arrets); 
				arrets = null; 
				inTrkpt = false; 
			}else if(qName.equals("numero")){ 
				arrets.setNumero(Integer.parseInt(buffer.toString())); 
				buffer = null; 
				inNum = false; 
			}else if(qName.equals("port")){ 
				arrets.setPort(Integer.parseInt(buffer.toString())); 
				buffer = null; 
				inPort = false; 
			}else if(qName.equals("nom")){ 
				arrets.setNom(buffer.toString()); 
				buffer = null; 
				inNom = false; 
			}else if(qName.equals("latitude")){ 
				arrets.setLatitude(Double.parseDouble(buffer.toString())); 
				buffer = null; 
				inLat = false; 
			}else if(qName.equals("longitude")){ 
				arrets.setLongitude(Double.parseDouble(buffer.toString())); 
				buffer = null; 
				inLat = false; 
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
		public List<Arrets> getListArrets(){
			return listArrets;		
		}
	}