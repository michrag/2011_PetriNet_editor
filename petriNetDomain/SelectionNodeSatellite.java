package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Satellite specifico per un pianeta di tipo {@link Node}, rappresenta un rettangolo di poco più grande di quello occupato dal pianeta
 * che dovrebbe apparire quando il pianeta è selezionato.
 *
 */
public class SelectionNodeSatellite extends SelectionSatellite
{
    private Dimension dim;
    private Point position;
    private int margin;

    /**
     * Costruisce un oggetto {@link SelectionNodeSatellite}, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link SelectionNodeSatellite}.
     */
    public SelectionNodeSatellite(Node planet)
    {
        super(planet);
        int maxDim = Math.max(planet.getDimensions().width, planet.getDimensions().height);
        margin = Math.max(2, Math.round(maxDim / 10));
        dim = new Dimension(planet.getDimensions().width + margin * 2, planet.getDimensions().height + margin * 2);
        updatePosition();
    }

    @Override
    public void updatePosition()
    {
        position = getPositionRelativeTo(getPlanet().getPosition());
    }

    @Override
    public Point getPositionRelativeTo(Point planetLocation)
    {
        Point location = new Point(planetLocation);
        location.translate(-margin, -margin);
        return location;
    }

    @Override
    public Point getPosition()
    {
        return position;
    }

    @Override
    public Dimension getDimensions()
    {
        return dim;
    }

}
