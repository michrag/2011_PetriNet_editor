package graphicDomain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import petriNetDomain.JointNode;

/**
 * Rappresentazione grafica di {@link JointNode}.
 *
 *
 */
public class GraphicJointNode extends GraphicNode
{
    private boolean paintIt;

    /**
     * Costruisce un oggetto {@link GraphicJointNode} che rappresenta il {@link JointNode} <code>jn</code>.
     */
    public GraphicJointNode(JointNode jn)
    {
        super(jn);
        paintIt = jn.isVisible();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if(paintIt)
        {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
    }

    @Override
    public void drawYourselfOn(Graphics g, Rectangle graphicsRect)
    {
        if(paintIt)
        {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.BLACK);
            g.drawRect(getX() - graphicsRect.x, getY() - graphicsRect.y, getSize().width - 1, getSize().height - 1);
        }
    }

}
