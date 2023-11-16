package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;

import pNeditor.Options;

/**
 * Satellite specifico per un pianeta di tipo {@link Node}, predisposto a rappresentare l'etichetta contenente il nome del pianeta e a
 * posizionarla appena sotto il pianeta e centrata.
 *
 */
public class LabelNameNodeSatellite extends LabelTextSatellite
{
    private static Dimension dimension;

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
        dimension = new Dimension(Options.getInstance().getLabelNodeDimensionPredef());
    }

    private Point position;

    /**
     * Costruisce un oggetto {@link LabelNameNodeSatellite}, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link LabelNameNodeSatellite}.
     */
    public LabelNameNodeSatellite(Node planet)
    {
        super(planet);
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

        location.translate(getPlanet().getDimensions().width / 2 - dimension.width / 2, getPlanet().getDimensions().height);

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
        return dimension;
    }

    @Override
    public String getText()
    {
        return getPlanet().getName();
    }

}
