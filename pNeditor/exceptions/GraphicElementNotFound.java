package pNeditor.exceptions;

import graphicDomain.PNgraphicElement;
import petriNetDomain.PNelement;

/**
 * {@link Exception} che indica che un dato {@link PNelement} non ha un corrispondente {@link PNgraphicElement}.
 *
 */
public class GraphicElementNotFound extends PNeditorGenericException
{
    PNelement pnElement;

    public GraphicElementNotFound(PNelement pnElement)
    {
        this.pnElement = pnElement;
    }

    @Override
    public String getMessage()
    {
        return pnElement.getName() + " non ha un corrispondente elemento grafico in PetriNetEditor!";
    }

    @Override
    public void printStackTrace()
    {
        super.printStackTrace();
        System.out.println(getMessage());
    }

}
