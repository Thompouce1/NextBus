package arret;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class qui permet la creation d'un server multiThread TCP pour un arret de bus. 
 * Ce server fournie des service pour l'application Android.
 * 
 * @author Quentin Laborde
 *
 */
public class ServerMultTCPArret extends Thread{

    private int portArret = 1234;
    private Arret arret;
    
    private ServerSocket socketServer = null;
    boolean enFonction = false;
    
    
    public ServerMultTCPArret(Arret arret,int port){
        portArret = port;
        this.arret = arret;
    }

    public ServerSocket creationSocketServer(){
        try{
            return new ServerSocket(portArret);
        }
        catch(IOException e){
            System.out.println("Creation socket Echec : " + e);
            System.exit(1);
        }
        return null;
        
    }
    

    public void attenteConnection(ServerSocket socketServer){
        try{
            Socket socket = socketServer.accept();
            System.out.println("\n\n\nConnection d'un client\n\n\n");
            new ThreadServerTCPArret(arret,socket).start();                  
        }
        catch(IOException e){
            System.out.println("Probleme accept");
            System.exit(0);
        }
        

    }
    
    
    
    public void run(){
        enFonction = true;
        
        try{
            socketServer = creationSocketServer();
        }
        catch(Exception e){
            System.out.println("probleme création sockete server Arret → "+e);
        }

        System.out.println("Serveur en attente de connexion.");
        
        while(enFonction) {
            attenteConnection(socketServer);
        }
        
    }

        
    
}