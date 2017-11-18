package exception;

/**
 * Class qui permet de creer une exception pour les cas ou le bus effectuer une action non autorisé par les etats.
 * 
 * @author Quentin Laborde
 *
 */
@SuppressWarnings("serial")
public class CommandeBusException extends Exception{
    public CommandeBusException(){}
}
