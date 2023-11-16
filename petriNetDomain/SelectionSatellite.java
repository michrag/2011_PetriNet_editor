package petriNetDomain;

/**
 * Classe astratta. Il satellite che dovrebbe manifestare visivamente il fatto che il suo pianeta sia selezionato.
 *
 */
public abstract class SelectionSatellite extends Satellite
{
    /**
     * Costruisce un oggetto {@link SelectionSatellite}>, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link SelectionSatellite}.
     */
    public SelectionSatellite(PNelement planet)
    {
        super(planet);
    }

    @Override
    public void accept(IPNEVisitor v)
    {
        v.visitSelectionSatellite(this);
    }

    @Override
    public String getName()
    {
        return "SelectionSatellite of " + getPlanet().getName();
    }

}
