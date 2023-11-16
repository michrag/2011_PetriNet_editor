package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import pNeditor.Options;

/**
 * Un {@link Arc} è il più piccolo rettangolo che contiene completamente due determinati {@link Node}.
 * A differenza degli oggetti {@link Node}, un {@link Arc} non ha una posizione né una dimensione fissate ma queste sono
 * calcolate dinamicamente ad ogni richiesta in base alla posizione dei due {@link Node} che l'{@link Arc} contiene.
 * Possiede al suo interno due punti che possono essere usati come estremi di un segmento che unisce i due {@link Node}.
 * Un oggetto di tipo {@link Arc} appartiene ad un oggetto di tipo {@link Link}.
 *
 */
public class Arc extends PNelement
{
    /**
     * Il tipo dell'{@link Arc}. Determina il modo in cui sono calcolati i due estremi del segmento e quale "accessorio" (freccia, cerchio o nessuno)
     * dovrà essere disegnato insieme al segmento.
     *
     */
    public static enum ArcTypeEnum
    {
        ARROW_ON_TRANSITION,
        ARROW_ON_PLACE,
        CIRCLE_ON_TRANSITION,
        SIMPLE_LINE
    }

    private static int circleDiameter;

    /**
     * Restituisce il diametro del cerchio da disegnare per rappresentare una precondizione inibitrice.
     * @return diametro del cerchio.
     */
    public static int getCircleDiameter()
    {
        return circleDiameter;
    }

    private static int arrowSize;

    /**
     * Restituisce la dimensione della freccia da disegnare per rappresentare un precondizione o una postcondizione.
     * @return dimensione della freccia.
     */
    public static int getArrowSize()
    {
        return arrowSize;
    }

    /**
     * Aumenta di <code>scaleFactor</code> il diametro del cerchio e la dimensione dela freccia.
     * @param scaleFactor fattore di incremento.
     */
    public static void zoomIn(float scaleFactor)
    {
        circleDiameter += Math.round(circleDiameter / scaleFactor);
        arrowSize += Math.round(arrowSize / scaleFactor);
    }

    /**
     * Riduce di <code>scaleFactor</code> il diametro del cerchio e la dimensione dela freccia.
     * @param scaleFactor fattore di decremento.
     */
    public static void zoomOut(float scaleFactor)
    {
        circleDiameter -= Math.round(circleDiameter / scaleFactor);
        arrowSize -= Math.round(arrowSize / scaleFactor);
    }

    /**
     * Reimposta il diametro del cerchio e la dimensione della freccia ai valori predefiniti.
     */
    public static void zoomReset()
    {
        circleDiameter = Options.getInstance().getArcCircleDiameterPredef();
        arrowSize = Options.getInstance().getArcArrowSizePredef();
    }

    private Link link;
    private Node backNode;
    private Node fwdNode;
    private Arc.ArcTypeEnum type;

    /**
     * Costruisce un oggetto {@link Arc} che contiene due nodi, è di un certo tipo e appartiene ad un {@link Link}.
     * @param backNode il {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente).
     * @param fwdNode il {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante).
     * @param type il tipo dell'{@link Arc}.
     * @param link il {@link Link} che possiede l'{@link Arc}.
     */
    public Arc(Node backNode, Node fwdNode, ArcTypeEnum type, Link link)
    {
        super();
        this.link = link;
        this.type = type;
        setBackward(backNode);
        setForward(fwdNode);
    }

    /**
     * Restituisce il {@link Link} a cui appartiene questo {@link Arc}.
     * @return il {@link Link} proprietario di questo {@link Arc}.
     */
    public Link getLink()
    {
        return link;
    }

    /**
     * Restituisce il {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente).
     * @return il {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente).
     */
    public Node getBackNode()
    {
        return backNode;
    }

    /**
     * Restituisce il {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante).
     * @return il {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante).
     */
    public Node getFwdNode()
    {
        return fwdNode;
    }

    /**
     * Restituisce il tipo dell'{@link Arc}.
     * @return il tipo dell'{@link Arc}.
     */
    public ArcTypeEnum getType()
    {
        return type;
    }

    /**
     * Restituisce il punto in cui il segmento dell'{@link Arc} si unisce al {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente),
     * nello stesso sistema di riferimento in cui è la posizione dell'{@link Arc}.
     * @return punto d'intersezione tra il segmento dell'{@link Arc} e il {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente).
     */
    public Point getBackEndPoint()
    {
        return getBackEndPoint(false);
    }

