package serverInformationStatic;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Main Class for the server
 */
public class Server
{
    private int port = 4444;
	private boolean listening = false;

    /**
     * Constructeur pour un serveur TCP
     * @param port
     */
    public Server(int port)
    {
        this.port = port;
    }

    /**
     * Execution du serveur TCP
     */
    public void run()
    {
        ServerSocket server = null;
        listening  = true;
        try
        {
            server = new ServerSocket(port);

            while(listening)
                new ServerThread(server.accept(), listening).start();

            server.close();

        } catch (IOException e) {
            System.err.println("Error");
            System.exit(-1);
        }


    }
    
    public boolean getListening(){
    	return listening;
    }
    
    public void setListening(boolean listening){
    	this.listening = listening;
    }
}
