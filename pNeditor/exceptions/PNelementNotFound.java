package pNeditor.exceptions;

import petriNetDomain.PNelement;
import petriNetDomain.PetriNet;

/**
 * {@link Exception} che indica che un certo {@link PNelement} non appartiene ad una data {@link PetriNet}.
 *
 */
public class PNelementNotFound extends PNeditorGenericException
{
    PNelement pnElement;

    public PNelementNotFound(PNelement pnElement)
    {
        this.pnElement = pnElement;
    }

    @Override
    public String getMessage()
    {
        return pnElement.getName() + " non presente nella rete di Petri!";
    }

    @Override
    public void printStackTrace()
    {
        super.printStackTrace();
        //System.out.println(getMessage());
    }

}
