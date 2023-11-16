package graphicDomain;

import petriNetDomain.Node;

/**
 * Classe astratta. Rappresentazione grafica di {@link Node}.
 *
 *
 */
public abstract class GraphicNode extends PNgraphicElement
{
    /**
     * Costruisce un oggetto {@link GraphicNode} che rappresenta il {@link Node} <code>n</code>.
     */
    public GraphicNode(Node n)
    {
        super(n);
    }

}
