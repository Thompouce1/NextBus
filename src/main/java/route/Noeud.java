package route;

import bus.Bus;

/**
 * Class qui permet de creer un noeud pour la ligne de bus. 
 * Chaque noeud est une position GPS.
 * 
 * @author Quentin Laborde
 *
 */
public class Noeud {
    
    private String num;
    protected String nom;
    
    private Noeud liaison1;
    private Noeud liaison2;
    
    private Bus busPresent = null;
    
    private Position position ;
    
    
    
    /////////////////// Constructeur //////////////////////////////////

    public Noeud() {}
    
    public Noeud(String nom, double lon, double lat) {
        this.num = nom;
        this.position = new Position(lon, lat);
    }
    
    public Noeud(String num, double lon, double lat,Noeud l1, Noeud l2){
        this.num = num;
        this.position = new Position(lon, lat);
        this.liaison1 = l1;
        this.liaison2 = l2;
    }
    
    ////////////////////////// Mehtode ///////////////////////////////////
    
    

    
    ///////////////////////// Getter et Setter ////////////////////////
    
    public Noeud getLiaison1() {
        return liaison1;
    }

    public void setLiaison1(Noeud liaison1) {
        this.liaison1 = liaison1;
    }

    public Noeud getLiaison2() {
        return liaison2;
    }

    public void setLiaison2(Noeud liaison2) {
        this.liaison2 = liaison2;
    }

    public Bus getBusPresent() {
        return busPresent;
    }

    public void setBusPresent(Bus busPresent) {
        this.busPresent = busPresent;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String nom) {
        this.num = nom;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    


    
    
    
    

}