    /**
     * Restituisce il punto in cui il segmento dell'{@link Arc} si unisce al {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente),
     * nello stesso sistema di riferimento in cui è la posizione dell'{@link Arc} oppure nel sistema di riferimento che ha origine nella posizione dell'{@link Arc}.
     * @param inArcCoordinates se <code>true</code> il punto restituito sarà nel SdR che ha origine nella posizione dell'{@link Arc},
     * se <code>false</code> sarà nello stesso sistema di riferimento in cui è la posizione dell'{@link Arc}.
     * @return punto d'intersezione tra il segmento dell'{@link Arc} e il {@link Node} per il quale questo {@link Arc} è un arco in avanti (uscente).
     */
    public Point getBackEndPoint(boolean inArcCoordinates)
    {
        Point backEndPoint = new Point();

        if(type == ArcTypeEnum.CIRCLE_ON_TRANSITION)
        {
            backEndPoint = new Point(backNode.getAnchorPointRelativeTo(getFwdEndPoint(false)));
        }

        if(type == ArcTypeEnum.ARROW_ON_TRANSITION)
        {
            backEndPoint = new Point(backNode.getAnchorPointRelativeTo(getFwdEndPoint(false)));
        }

        if(type == ArcTypeEnum.ARROW_ON_PLACE)
        {
            backEndPoint = new Point(backNode.getAnchorPointRelativeTo(fwdNode.getCenter()));
        }

        if(type == ArcTypeEnum.SIMPLE_LINE)
        {
            backEndPoint = new Point(backNode.getAnchorPointRelativeTo(fwdNode.getCenter()));
        }

        if(inArcCoordinates)
        {
            backEndPoint.translate(-getPosition().x, -getPosition().y);
        }

        return backEndPoint;
    }

    /**
     * Restituisce il punto in cui il segmento dell'{@link Arc} si unisce al {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante),
     * nello stesso sistema di riferimento in cui è la posizione dell'{@link Arc}.
     * @return punto d'intersezione tra il segmento dell'{@link Arc} e il {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante).
     */
    public Point getFwdEndPoint()
    {
        return getFwdEndPoint(false);
    }

    /**
     * Restituisce il punto in cui il segmento dell'{@link Arc} si unisce al {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante),
     * nello stesso sistema di riferimento in cui è la posizione dell'{@link Arc} oppure nel sistema di riferimento che ha origine nella posizione dell'{@link Arc}.
     * @param inArcCoordinates se <code>true</code> il punto restituito sarà nel SdR che ha origine nella posizione dell'{@link Arc},
     * se <code>false</code> sarà nello stesso sistema di riferimento in cui è la posizione dell'{@link Arc}.
     * @return punto d'intersezione tra il segmento dell'{@link Arc} e il {@link Node} per il quale questo {@link Arc} è un arco all'indietro (entrante).
     */
    public Point getFwdEndPoint(boolean inArcCoordinates)
    {
        Point fwdEndPoint = new Point();

        if(type == ArcTypeEnum.CIRCLE_ON_TRANSITION)
        {
            fwdEndPoint = new Point(fwdNode.getAnchorPointRelativeTo(backNode.getCenter()));
        }

        if(type == ArcTypeEnum.ARROW_ON_TRANSITION)
        {
            fwdEndPoint = new Point(fwdNode.getAnchorPointRelativeTo(backNode.getCenter()));
        }

        if(type == ArcTypeEnum.ARROW_ON_PLACE)
        {
            fwdEndPoint = new Point(fwdNode.getAnchorPointRelativeTo(getBackEndPoint(false)));
        }

        if(type == ArcTypeEnum.SIMPLE_LINE)
        {
            fwdEndPoint = new Point(fwdNode.getAnchorPointRelativeTo(getBackEndPoint(false)));
        }

        if(inArcCoordinates)
        {
            fwdEndPoint.translate(-getPosition().x, -getPosition().y);
        }

        return fwdEndPoint;
    }

    @Override
    public String getName()
    {
        return "(" + backNode.getName() + ", " + fwdNode.getName() + ") " + type;
    }

    @Override
    public void accept(IPNEVisitor v)
    {
        v.visitArc(this);
    }

    @Override
    public Point getPosition()
    {
        return new Point(Math.min(backNode.getPosition().x, fwdNode.getPosition().x), Math.min(backNode.getPosition().y, fwdNode.getPosition().y));
    }

    /**
     * Aggiorna la posizione di tutti i suoi satelliti. Necessario in quanto un {@link Arc}, non avendo una posizione fissa, non ha un metodo del tipo "setPosition" che
     * assolva questo compito.
     */
    void updateSatellitesPosition()
    {
        notifyAllSatellitesNewPosition();
        link.createUniqueLabel();
    }

    @Override
    public Dimension getDimensions()
    {
        return new Dimension(Math.max(backNode.getPosition().x + backNode.getDimensions().width, fwdNode.getPosition().x + fwdNode.getDimensions().width) - Math.min(backNode.getPosition().x, fwdNode.getPosition().x),
                             Math.max(backNode.getPosition().y + backNode.getDimensions().height, fwdNode.getPosition().y + fwdNode.getDimensions().height) - Math.min(backNode.getPosition().y, fwdNode.getPosition().y));
    }

    private void setBackward(Node node)
    {
        backNode = node;
        node.addForward(this);
    }

    private void setForward(Node node)
    {
        fwdNode = node;
        node.addBackward(this);
    }

    // non lo chiama nessuno...
    void removeBackward(Node node)
    {
        node.removeForward(this);
        // non ha senso rimuovere il riferimento al nodo in quanto non può esistere un arco senza nodi
    }

    // non lo chiama nessuno...
    void removeForward(Node node)
    {
        node.removeBackward(this);
        // non ha senso rimuovere il riferimento al nodo in quanto non può esistere un arco senza nodi
    }

    @Override
    public Set<SelectionSatellite> getProperSelectionSatellites()
    {
        HashSet<SelectionSatellite> set = new HashSet<SelectionSatellite>();
        set.add(new SelectionArcSatellite(this));
        return set;
    }

}
