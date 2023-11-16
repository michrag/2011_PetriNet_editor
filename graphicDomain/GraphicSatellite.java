package graphicDomain;

import petriNetDomain.Satellite;

/**
 * Classe astratta. Rappresentazione grafica di {@link Satellite}.
 *
 *
 */
public abstract class GraphicSatellite extends PNgraphicElement
{
    /**
     * Costruisce un oggetto {@link GraphicSatellite} che rappresenta il {@link Satellite} <code>sat</code>.
     */
    public GraphicSatellite(Satellite sat)
    {
        super(sat);
    }

}
