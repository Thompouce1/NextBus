package main;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import donneesArrets.ArretsHandler;
import donneesBus.BusHandler;
import donneesNoeuds.NoeudsHandler;
import serverInformationStatic.RequestHandler;
import serverInformationStatic.Server;

/**
 * Main of the server
 */
public class MainServer
{
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
    {
    	
    	Server server = new Server(4444);
    	System.out.println("Serveur lancé");
        server.run();
        
    }
}

