package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Satellite specifico per un pianeta di tipo {@link Arc}, rappresenta il rettangolo la cui diagonale è la linea dell'{@link Arc},
 * o che ha due lati paralleli ad essa se questa è perfettamente verticale od orizzontale.
 *
 */
public class SelectionArcSatellite extends SelectionSatellite
{
    private Arc planet; // per comodità, altrimenti dovrei castare tutte le chiamate ai metodi getFwdPoint e getBackPoint
    private Point position;
    private int margin;

    /**
     * Costruisce un oggetto {@link SelectionArcSatellite}, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link SelectionArcSatellite}.
     */
    public SelectionArcSatellite(Arc planet)
    {
        super(planet);
        this.planet = planet;
        margin = Math.max(JointNode.getDimension().width, JointNode.getDimension().height);
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
        Point location = new Point(Math.min(planet.getBackEndPoint().x, planet.getFwdEndPoint().x), Math.min(planet.getBackEndPoint().y, planet.getFwdEndPoint().y));

        if(Math.abs(planet.getBackEndPoint().x - planet.getFwdEndPoint().x) <= margin)
        {
            location.translate(-margin, 0);
        }

        if(Math.abs(planet.getBackEndPoint().y - planet.getFwdEndPoint().y) <= margin)
        {
            location.translate(0, -margin);
        }

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
        Dimension dim = new Dimension(Math.abs(planet.getFwdEndPoint().x - planet.getBackEndPoint().x), Math.abs(planet.getFwdEndPoint().y - planet.getBackEndPoint().y));

        if(Math.abs(planet.getBackEndPoint().x - planet.getFwdEndPoint().x) <= margin)
        {
            dim.width += (margin * 2);
        }

        if(Math.abs(planet.getBackEndPoint().y - planet.getFwdEndPoint().y) <= margin)
        {
            dim.height += (margin * 2);
        }

        return dim;
    }

}
