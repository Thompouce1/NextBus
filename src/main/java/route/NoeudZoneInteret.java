package route;

/**
 * Class qui permet de creer un noeud zonne d'interet pour la ligne de bus. 
 * Chaque noeud est une position GPS qui indique la position d'une zonne d'interet.
 * 
 * @author Quentin Laborde
 *
 */
public class NoeudZoneInteret extends Noeud{
    
    public NoeudZoneInteret(String nom, double lon, double lat){
        super(nom, lon, lat);
    }

}
