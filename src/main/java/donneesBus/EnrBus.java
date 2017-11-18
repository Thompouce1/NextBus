package donneesBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class EnrBus {
	
	public EnrBus(){}
	
	private Document document;

	public void lire(String cheminFichier) {
		   SAXBuilder sxb = new SAXBuilder();
		   try {
		      document = sxb.build(new File(cheminFichier));
		   } catch (JDOMException | IOException e) {
		      System.err.println(e.getMessage());
		   }
	}
	
	public void ecrire(int num, int port, Object object, Object object2){
		
		Element bus = new Element("trkpt");
		bus.addContent(new Element("numero").setText(""+num));
		bus.addContent(new Element("port").setText(""+port));
		bus.addContent(new Element("vitesse").setText(""+object));
		bus.addContent(new Element("direction").setText(""+object2));
		document.getRootElement().addContent(bus);
		
	}
	
	public void enregistrer() throws IOException{
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		sortie.output(this.document, System.out);
		sortie.setFormat(Format.getPrettyFormat());
		sortie.output(document, new FileWriter("Donnees/ListeBus.xml"));

		System.out.println("File Saved!");
	}
	
	public void supprimer(String suppr) throws JDOMException, IOException{
		InputStreamReader flog = null;
		LineNumberReader llog = null;
		String myLine = null;
		String xmldata="";
		String sMethod="";
		try{
		  flog= new InputStreamReader(new FileInputStream("Donnees/ListeBus.xml"));
		  llog= new LineNumberReader(flog);
		  while((myLine = llog.readLine()) != null){
		     xmldata += myLine;
		  }         
		}catch(Exception e){
		    
		}
		 
		SAXBuilder build = new SAXBuilder();
		Reader in = new StringReader(xmldata);
		Element root= null;
		Document doc;
		doc = build.build(in);
		root=doc.getRootElement();
		List<Element> list = root.getChildren("trkpt");
		List enfants = root.getChildren("trkpt");
		Iterator ite = enfants.iterator();
		int i = 0;
		while(ite.hasNext()){
			String numero =   list.get(i).getChildText("numero");
			Element courant = (Element)ite.next();
			if(numero.equals(suppr)){
			    //courant.removeContent();
				//courant.removeContent(Integer.parseInt(suppr));
			    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
				sortie.output(this.document, System.out);
			}
			i++;
		}
		
		
		
	}
}
