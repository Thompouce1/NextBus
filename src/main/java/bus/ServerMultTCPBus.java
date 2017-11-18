package bus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class qui permet de creer un server multithread TCP pour un bus.
 * 
 * @author Quentin Laborde
 *
 */
public class ServerMultTCPBus extends Thread{
    
    private int portBus = 12224;
    private Bus bus;
    
    private ServerSocket socketServer = null;
    boolean enFonction = false;
    
    
    public ServerMultTCPBus(Bus bus,int port){
        this.portBus = port;
        this.bus = bus;
        
    }

    public ServerSocket creationSocketServer(){
        try{
            return new ServerSocket(portBus);
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
            System.out.println("\nConnection d'un client");
            new ThreadServerTCPBus(bus,socket).start();                  
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
            System.out.println("Probleme création server Bus");
        }
        
        System.out.println("Serveur en attente de connexion.");
        
        while(enFonction) {
            attenteConnection(socketServer);
        }
        
    }

        
    
}