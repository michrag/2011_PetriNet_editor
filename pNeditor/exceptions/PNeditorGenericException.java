package pNeditor.exceptions;

/**
 * Generica {@link Exception} dell'editor.
 *
 */
public class PNeditorGenericException extends Exception
{
    @Override
    public String getMessage()
    {
        return "Errore generico di PetriNetEditor!";
    }

    @Override
    public void printStackTrace()
    {
        super.printStackTrace();
        //System.out.println(getMessage());
    }

}
