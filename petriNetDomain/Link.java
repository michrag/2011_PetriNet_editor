package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import petriNetDomain.Arc.ArcTypeEnum;

/**
 * Un collegamento tra una {@link Place} e una {@link Transition} cioè una precondizione, una postcondizione o una precondizione
 * inibitrice. In pratica è un contenitore di oggetti {@link Arc} e oggetti {@link JointNode}, con meccanismi per aggiungerli e
 * rimuoverli.
 */
public class Link extends PNelement
{
    /**
     * Il tipo del {@link Link}.
     *
     */
    public static enum LinkTypeEnum
    {
        NORMAL_PRECONDITION,
        POSTCONDITION,
        INHIBITOR_PRECONDITION,
    }

    private Place place;
    private Transition transition;
    private LinkTypeEnum type;
    private Set<Arc> arcs;
    private Set<JointNode> jointNodes;
    private int jointnodeCounter;

    /**
     * Costruisce un oggetto {@link Link} di tipo <code>type</code>, che collega una {@link Place} e una {@link Transition}.
     * @param place l'oggetto {@link Place} coinvolto nel collegamento
     * @param transition l'oggetto {@link Transition} coinvolto nel collegamento
     * @param type il tipo del {@link Link}.
     */
    public Link(Place place, Transition transition, LinkTypeEnum type)
    {
        super();
        this.place = place;
        this.transition = transition;
        this.type = type;
        arcs = new HashSet<Arc>();
        jointNodes = new LinkedHashSet<JointNode>();
        jointnodeCounter = 1;
        createFirstArc(place, transition, type);
    }

    /**
     * Restituisce la {@link Place} del {@link Link}.
     * @return la {@link Place} del {@link Link}.
     */
    public Place getPlace()
    {
        return place;
    }

    /**
     * Restituisce la {@link Transition} del {@link Link}.
     * @return la {@link Transition} del {@link Link}.
     */
    public Transition getTransition()
    {
        return transition;
    }

    /**
     * Restituisce il tipo del {@link Link}.
     * @return il tipo del {@link Link}.
     */
    public LinkTypeEnum getType()
    {
        return type;
    }

    /**
     * Restituisce l'insieme degli archi posseduti dal {@link Link}, in sola lettura.
     * @return l'insieme degli archi posseduti dal {@link Link}, in sola lettura.
     */
    public Set<Arc> getArcs()
    {
        return Collections.unmodifiableSet(arcs);
    }

    /**
     * Restituisce l'insieme dei {@link JointNode} posseduti dal {@link Link}, in sola lettura.
     * @return l'insieme dei {@link JointNode} posseduti dal {@link Link}, in sola lettura.
     */
    public Set<JointNode> getJointNodes()
    {
        return Collections.unmodifiableSet(jointNodes);
    }

    private void createFirstArc(Place place, Transition transition, Link.LinkTypeEnum linkType)
    {
        if(linkType == LinkTypeEnum.NORMAL_PRECONDITION)
        {
            createArc(place, transition, ArcTypeEnum.ARROW_ON_TRANSITION);
        }

        if(linkType == LinkTypeEnum.INHIBITOR_PRECONDITION)
        {
            createArc(place, transition, ArcTypeEnum.CIRCLE_ON_TRANSITION);
        }

        if(linkType == LinkTypeEnum.POSTCONDITION)
        {
            createArc(transition, place, ArcTypeEnum.ARROW_ON_PLACE);
        }
    }

    /**
     * Istanzia un nuovo {@link JointNode} con posizione <code>position</code> e lo aggiunge a questo {@link Link}.
     * @param position la posizione che avrà il {@link JointNode} creato.
     * @return il {@link JointNode} creato.
     */
    public JointNode createJointNodeAt(Point position)
    {
        String name = new String("jointnode" + jointnodeCounter + " of " + getName());
        jointnodeCounter++;
        JointNode jointnode = new JointNode(name, position, this);
        jointNodes.add(jointnode);

        return jointnode;
    }

