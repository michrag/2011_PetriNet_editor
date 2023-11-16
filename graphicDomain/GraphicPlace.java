package graphicDomain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import petriNetDomain.Place;

/**
 * Rappresentazione grafica di {@link Place}.
 *
 *
 */
public class GraphicPlace extends GraphicNode
{
    /**
     * Costruisce un oggetto {@link GraphicPlace} che rappresenta la {@link Place} <code>p</code>.
     */
    public GraphicPlace(Place p)
    {
        super(p);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        g.setColor(Color.black);
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }

    @Override
    public void drawYourselfOn(Graphics g, Rectangle graphicsRect)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillOval(getX() - graphicsRect.x, getY() - graphicsRect.y, getSize().width - 1, getSize().height - 1);
        g.setColor(Color.black);
        g.drawOval(getX() - graphicsRect.x, getY() - graphicsRect.y, getSize().width - 1, getSize().height - 1);
    }

}
