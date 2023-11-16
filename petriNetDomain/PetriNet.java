package petriNetDomain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Una rete di Petri. In pratica è un contenitore di {@link Place}, {@link Transition} e {@link Link}, con meccanismi per
 * aggiungerli e rimuoverli.
 *
 */
public class PetriNet
{
    private Set<Place> places;
    private Set<Transition> transitions;
    private Set<Link> links;

    /**
     * Costruisce una nuova rete di Petri.
     */
    public PetriNet()
    {
        places = new LinkedHashSet<Place>();
        transitions = new LinkedHashSet<Transition>();
        links = new LinkedHashSet<Link>();
    }

    /**
     * Restituisce l'insieme di tutte le Places della rete, in sola lettura
     * @return l'insieme di tutte le {@link Place} della rete, in sola lettura
     */
    public Set<Place> getPlaces()
    {
        return Collections.unmodifiableSet(places);
    }

    /**
     * Restituisce l'insieme di tutte le Transitions della rete, in sola lettura
     * @return l'insieme di tutte le {@link Transition}> della rete, in sola lettura
     */
    public Set<Transition> getTransitions()
    {
        return Collections.unmodifiableSet(transitions);
    }

    /**
     * Restituisce l'insieme di tutti i Links della rete, in sola lettura.
     * @return l'insieme di tutti i {@link Link} della rete, in sola lettura.
     */
    public Set<Link> getLinks()
    {
        return Collections.unmodifiableSet(links);
    }

    /**
     * Restituisce l'insieme di tutti i Nodes della rete, in sola lettura.
     * @return l'insieme di tutti i {@link Node} della rete, in sola lettura.
     */
    public Set<Node> getNodes()
    {
        Set<Node> nodes = new LinkedHashSet<Node>();

        nodes.addAll(getPlaces());
        nodes.addAll(getTransitions());

        for(Link l : getLinks())
        {
            nodes.addAll(l.getJointNodes());
        }

        return Collections.unmodifiableSet(nodes);
    }

    /**
     * Restituisce l'insieme di tutti i Nodes e di tutti i Links della rete, in sola lettura.
     * @return l'insieme di tutti i {@link Node}> e di tutti i {@link Link} della rete, in sola lettura.
     */
    public Set<PNelement> getNodesAndLinks()
    {
        Set<PNelement> nodesAndLinks = new LinkedHashSet<PNelement>();

        nodesAndLinks.addAll(getNodes());
        nodesAndLinks.addAll(getLinks());

        return Collections.unmodifiableSet(nodesAndLinks);
    }

    /**
     * Restituisce l'insieme di tutti i Nodes e di tutti gli Arcs della rete, in sola lettura.
     * @return l'insieme di tutti i {@link Node} e di tutti i {@link Arc} della rete, in sola lettura.
     */
    public Set<PNelement> getNodesAndArcs()
    {
        Set<PNelement> nodesAndArcs = new LinkedHashSet<PNelement>();

        nodesAndArcs.addAll(getNodes());

        for(Link l : getLinks())
        {
            nodesAndArcs.addAll(l.getArcs());
        }

        return Collections.unmodifiableSet(nodesAndArcs);
    }

    /**
     * Aggiunge la {@link Place} <code>p</code> alla rete.
     * @param p la {@link Place} da aggiungere.
     */
    public void addPlace(Place p)
    {
        places.add(p);
    }

    /**
     * Rimuove la {@link Place}<code>p</code> dalla rete.
     * @param p la {@link Place} da rimuovere.
     */
    public void removePlace(Place p)
    {
        places.remove(p);
    }

    /**
     * Aggiunge la {@link Transition}<code>t</code> alla rete.
     * @param t la {@link Transition} da aggiungere.
     */
    public void addTransition(Transition t)
    {
        transitions.add(t);
    }

    /**
     * Rimuove la {@link Transition}<code>t</code> dalla rete.
     * @param t la {@link Transition} da rimuovere.
     */
    public void removeTransition(Transition t)
    {
        transitions.remove(t);
    }

    /**
     * Aggiunge il {@link Link}<code>l</code> alla rete
     * @param l il {@link Link} da aggiungere.
     */
    public void addLink(Link l)
    {
        links.add(l);
    }

    /**
     * Rimuove il {@link Link}<code>l</code> dalla rete
     * @param l il {@link Link} da rimuovere.
     */
    public void removeLink(Link l)
    {
        links.remove(l);
    }

    public String getInfo()
    {
        String result = "----- Petri Net: -----\n";

        result += "+ Places:\n";

        for(Place place : places)
        {
            result += place.getName() + "\n";
        }

        result += "+ Transitions:\n";

        for(Transition transition : transitions)
        {
            result += transition.getName() + "\n";
        }

        result += "+ Links:\n";

        for(Link link : links)
        {
            result += link.getName() + "\n";

            result += "  + jointnodes:\n";

            for(JointNode jn : link.getJointNodes())
            {
                result += jn.getName() + "\n";
            }

            result += "  + arcs:\n";

            for(Arc arc : link.getArcs())
            {
                result += arc.getName() + "\n";
            }
        }

        return result;
    }


}
