package main;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import donneesNoeuds.NoeudsHandler;

public class MainDonnees {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory fabrique = SAXParserFactory.newInstance(); 
		SAXParser parseur = fabrique.newSAXParser(); 
		  
		File fichier = new File("DonneesGPS/Track1.xml"); 
		DefaultHandler gestionnaire = new NoeudsHandler(); 
		parseur.parse(fichier, gestionnaire);
	}

}
