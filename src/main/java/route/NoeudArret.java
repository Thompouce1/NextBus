package route;

/**
 * Class qui permet de creer un noeud arret pour la ligne de bus. 
 * Chaque noeud est une position GPS qui indique la position d'un arret de bus.
 * 
 * @author Quentin Laborde
 *
 */
public class NoeudArret extends Noeud{
    
    private String IP;

    /////////////////////////// Constructeur /////////////////////////////
    
    public NoeudArret(String num, String nom, double lon, double lat) {
        super(num, lon, lat);
        this.nom = nom;
    }
    
    ////////////////////////// Mehtodes //////////////////////////////////
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
}
