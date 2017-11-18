package donneesArrets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class EnrArret {
	
	public EnrArret(){}
	
	private Document document;

	public void lire(String cheminFichier) {
		   SAXBuilder sxb = new SAXBuilder();
		   try {
		      document = sxb.build(new File(cheminFichier));
		   } catch (JDOMException | IOException e) {
		      System.err.println(e.getMessage());
		   }
	}
	
	public void ecrire(int num, Object nom, int port, Object lat, Object lon){
		
		Element arret = new Element("trkpt");
		arret.addContent(new Element("numero").setText(""+num));
		arret.addContent(new Element("nom").setText(""+nom));
		arret.addContent(new Element("port").setText(""+port));
		arret.addContent(new Element("latitude").setText(""+lat));
		arret.addContent(new Element("longitude").setText(""+lon));
		document.getRootElement().addContent(arret);
		
	}
	
	public void incrementerArretPosSupp(int pos){
	    List<Element> listeElement = document.getRootElement().getChildren("trkpt");
	    
	    for(int i = 0; i < listeElement.size(); i++){
	        String num = listeElement.get(i).getChild("numero").getText();
	        if( Integer.parseInt(num) >= pos ){
	        	listeElement.get(i).getChild("numero").setText((Integer.parseInt(num)+1)+"");
	        }	        
	    }
	}
	
	public void enregistrer() throws IOException{
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		//sortie.output(this.document, System.out);
		sortie.setFormat(Format.getPrettyFormat());
		sortie.output(document, new FileWriter("Donnees/ListeArrets.xml"));

		System.out.println("File Saved!");
	}
}
