package serverInformationStatic;

import java.io.*;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.JDOMException;
import org.json.JSONException;
import org.xml.sax.SAXException;

/**
 * Main class for a thread of the server
 */
public class ServerThread extends Thread
{
    private Socket socket;
    private ProtocoleHandler handler;

    BufferedReader in;
    DataOutputStream out;
    
    /**
     * Constructeur de serveur
     * @param listening 
     */
    public ServerThread(Socket socket, boolean listening)
    {
        handler = new ProtocoleHandler(listening);
        this.socket = socket;
    }
    
    private void creationEntreSortie(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lancer l'execution du serveur
     */
    public void run() {
        
        creationEntreSortie();
        
        
        try {
            while (true) {

                String requete = in.readLine();
                if (requete != null) {
                    System.out.println("Nouvelle requête: " + requete);
                    String answer = handler.gererRequete(requete);
                    out.writeBytes(answer + "\n");
                    System.out.println("Réponse: " + answer);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        } catch (JSONException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
    }
}
