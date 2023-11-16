package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe astratta. Codifica un generico elemento di una rete di Petri. E' la radice della gerarchia del package {@link PetriNet}.
 *
 */
public abstract class PNelement implements IPNEVisitable
{
    /**
     * Restituisce il nome del {@link PNelement}.
     * @return il nome dell'oggetto.
     */
    public abstract String getName();

    /**
     * Restituisce la posizione del {@link PNelement}.
     * @return la posizione dell'oggetto.
     */
    public abstract Point getPosition();

    /**
     * Restituisce la dimensione del {@link PNelement}.
     * @return la dimensione dell'oggetto.
     */
    public abstract Dimension getDimensions();

    /**
     * Restituisce il rettangolo occupato dal {@link PNelement}.
     * @return il rettangolo occupato dall'oggetto.
     */
    public Rectangle getRect()
    {
        return new Rectangle(getPosition(), getDimensions());
    }

    /**
     * Restituisce il centro del {@link PNelement}, nello stesso sistema di riferimento in cui è la posizione.
     * @return il centro dell'oggetto.
     */
    public Point getCenter()
    {
        Point center = new Point(getPosition());
        center.translate(getDimensions().width / 2, getDimensions().height / 2);
        return center;
    }

    /**
     * Restituisce l'insieme di {@link SelectionSatellite}> adatti a questo {@link PNelement}.
     * @return i {@link SelectionSatellite} propri di questo oggetto.
     */
    public abstract Set<SelectionSatellite> getProperSelectionSatellites();

    private Set<Satellite> satellites;

    /**
     * Costruisce un oggetto {@link PNelement}.
     */
    public PNelement()
    {
        super();
        satellites = new HashSet<Satellite>();
    }

    /**
     * Aggiunge il {@link Satellite} <code>sat</code> all'insieme dei satelliti di questo {@link PNelement}.
     * @param sat il {@link Satellite} da aggiungere.
     */
    public void addSatellite(Satellite sat)
    {
        satellites.add(sat);
    }

    /**
     * Restituisce tutti i satelliti di questo {@link PNelement}.
     * @return l'insieme di {@link Satellite} propri dell'oggetto.
     */
    public Set<Satellite> getSatellites()
    {
        return Collections.unmodifiableSet(satellites);
    }

    /**
     * Impone a tutti i satelliti di questo {@link PNelement} di aggiornare la propria posizione.
     */
    public void notifyAllSatellitesNewPosition()
    {
        for(Satellite s : satellites)
        {
            s.updatePosition();
        }
    }

    /**
     * Elimina tutti i satelliti di questo {@link PNelement}.
     */
    public void removeAllSatellites()
    {
        satellites.clear();
    }

    /**
     * Rimuove il {@link Satellite}<code>sat</code> dall'insieme dei satelliti di questo {@link PNelement}.
     * @param sat il {@link Satellite} che verrà rimosso.
     */
    public void removeSatellite(Satellite sat)
    {
        satellites.remove(sat);
    }

}
