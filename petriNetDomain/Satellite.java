package petriNetDomain;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe astratta. Un oggetto {@link Satellite} è un oggetto associato ad un altro {@link PNelement} ("pianeta") tale che segua
 * tutti i movimenti del proprio pianeta. In altre parole, deve mantenere la propria posizione relativa al pianeta.
 * @see PNelement
 */
public abstract class Satellite extends PNelement
{
    private PNelement planet;

    /**
     * Costruisce un oggetto {@link Satellite}, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link Satellite}.
     */
    public Satellite(PNelement planet)
    {
        this.planet = planet;
    }

    /**
     * Restituisce il pianeta di questo {@link Satellite}.
     * @return il pianeta.
     */
    public PNelement getPlanet()
    {
        return planet;
    }

    /**
     * Impone a questo satellite di aggiornare la propria posizione.
     */
    public abstract void updatePosition();

    /**
     * Restituisce la posizione del satellite, calcolata in base a quella del pianeta.
     * @param planetPosition la posizione in base alla quale calcolare quella del satellite.
     * @return la posizione del satellite calcolata in funzione di <code>planetPosition</code>.
     */
    abstract Point getPositionRelativeTo(Point planetPosition);

    /**
     * Restituisce il rettangolo occupato da questo satellite se il suo pianeta <em>avesse</em> posizione uguale a <code>planetLocation</code>.
     * Non modifica la reale posizione del satellite. E' necessario per il corretto funzionamento durante il dragging.
     * @param planetLocation la posizione "virtuale" del pianeta.
     * @return il rettangolo occupato dal satellite se il suo pianeta avesse posizione uguale a <code>planetLocation</code>.
     */
    public Rectangle getBoundsWhileDragging(Point planetLocation)
    {
        return new Rectangle(getPositionRelativeTo(planetLocation), getDimensions());
    }

    // i Satelliti non avranno satelliti perciò ridefinisco i metodi relativi lasciandoli vuoti
    @Override
    public Set<Satellite> getSatellites()
    {
        return new HashSet<Satellite>();
    }

    @Override
    public Set<SelectionSatellite> getProperSelectionSatellites()
    {
        return new HashSet<SelectionSatellite>();
    }

    @Override
    public void addSatellite(Satellite satellite) {}

    @Override
    public void notifyAllSatellitesNewPosition() {}

    @Override
    public void removeAllSatellites() {}

    @Override
    public void removeSatellite(Satellite satellite) {}

}
