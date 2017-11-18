package main;

import java.io.IOException;

import donneesBus.EnrBus;

public class mainXML {

	public static void main(String[] args) throws IOException {
		EnrBus e = new EnrBus();
		e.lire("DonneesBus/ListeBus.xml");
		//e.ecrire();
		//e.afficher();
	}

}
