package petriNetDomain;

import java.awt.Dimension;
import java.awt.Point;

import pNeditor.Options;

/**
 * Una transizione (transition) di una rete di Petri.
 *
 */
public class Transition extends Node
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
        dimension = new Dimension(Options.getInstance().getTransitionDimensionPredef());
    }

    private boolean vertical;

    /**
     * Imposta l'orientazione della transizione: verticale oppure orizzontale.
     * @param vertical se <code>true</code> la transizione sarà rappresentata verticalmente, se <code>false</code> orizzontalmente.
     */
    public void setVertical(boolean vertical)
    {
        this.vertical = vertical;
    }

    /**
     * Restituisce <code>true</code> se l'orientazione della transizione è verticale, <code>false</code> se è orizzontale.
     * @return <code>true</code> se l'orientazione della transizione è verticale, <code>false</code> se è orizzontale.
     */
    public boolean isVertical()
    {
        return vertical;
    }

    /**
     * Cambia l'orientazione della transizione: se era verticale diventa orizzontale e viceversa.
     */
    public void flip()
    {
        if(vertical)
        {
            vertical = false;
            notifyAllSatellitesNewPosition();
            return;
        }
        else
        {
            vertical = true;
        }

        notifyAllSatellitesNewPosition();
    }

    @Override
    public Dimension getDimensions()
    {
        if(vertical)
        {
            return dimension;
        }
        else
        {
            return new Dimension(dimension.height, dimension.width);
        }
    }

    /**
     * Costruisce un oggetto {@link Transition} con un determinato nome e una determinata posizione.
     * @param name il nome della {@link Transition}.
     * @param position la posizione della {@link Transition}.
     */
    public Transition(String name, Point position)
    {
        super(name, position);
        vertical = true;
    }

    @Override
    public void accept(IPNEVisitor v)
    {
        v.visitTransition(this);
    }

    @Override
    public Point getAnchorPointRelativeTo(Point referencePoint)
    {
        if(vertical)
        {
            if(referencePoint.y < getPosition().y)
            {
                return new Point(getPosition().x + getDimensions().width / 2, getPosition().y);
            }

            if(referencePoint.y >= getPosition().y && referencePoint.y <= getPosition().y + getDimensions().height)
            {
                if(referencePoint.x <= getCenter().x)
                {
                    return new Point(getPosition().x, referencePoint.y);
                }
                else
                {
                    return new Point(getPosition().x + getDimensions().width - 1, referencePoint.y);
                }
            }

            if(referencePoint.y > getPosition().y + getDimensions().height)
            {
                return new Point(getPosition().x + getDimensions().width / 2, getPosition().y + getDimensions().height - 1);
            }

            return getCenter();
        }
        else
        {
            if(referencePoint.x < getPosition().x)
            {
                return new Point(getPosition().x, getPosition().y + getDimensions().height / 2);
            }

            if(referencePoint.x >= getPosition().x && referencePoint.x <= getPosition().x + getDimensions().width)
            {
                if(referencePoint.y <= getCenter().y)
                {
                    return new Point(referencePoint.x, getPosition().y);
                }
                else
                {
                    return new Point(referencePoint.x, getPosition().y + getDimensions().height - 1);
                }
            }

            if(referencePoint.x > getPosition().x + getDimensions().width)
            {
                return new Point(getPosition().x + getDimensions().width - 1, getPosition().y + getDimensions().height / 2);
            }

            return getCenter();
        }
    }

}
