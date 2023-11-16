package petriNetDomain;

import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe astratta. Codifica un generico nodo (vertice) di una rete di Petri.
 *
 */
public abstract class Node extends PNelement
{
    private String name;
    private Point position;

    /**
     * Restituisce il punto di ancoraggio, cioè il punto adatto ad essere un estremo del segmento di un {@link Arc}, in funzione di un
     * punto di riferimento.
     * @param referencePoint il punto di riferimento in funzione del quale calcolare il punto di ancoraggio.
     * @return il punto di ancoraggio, nello stesso SdR in cui è la posizione del {@link Node}.
     */
    public abstract Point getAnchorPointRelativeTo(Point referencePoint);

    private Set<Arc> forwardArcs;
    private Set<Arc> backwardArcs;

    /**
     * Costruisce un oggetto {@link Node} con un determinato nome e una determinata posizione.
     * @param name il nome del {@link Node}.
     * @param position la posizione del {@link Node}.
     */
    public Node(String name, Point position)
    {
        super();
        this.name = name;
        this.position = position;
        forwardArcs = new HashSet<Arc>();
        backwardArcs = new HashSet<Arc>();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Point getPosition()
    {
        return position;
    }

    /**
     * Imposta la posizione.
     * @param position la nuova posizione.
     */
    public void setPosition(Point position)
    {
        this.position = position;
        notifyAllSatellitesNewPosition();

        // gli archi devono aggiornare la posizione dei satelliti
        for(Arc forwardArc : getForwardArcs())
        {
            forwardArc.updateSatellitesPosition();
        }

        for(Arc backwardArc : getBackwardArcs())
        {
            backwardArc.updateSatellitesPosition();
        }
    }

    /**
     * Restituisce l'insieme di archi all'indietro (entranti), in sola lettura.
     * @return l'insieme di archi all'indietro (entranti), in sola lettura.
     */
    public Set<Arc> getBackwardArcs()
    {
        return Collections.unmodifiableSet(backwardArcs);
    }

    /**
     * Restituisce l'insieme di archi in avanti (uscenti), in sola lettura.
     * @return l'insieme di archi in avanti (uscenti), in sola lettura.
     */
    public Set<Arc> getForwardArcs()
    {
        return Collections.unmodifiableSet(forwardArcs);
    }

    /**
     * Aggiunge l'{@link Arc} <code>arc</code> all'insieme degli archi all'indietro (entranti).
     * @param arc l'{@link Arc} da aggiungere all'insieme degli archi all'indietro (entranti).
     */
    void addBackward(Arc arc)
    {
        backwardArcs.add(arc);
    }

    /**
     * Aggiunge l'<code>Arc arc</code> all'insieme degli archi in avanti (uscenti).
     * @param arc l'<code>Arc</code> da aggiungere all'insieme degli archi in avanti (uscenti).
     */
    void addForward(Arc arc)
    {
        forwardArcs.add(arc);
    }

    /**
     * Rimuove l'{@link Arc} <code>arc</code> dall'insieme degli archi all'indietro (entranti).
     * @param arc l'{@link Arc} da rimuovere dall'insieme degli archi all'indietro (entranti).
     */
    void removeBackward(PNelement arc)
    {
        backwardArcs.remove(arc);
    }

    /**
     * Rimuove l'{@link Arc} <code>arc</code> dall'insieme degli archi in avanti (uscenti).
     * @param arc l'{@link Arc} da rimuovere dall'insieme degli archi in avanti (uscenti).
     */
    void removeForward(PNelement arc)
    {
        forwardArcs.remove(arc);
    }

    @Override
    public Set<SelectionSatellite> getProperSelectionSatellites()
    {
        HashSet<SelectionSatellite> set = new HashSet<SelectionSatellite>();
        set.add(new SelectionNodeSatellite(this));
        return set;
    }

}
