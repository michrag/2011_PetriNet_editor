package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;

import pNeditor.Options;

/**
 * Satellite specifico per un pianeta di tipo {@link Arc}, predisposto a rappresentare l'etichetta contenente il nome del pianeta e a
 * posizionarla al centro del rettangolo occupato dal pianeta.
 *
 */
public class LabelNameArcSatellite extends LabelTextSatellite
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
        dimension = new Dimension(Options.getInstance().getLabelArcDimensionPredef());
    }

    private Point position;
    private Arc arc; // per comodità

    /**
     * Costruisce un oggetto {@link LabelNameArcSatellite}, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link LabelNameArcSatellite}.
     */
    public LabelNameArcSatellite(Arc planet)
    {
        super(planet);
        arc = planet;
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

        location = new Point(getPlanet().getCenter());
        location.translate(-(dimension.width / 2), 0);

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
        return arc.getLink().getName(); // vogliamo mostrare il nome del link dell'arco, non dell'arco!
    }

}
