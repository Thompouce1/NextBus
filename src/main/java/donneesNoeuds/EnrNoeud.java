package donneesNoeuds;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class EnrNoeud {

	public EnrNoeud(){}
	
	private Document document;

	public void lire(String cheminFichier) {
		   SAXBuilder sxb = new SAXBuilder();
		   try {
		      document = sxb.build(new File(cheminFichier));
		   } catch (JDOMException | IOException e) {
		      System.err.println(e.getMessage());
		   }
	}
	
	public void ecrire(Object lat, Object lon, int position){
		
		Element noeud = new Element("trkpt");
		noeud.setAttribute("lat", (String) lat);
		noeud.setAttribute("lon", (String) lon);
		noeud.addContent(new Element("ele").setText("0"));
		noeud.addContent(new Element("speed").setText("0"));
		noeud.addContent(new Element("time").setText("0"));
		document.getRootElement().addContent(position,noeud);
		
	}
	
	public void enregistrer() throws IOException{
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		//sortie.output(this.document, System.out);
		sortie.setFormat(Format.getPrettyFormat());
		sortie.output(document, new FileWriter("Donnees/Track1.xml"));

		System.out.println("File Saved!");
	}
}
