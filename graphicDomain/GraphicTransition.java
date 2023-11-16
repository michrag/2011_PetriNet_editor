package graphicDomain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import petriNetDomain.Transition;

/**
 * Rappresentazione grafica di {@link Transition}.
 *
 *
 */
public class GraphicTransition extends GraphicNode
{
    /**
     * Costruisce un oggetto {@link GraphicTransition} che rappresenta la {@link Transition} <code>t</code>.
     */
    public GraphicTransition(Transition t)
    {
        super(t);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
    }

    @Override
    public void drawYourselfOn(Graphics g, Rectangle graphicsRect)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(getX() - graphicsRect.x, getY() - graphicsRect.y, getSize().width - 1, getSize().height - 1);
    }

}