    /**
     * Elimina <code>arcToRemove</code>, aggiunge <code>jointnode</code> e crea due nuovi archi: uno che collega <code>jointnode</code>
     * con il "backNode" di <code>arcToRemove</code>, l'altro che collega <code>jointnode</code> con il "forwardNode" di <code>arcToRemove</code>.
     * @param jointnode il {@link JointNode} da aggiungere.
     * @param arcToRemove l'{@link Arc} da rimuovere.
     */
    public void addJointNodeReplacingArc(JointNode jointnode, Arc arcToRemove)
    {
        Node backNode = arcToRemove.getBackNode();
        Node fwdNode = arcToRemove.getFwdNode();

        Arc.ArcTypeEnum oldtype = arcToRemove.getType();

        deleteArc(arcToRemove);

        jointNodes.add(jointnode);

        createArc(backNode, jointnode, Arc.ArcTypeEnum.SIMPLE_LINE);
        createArc(jointnode, fwdNode, oldtype);
    }

    /**
     * Elimina il {@link JointNode} <code>jn</code> e i due archi ad esso collegati e crea un nuovo Arc tra i due nodi che erano
     * colegati dai due archi rimossi.
     * @param jn il {@link JointNode} da rimuovere.
     */
    public void deleteJointNode(JointNode jn)
    {
        if(jointNodes.remove(jn))
        {
            Arc oldBackArc = jn.getBackArc();
            Arc oldFwdArc = jn.getFwdArc();

            Arc.ArcTypeEnum type = oldFwdArc.getType();

            Node backNode = oldBackArc.getBackNode();
            Node fwdNode = oldFwdArc.getFwdNode();

            deleteArc(oldFwdArc);
            deleteArc(oldBackArc);

            createArc(backNode, fwdNode, type);
        }
    }

    /**
     * Crea un arco tra due nodi.
     * @param backNode il nodo per cui l'{@link Arc} creato sarà un arco in avanti (uscente).
     * @param fwdNode il nodo per cui l'{@link Arc} creato sarà un arco all'indietro (entrante).
     * @param type il tipo dell'{@link Arc} da istanziare.
     */
    public void createArc(Node backNode, Node fwdNode, Arc.ArcTypeEnum type)
    {
        Arc arc = new Arc(backNode, fwdNode, type, this);
        arcs.add(arc);
        createUniqueLabel();
    }

    public Arc getArcBetween(Node backNode, Node fwdNode)
    {
        for(Arc arc : arcs)
        {
            if(arc.getBackNode() == backNode && arc.getFwdNode() == fwdNode)
            {
                return arc;
            }
        }

        return null; // TODO: meglio con un'eccezione?
    }

    private Arc getArcLinkedToPlace()
    {
        for(Arc arc : arcs)
        {
            if(arc.getBackNode() instanceof Place || arc.getFwdNode() instanceof Place)
            {
                return arc;
            }
        }

        return null; // TODO: meglio con un'eccezione?
    }

    private Arc getArcLinkedToTransition()
    {
        for(Arc arc : arcs)
        {
            if(arc.getBackNode() instanceof Transition || arc.getFwdNode() instanceof Transition)
            {
                return arc;
            }
        }

        return null; // TODO: meglio con un'eccezione?
    }

    /**
     * Elimina l'{@link Arc} <code>arc</code>.
     * @param arc l'oggetto {@link Arc} da rimuovere.
     */
    public void deleteArc(Arc arc)
    {
        if(arcs.remove(arc))
        {
            arc.getBackNode().removeForward(arc);
            arc.getFwdNode().removeBackward(arc);
        }
    }

