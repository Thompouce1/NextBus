package bus.etat;

import exception.CommandeBusException;

/**
 * Class qui permet de creer un l'etat en route plein du bus. 
 * 
 * @author Quentin Laborde
 *
 */
public class EtatEnRoutePlein extends EtatBus{
    
    
    /////////////////////////// Methodes //////////////////////
    
    
    public EtatBus arreter() throws CommandeBusException{
        return new EtatArret();
    }
  
}
