package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import pNeditor.Options;

/**
 * Un nodo di giuntura tra due {@link Arc}. A diferenza degli altri {@link Node}, può avere un solo arco entrante e un solo arco uscente.
 *
 */
public class JointNode extends Node
{
    private static Dimension dimension;

    /**
     * Restituisce la dimensione statica (attributo della classe).
     * @return la dimensione statica.
     */
    public static Dimension getDimension()
    {
        return dimension;
    }

    /**
     * Aumenta di <code>scaleFactor</code> la dimensione statica (attributo della classe).
     * @param scaleFactor fattore di incremento
     */
    public static void zoomIn(float scaleFactor)
    {
        dimension.width += Math.round(dimension.width / scaleFactor);
        dimension.height += Math.round(dimension.height / scaleFactor);
    }

    /**
     * Riduce di <code>scaleFactor</code> la dimensione statica (attributo della classe).
     * @param scaleFactor fattore di decremento
     */
    public static void zoomOut(float scaleFactor)
    {
        dimension.width -= Math.round(dimension.width / scaleFactor);
        dimension.height -= Math.round(dimension.height / scaleFactor);
    }

    /**
     * Reimposta la dimensione statica (attributo della classe) al valore predefinito.
     */
    public static void zoomReset()
    {
        dimension = new Dimension(Options.getInstance().getJointnodeDimensionPredef());
    }


    private Link link;
    private Arc backArc;
    private Arc fwdArc;

    /**
     * Costruisce un oggetto {@link JointNode} con un determinato nome, una determinata posizione e un {@link Link} a cui appartiene.
     * @param name il nome del {@link JointNode}.
     * @param position la posizione del {@link JointNode}.
     * @param link il {@link Link} a cui apprtiene il {@link JointNode}.
     */
    public JointNode(String name, Point position, Link link)
    {
        super(name, position);
        this.link = link;
    }

    @Override
    public Set<Arc> getBackwardArcs()
    {
        Set<Arc> backArcs = new LinkedHashSet<Arc>();
        backArcs.add(backArc);
        return Collections.unmodifiableSet(backArcs);
    }

    @Override
    public Set<Arc> getForwardArcs()
    {
        Set<Arc> fwdArcs = new LinkedHashSet<Arc>();
        fwdArcs.add(fwdArc);
        return Collections.unmodifiableSet(fwdArcs);
    }

    /**
     * Restituisce l'{@link Arc} all'indietro (uscente) di questo JointNode.
     * @return l'{@link Arc} all'indietro (uscente) di questo JointNode.
     */
    public Arc getBackArc()
    {
        return backArc;
    }

    /**
     * Restituisce l'{@link Arc} in avanti (entrante) di questo JointNode.
     * @return l'{@link Arc} in avanti (entrante) di questo JointNode.
     */
    public Arc getFwdArc()
    {
        return fwdArc;
    }

    @Override
    void addBackward(Arc arc)
    {
        backArc = arc;
    }

    @Override
    void addForward(Arc arc)
    {
        fwdArc = arc;
    }

    @Override
    void removeBackward(PNelement arc) {} // non c'è da fare nulla in quanto non deve esistere un JN senza archi né un arco senza nodi

    @Override
    void removeForward(PNelement arc) {} // non c'è da fare nulla in quanto non deve esistere un JN senza archi né un arco senza nodi

    @Override
    public Dimension getDimensions()
    {
        return dimension;
    }

    /**
     * Restituisce <code>true</code> se questo JointNode dovrà essere disegnato, <code>false</code> altrimenti.
     * @return <code>true</code> se questo JointNode dovrà essere disegnato, <code>false</code> altrimenti.
     */
    public boolean isVisible()
    {
        return Options.getInstance().getJointnodesVisibility();
    }

    @Override
    public void accept(IPNEVisitor v)
    {
        v.visitJointNode(this);
    }

    /**
     * Restituisce il {@link Link} proprietario di questo JointNode.
     * @return il {@link Link} proprietario di questo JointNode.
     */
    public Link getLink()
    {
        return link;
    }

    /**
     * Restituisce <code>true</code> se questo JointNode è inutile (perché collega due segmenti che appartengono approsimativamente alla stessa retta), <code>false</code> altrimenti.
     * @return <code>true</code> se questo JointNode è da rimuovere, <code>false</code> altrimenti.
     */
    public boolean shouldBeRemoved()
    {
        Point backNodeAnchorPoint = getBackArc().getBackEndPoint();
        Point fwdNodeAnchorPoint = getFwdArc().getFwdEndPoint();

        // i tre lati del triangolo
        double a = backNodeAnchorPoint.distance(fwdNodeAnchorPoint); // lato invisibile
        double b = fwdNodeAnchorPoint.distance(getCenter());
        double c = backNodeAnchorPoint.distance(getCenter());

        // alpha è l'angolo opposto ad a
        double cosalpha = 1;

        if(b != 0 && c != 0) // non si sa mai...
        {
            cosalpha = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2)) / (2 * b * c) ;    // teorema del coseno per triangoli qualunque
        }

        double epsilon = Options.getInstance().getJointNodeRemovingEpsilon();

        if((cosalpha + 1) <= epsilon)
        {
            return true;
        }

        return false;
    }

    @Override
    public Point getAnchorPointRelativeTo(Point referencePoint)
    {
        return getCenter();
    }

}