    /**
     * Ricollega il {@link Link} alla {@link Place} e alla {@link Transition} per le quali era stato creato.
     */
    public void reLink()
    {
        if((type == LinkTypeEnum.NORMAL_PRECONDITION) || (type == LinkTypeEnum.INHIBITOR_PRECONDITION))
        {
            place.addForward(getArcLinkedToPlace());
            transition.addBackward(getArcLinkedToTransition());
        }

        if(type == LinkTypeEnum.POSTCONDITION)
        {
            transition.addForward(getArcLinkedToTransition());
            place.addBackward(getArcLinkedToPlace());
        }
    }

    /**
     * Scollega il {@link Link} dalla {@link Place} e dalla {@link Transition} per le quali era stato creato.
     */
    public void unLink()
    {
        // devo rimuovere i riferimenti agli archi dei link in place e transition...
        // sia dietro che davanti, tanto se non c'era non succede nulla!
        place.removeBackward(getArcLinkedToPlace());
        place.removeForward(getArcLinkedToPlace());

        transition.removeBackward(getArcLinkedToTransition());
        transition.removeForward(getArcLinkedToTransition());
    }

    /**
     * Determina l'unico {@link Arc} di questo {@link Link} autorizzato a mostrare l'etichetta di testo.
     */
    void createUniqueLabel()
    {
        Arc uniqueLabelOwner = (Arc) arcs.toArray()[0]; // giusto per non rischiare null pointer exception
        int lenght = 0;

        for(Arc arc : arcs)
        {
            Set<Satellite> sats = new HashSet<Satellite>(arc.getSatellites()); // così evito la concurrencyexception

            for(Satellite lsat : sats)
            {
                if(lsat instanceof LabelTextSatellite)
                {
                    arc.removeSatellite(lsat);
                }
            }

            if(Math.abs(arc.getFwdEndPoint().x - arc.getBackEndPoint().x) > lenght)
            {
                lenght = Math.abs(arc.getFwdEndPoint().x - arc.getBackEndPoint().x);
                uniqueLabelOwner = arc;
            }

            if(Math.abs(arc.getFwdEndPoint().y - arc.getBackEndPoint().y) > lenght)
            {
                lenght = Math.abs(arc.getFwdEndPoint().y - arc.getBackEndPoint().y);
                uniqueLabelOwner = arc;
            }
        }

        uniqueLabelOwner.addSatellite(new LabelNameArcSatellite(uniqueLabelOwner));
    }

    @Override
    public void accept(IPNEVisitor v) {}

    @Override
    public String getName()
    {
        String name = new String();

        if(type == LinkTypeEnum.NORMAL_PRECONDITION)
        {
            name = new String("(" + place.getName() + ", " + transition.getName() + ") " + "NormalPrecondition");
        }

        if(type == LinkTypeEnum.INHIBITOR_PRECONDITION)
        {
            name = new String("(" + place.getName() + ", " + transition.getName() + ") " + "InhibitorPrecondition");
        }

        if(type == LinkTypeEnum.POSTCONDITION)
        {
            name = new String("(" + transition.getName() + ", " + place.getName() + ") " + "PostCondition");
        }

        return name;
    }

    // posizione e dimensione non hanno senso per i link!
    @Override
    public Point getPosition()
    {
        return new Point();
    }

    @Override
    public Dimension getDimensions()
    {
        return new Dimension();
    }

    // i link non hanno satelliti (propri)!
    @Override
    public Set<Satellite> getSatellites()
    {
        return new HashSet<Satellite>();
    }

    @Override
    public void notifyAllSatellitesNewPosition() {}

    @Override
    public void removeAllSatellites() {}

    @Override
    public void removeSatellite(Satellite satellite) {}

    @Override
    public void addSatellite(Satellite satellite) {}

    // il link è selezionato, ma i rettangoli di selezione appartengono ai singoli archi...!
    @Override
    public Set<SelectionSatellite> getProperSelectionSatellites()
    {
        Set<SelectionSatellite> selsats = new HashSet<SelectionSatellite>();

        for(Arc arc : arcs)
        {
            selsats.addAll(arc.getProperSelectionSatellites());
        }

        return selsats;
    }

}
