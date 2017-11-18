package bus.etat;

import exception.CommandeBusException;

/**
 * Class qui permet de creer un l'etat en route du bus. 
 * 
 * @author Quentin Laborde
 *
 */
public class EtatEnRoute extends EtatBus{

    
    /////////////////////////// Methodes //////////////////////
  
    public EtatBus arreter() throws CommandeBusException{
        return new EtatArret();
    }
  
}
