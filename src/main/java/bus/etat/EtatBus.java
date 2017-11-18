package bus.etat;

import exception.CommandeBusException;

/**
 * Class qui permet de creer un l'etat parent du bus. 
 * 
 * @author Quentin Laborde
 *
 */
public class EtatBus {
    
    
    
    /////////////////////////// Constructeur //////////////////
    
    public EtatBus(){
        
    }
    
    /////////////////////////// Methodes //////////////////////
    
    
    public EtatBus avancer(boolean capaciteMaximum) throws CommandeBusException{
        throw new CommandeBusException();
    }
    
    public EtatBus arreter() throws CommandeBusException{
        throw new CommandeBusException();
    }
    
    public EtatBus remplireBus() throws CommandeBusException{
        throw new CommandeBusException();
    }
    
    public EtatBus viderBus() throws CommandeBusException{
        throw new CommandeBusException();
    }
}
