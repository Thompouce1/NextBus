package arret;

import route.Ligne;

/**
 * Class qui permet de simuler un arret de bus 
 * et ces communication avec les bus, le server d'information et l'application android
 * 
 * @author Quentin Laborde
 * 
 *
 */
public class Arret extends Thread{
    
    
    private String IPServerInfo = "10.212.115.218";
    //private String IPServerInfo = "10.212.115.127";
    //private String IPServerInfo = "10.188.232.162";

    private int portServerInfo = 4444;
    
    private String IP = "";
    private int port = 1234;
    
    /////////////////////// Doonees Arret /////////////////////////
    
    private String numArret;
    private String nomArret;
     
    /////////////////////// Ligne ///////////////////////////////////
    
    private Ligne ligne;
    
    /////////////////////////// Server et Client TCP //////////////////
    
    ClientMultTCPArret client;
    
    ////////////////////////////: Constructeurs /////////////////////////
    
    public Arret(String num){
        this.numArret = num;
        this.ligne = new Ligne();
    }
    
    ///////////////////////////// Methodes /////////////////////////////////////

    public void run(){
        client = new ClientMultTCPArret(this, IPServerInfo, portServerInfo);

        client.getClientVersServerInfo().initArret(numArret);

        new ServerMultTCPArret(this, port).start();
    }
    

    ////////////////////////// Getters et Setters //////////////////////////////
    

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public ClientMultTCPArret getClient() {
        return client;
    }

    public void setClient(ClientMultTCPArret client) {
        this.client = client;
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public String getNumArret() {
        return numArret;
    }

    public void setNumArret(String nomArret) {
        this.numArret = nomArret;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNomArret() {
        return nomArret;
    }

    public void setNomArret(String nomArret) {
        this.nomArret = nomArret;
    }
    
    
    
    
    
    
}