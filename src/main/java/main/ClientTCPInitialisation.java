package main;

import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class qui permet de creer un client pour initialiser les bus et les arret. Permet aussi d'ajouter de nouveaux bus et arrets.
 * 
 * @author Quentin Laborde
 *
 */
public class ClientTCPInitialisation {

    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    
    String requete;
    String reponse;
    
    JSONObject requeteJSON = new JSONObject();
    JSONObject reponseJSON = new JSONObject();
    
    private String IPServerInformation;
    private int portServerInformation;
    
    public ClientTCPInitialisation(String IPServerInformation, int portServerInformation){
        this.IPServerInformation = IPServerInformation;
        this.portServerInformation = portServerInformation;
         
    }

    void connection(){   
        try{
            socket = new Socket(IPServerInformation, portServerInformation);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        }
        catch(Exception e){
               System.out.println("Probleme connection : " + e);
        }
    }
    
    void deconnection(){
        try{
            envoyerRequete("DECONNECTION");
            out.close();
            in.close();
            socket.close();
        }
        
        catch(Exception e){
            System.out.println("Probleme deconnection : " + e);
        }

    }
   
    public int nombreBus(){

        envoyerRequete("NBBUS");
        recupererReponse();

        try{
            return reponseJSON.getInt("NOMBREBUS");
        }
        catch(JSONException e){
            System.out.println("Probleme Json non conforme → "+e);
        }
        
        return 0;


    }
    
    public int nombreArret(){

        envoyerRequete("NBARRETS");
        recupererReponse();

        try{
            return reponseJSON.getInt("NOMBREARRETS");
        }
        catch(JSONException e){
            System.out.println("Probleme Json non conforme → "+e);
        }
        
        return 0;


    }

    public String ajouterBus(String vitesse, String direction){
        String Vitesse[] = {"Vitesse",vitesse};
        String Direction[] = {"Direction",direction};

        
        envoyerRequete("ADDBUS",Vitesse,Direction);

        recupererReponse();

        try{
            return reponseJSON.getString("Reponse");
        }
        catch(JSONException e){
            System.out.println("Probleme Json non conforme → "+e);
        }
        
        return "Probleme !!!!";

    }
    //{\”Requete\”:\”SUPPARRET\”,\”NomArret\”:\”Templ..\”}

    public String supprimerBus(String num){
        String NumBus[] = {"Numero",num};

        
        envoyerRequete("DELBUS",NumBus);

        recupererReponse();

        try{
            return reponseJSON.getString("Reponse");
        }
        catch(JSONException e){
            System.out.println("Probleme Json non conforme → "+e);
        }
        
        return "Probleme !!!!";

    }
    
    
    /**
     * {\”Requete\”:\”ADDARRET\”,\”Nom\”:\”\”,\”IP\”:\”\”,\”Port\”:\”\”,\”Lat\”:\”\”,\”Lon\”:\”\”}
     * @return
     */
    public String ajouterArret(String nom, String lat, String lon){
        String Nom[] = {"Nom",nom};
        String Lat[] = {"Lat",lat};
        String Lon[] = {"Lon",lon};
        
        envoyerRequete("ADDARRET",Nom,Lat,Lon);

        recupererReponse();

        try{
            return reponseJSON.getString("Reponse");
        }
        catch(JSONException e){
            System.out.println("Probleme Json non conforme → "+e);
        }
        
        return "Probleme !!!!";


    }
    
    public String supprimerArret(String nom){
        String NomArret[] = {"NomArret",nom};

        
        envoyerRequete("SUPPARRET",NomArret);

        recupererReponse();

        try{
            return reponseJSON.getString("Reponse");
        }
        catch(JSONException e){
            System.out.println("Probleme Json non conforme → "+e);
        }
        
        return "Probleme !!!!";

    }
    
    private void envoyerRequete(String typeRequete){
        try{
            requeteJSON = new JSONObject();
            requeteJSON.put("Requete", typeRequete);
            requete = requeteJSON.toString();
            System.out.println("\nRequete vers le server → " + requete);
            out.println(requete);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie requete : "+typeRequete);
        }
        
    }
    
    private void envoyerRequete(String typeRequete, String[]...parametre){
        try{
            requeteJSON = new JSONObject();
            requeteJSON.put("Requete", typeRequete);
            
            for(int i = 0; i < parametre.length ; i++){
                requeteJSON.put(parametre[i][0], parametre[i][1]);
            }
            
            requete = requeteJSON.toString();
            System.out.println("\nRequete vers le server → " + requete);
            out.println(requete);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Probleme ennvoie requete : "+typeRequete);
        }
        
    }
    
    private void recupererReponse(){
        try{
            reponse = in.readLine();
            System.out.println("\neponse server → " + reponse);
            reponseJSON = new JSONObject(reponse);
        }
        catch(Exception e){
            System.out.println("Probleme recupération reponse");
        }
        
    }
    
    


}