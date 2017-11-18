package bus.etat;

import exception.CommandeBusException;

/**
 * Class qui permet de creer un l'etat Arret du bus. 
 * 
 * @author Quentin Laborde
 *
 */
public class EtatArret extends EtatBus{
    

    /////////////////////////// Methodes //////////////////////
    
    
    public EtatBus avancer(boolean capaciteMaximum) throws CommandeBusException{
        if(capaciteMaximum){
            return new EtatEnRoutePlein();
        }
    
        return new EtatEnRoute();
    }
      
    public EtatBus remplireBus() throws CommandeBusException{
        return this;
    }
    
    public EtatBus viderBus() throws CommandeBusException{
        return this;
    }
}
