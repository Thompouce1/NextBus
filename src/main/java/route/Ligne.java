package route;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static java.lang.Math.*;

/**
 * Class qui permet de creer une ligne de bus pour qu'ils puise ce déplacer. 
 * Chaque bus a sa ligne, ils ce deplacent sans prendre en compte la position des autres bus
 * 
 * @author Quentin Laborde
 *
 */
public class Ligne {
    
    private HashMap<String,Noeud> listeNoeud = new HashMap<String,Noeud>();
    private HashMap<String,Noeud> listeArret = new HashMap<String,Noeud>();
    private HashMap<String,Noeud> listeZoneInteret = new HashMap<String,Noeud>();
    
    private Noeud positionObjetConnecte = new Noeud();

    
    //////////////////////// Constructeurs ///////////////////////////////
    
    public Ligne(){
        
    }
    
    
    public Ligne(Noeud noeudDepart){
        positionObjetConnecte = noeudDepart;
        
    }
    
    ///////////////////////// Methodes ////////////////////////////
    
    public void addNoeud(Noeud n){
        listeNoeud.put(n.getNum(),n);    
    }
    
    public void addArret(Noeud n){
        listeArret.put(n.getNum(),n);    
        listeNoeud.put(n.getNum(),n);    
    }
    
    public void addZoneInteret(Noeud n){
        listeZoneInteret.put(n.getNum(),n);    
        listeNoeud.put(n.getNum(),n);    
    }
    
    private double distanceEntreNoeud(Noeud noeud1, Noeud noeud2){
        double lat1 = noeud1.getPosition().getLatitude();
        double lat2 = noeud2.getPosition().getLatitude();
        double lon1 = noeud1.getPosition().getLongitude();
        double lon2 = noeud2.getPosition().getLongitude();
        
        double puissanceLat = pow(lat1 - lat2, 2);
        double puissanceLon = pow(lon1 - lon2, 2);
        
        //return sqrt(puissanceLat + puissanceLon);
        
        double R = 6371000;
        
        //degrees * Math.PI / 180
        return R*acos(sin(Math.toRadians(lat1))*sin(Math.toRadians(lat2))+cos(Math.toRadians(lat1))*cos(Math.toRadians(lat2))*cos(Math.toRadians(lon1-lon2)));
    }
    
    public double distanceEntreDeuxArrets(String nomNoeud1,String nomNoeud2, boolean senseDirect){
        Noeud NoeudArretArrive = getNoeudArretAvecNom(nomNoeud2);

        double sommeDistance = 0.0;
        
        Noeud noeudCourant = getNoeudArretAvecNom(nomNoeud1);

        boolean boucle = true;
        
        
        
        while(boucle){
            
            if(senseDirect){
                sommeDistance += distanceEntreNoeud(noeudCourant, noeudCourant.getLiaison2());
                noeudCourant = noeudCourant.getLiaison2();

            }
            else{
                sommeDistance += distanceEntreNoeud(noeudCourant, noeudCourant.getLiaison1());
                noeudCourant = noeudCourant.getLiaison1();

            }
            
            noeudCourant = noeudCourant.getLiaison2();
                        
            if(noeudCourant.equals(NoeudArretArrive)){
                boucle = false;
            }
                    
        }
        
        return sommeDistance;
    }
    
    
    
    public double distanceEntreBusEtArret(String nomNoeud1,boolean senseDirect){

        Noeud NoeudArret = getNoeudArretAvecNom(nomNoeud1);

        double sommeDistance = 0.0;
        
        Noeud noeudCourant = getPositionObjetConnecte();
        
        boolean boucle = true;
        
        while(boucle){
            
            if(senseDirect){
                sommeDistance += distanceEntreNoeud(noeudCourant, noeudCourant.getLiaison2());
                noeudCourant = noeudCourant.getLiaison2();

            }
            else{
                sommeDistance += distanceEntreNoeud(noeudCourant, noeudCourant.getLiaison1());
                noeudCourant = noeudCourant.getLiaison1();

            }
            

            if(noeudCourant.equals(NoeudArret)){
                boucle = false;
            }
                    
        }
        
        return sommeDistance;
    }
    
    public double distance(boolean sence){
        if(sence){
            return distanceEntreNoeud(getPositionObjetConnecte(), getPositionObjetConnecte().getLiaison2());
        }
        else{
            return distanceEntreNoeud(getPositionObjetConnecte(), getPositionObjetConnecte().getLiaison1());
        }
    }
    
    

    ///////////////////////////// Getters et Setters /////////////////////////////
    
    public Noeud getNoeud(String nom){
        return listeNoeud.get(nom);
    }
    
    public Noeud getNoeudArretAvecNom(String nom){
        Set<String> set = listeArret.keySet();
        Iterator<String> it = set.iterator();
        
        for(;it.hasNext();){
            Noeud noeudArret = listeArret.get(it.next());
            System.out.println(noeudArret.getNom());
            if(noeudArret.getNom().equals(nom)){
                return noeudArret;
            }
        }

        return null;
    }
    
    public void prochainNoeud(){
        try{
            positionObjetConnecte = positionObjetConnecte.getLiaison2();          

        }
        catch(Exception e){
            System.out.println("Probleme prochain noeud → "+e);
        }
    }
    
    public void precedentNoeud(){
        try{
            positionObjetConnecte = positionObjetConnecte.getLiaison1();          

        }
        catch(Exception e){
            System.out.println("Probleme precedent noeud → "+e);
        }
    }

    public Noeud getPositionObjetConnecte() {
        return positionObjetConnecte;
    }

    public void setPositionObjetConnecte(Noeud positionBus) {
        this.positionObjetConnecte = positionBus;
    }

    public HashMap<String, Noeud> getListeNoeud() {
        return listeNoeud;
    }

    public void setListeNoeud(HashMap<String, Noeud> listeNoeud) {
        this.listeNoeud = listeNoeud;
    }

    public HashMap<String, Noeud> getListeArret() {
        return listeArret;
    }

    public void setListeArret(HashMap<String, Noeud> listeArret) {
        this.listeArret = listeArret;
    }

    public HashMap<String, Noeud> getListeZoneInteret() {
        return listeZoneInteret;
    }

    public void setListeZoneInteret(HashMap<String, Noeud> listeZoneInteret) {
        this.listeZoneInteret = listeZoneInteret;
    }

    
    
    
}
