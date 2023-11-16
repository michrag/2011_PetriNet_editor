package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;

import pNeditor.Options;

/**
 * Un posto (place) di una rete di Petri.
 *
 */
public class Place extends Node
{
    private static Dimension dimension;

    /**
     * Restituisce la dimensione statica (attributo della classe).
     * @return la dimensione statica.
     */
    public static Dimension getDimension()
    {
        return dimension;
    }

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
        dimension = new Dimension(Options.getInstance().getPlaceDimensionPredef()); // ?????
    }

    @Override
    public Dimension getDimensions()
    {
        return dimension;
    }

    /**
     * Costruisce un oggetto {@link Place} con un determinato nome e una determinata posizione.
     * @param name il nome della {@link Place}.
     * @param position la posizione della {@link Place}.
     */
    public Place(String name, Point position)
    {
        super(name, position);
    }

    @Override
    public void accept(IPNEVisitor v)
    {
        v.visitPlace(this);
    }

    @Override
    public Point getAnchorPointRelativeTo(Point referencePoint)
    {
        Point anchor = getCenter();
        int radius = getDimensions().width / 2;

        double b = Math.abs(anchor.y - referencePoint.y); // cateto
        double c = Math.abs(anchor.x - referencePoint.x); // cateto

        if(b == 0 || c == 0) // triangolo degenere
        {
            if(b == 0)
            {
                if(referencePoint.x < anchor.x)
                {
                    anchor.translate(-radius, 0);
                }
                else
                {
                    anchor.translate(radius, 0);
                }
            }

            if(c == 0)
            {
                if(referencePoint.y < anchor.y)
                {
                    anchor.translate(0, -radius);
                }
                else
                {
                    anchor.translate(0, radius);
                }
            }
        }
        else
        {
            double beta = Math.atan(b / c); // angolo opposto al cateto b
            double dx = radius * Math.cos(beta);
            double dy = radius * Math.sin(beta);

            if(referencePoint.x < anchor.x && referencePoint.y < anchor.y)
            {
                anchor.translate((int) - dx, (int) - dy);
            }

            if(referencePoint.x > anchor.x && referencePoint.y < anchor.y)
            {
                anchor.translate((int)dx, (int) - dy);
            }

            if(referencePoint.x > anchor.x && referencePoint.y > anchor.y)
            {
                anchor.translate((int)dx, (int)dy);
            }

            if(referencePoint.x < anchor.x && referencePoint.y > anchor.y)
            {
                anchor.translate((int) - dx, (int)dy);
            }
        }

        return anchor;
    }

}
