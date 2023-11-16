package graphicDomain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import petriNetDomain.Arc;
import petriNetDomain.Node;

/**
 * Rappresentazione grafica di {@link Arc}.
 *
 *
 */
public class GraphicArc extends PNgraphicElement
{
    private Arc arc;

    private Point backEndPoint;
    private Point fwdEndPoint;

    private int circleDiameter;
    private int arrowSize;

    /**
     * Costruisce un oggetto {@link GraphicArc} che rappresenta l'{@link Arc} <code>a</code>.
     */
    public GraphicArc(Arc a)
    {
        super(a);
        arc = a; // per comodità!
        backEndPoint = arc.getBackEndPoint(true);
        fwdEndPoint = arc.getFwdEndPoint(true);
        circleDiameter = Arc.getCircleDiameter();
        arrowSize = Arc.getArrowSize();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        //g.drawRect( 0, 0, getSize().width - 1, getSize().height - 1 ); // debug purpose

        backEndPoint = arc.getBackEndPoint(true);
        fwdEndPoint = arc.getFwdEndPoint(true);

        g.drawLine(backEndPoint.x, backEndPoint.y, fwdEndPoint.x, fwdEndPoint.y);

        if(arc.getType() == Arc.ArcTypeEnum.CIRCLE_ON_TRANSITION)
        {
            if(arc.getLink().getTransition().isVertical())
            {
                if(backEndPoint.x <= fwdEndPoint.x)
                {
                    g.fillOval(fwdEndPoint.x - circleDiameter, fwdEndPoint.y - (circleDiameter / 2), circleDiameter, circleDiameter);
                }
                else
                {
                    g.fillOval(fwdEndPoint.x, fwdEndPoint.y - (circleDiameter / 2), circleDiameter, circleDiameter);
                }
            }
            else
            {
                if(backEndPoint.y <= fwdEndPoint.y)
                {
                    g.fillOval(fwdEndPoint.x - (circleDiameter / 2), fwdEndPoint.y - circleDiameter, circleDiameter, circleDiameter);
                }
                else
                {
                    g.fillOval(fwdEndPoint.x - (circleDiameter / 2), fwdEndPoint.y, circleDiameter, circleDiameter);
                }
            }
        }

        if(arc.getType() == Arc.ArcTypeEnum.ARROW_ON_TRANSITION || arc.getType() == Arc.ArcTypeEnum.ARROW_ON_PLACE)
        {
            drawArrow(g, fwdEndPoint, backEndPoint);
        }

        if(((Arc)getPNelement()).getType() == Arc.ArcTypeEnum.SIMPLE_LINE)
        {
            ; // non c'è da fare nulla! la linea viene disegnata in ogni caso
        }

    }


    @Override
    public void drawYourselfOn(Graphics g, Rectangle graphicsRect)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        //g.drawRect( 0, 0, getSize().width - 1, getSize().height - 1 ); // debug purpose

        backEndPoint = arc.getBackEndPoint(true);
        fwdEndPoint = arc.getFwdEndPoint(true);

        backEndPoint.translate(getX() - graphicsRect.x, getY() - graphicsRect.y);
        fwdEndPoint.translate(getX() - graphicsRect.x, getY() - graphicsRect.y);

        g.drawLine(backEndPoint.x, backEndPoint.y, fwdEndPoint.x, fwdEndPoint.y);

        if(arc.getType() == Arc.ArcTypeEnum.CIRCLE_ON_TRANSITION)
        {
            if(arc.getLink().getTransition().isVertical())
            {
                if(backEndPoint.x <= fwdEndPoint.x)
                {
                    g.fillOval(fwdEndPoint.x - circleDiameter, fwdEndPoint.y - (circleDiameter / 2), circleDiameter, circleDiameter);
                }
                else
                {
                    g.fillOval(fwdEndPoint.x, fwdEndPoint.y - (circleDiameter / 2), circleDiameter, circleDiameter);
                }
            }
            else
            {
                if(backEndPoint.y <= fwdEndPoint.y)
                {
                    g.fillOval(fwdEndPoint.x - (circleDiameter / 2), fwdEndPoint.y - circleDiameter, circleDiameter, circleDiameter);
                }
                else
                {
                    g.fillOval(fwdEndPoint.x - (circleDiameter / 2), fwdEndPoint.y, circleDiameter, circleDiameter);
                }
            }
        }

        if(arc.getType() == Arc.ArcTypeEnum.ARROW_ON_TRANSITION || arc.getType() == Arc.ArcTypeEnum.ARROW_ON_PLACE)
        {
            drawArrow(g, fwdEndPoint, backEndPoint);
        }

        if(((Arc)getPNelement()).getType() == Arc.ArcTypeEnum.SIMPLE_LINE)
        {
            ; // non c'è da fare nulla, la linea viene disegnata in ogni caso
        }
    }

    private void drawArrow(Graphics g, Point fwdEndPoint, Point backEndPoint)
    {
        double angle = Math.PI / 8;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double dx = fwdEndPoint.x - backEndPoint.x;
        double dy = fwdEndPoint.y - backEndPoint.y;
        double d = Math.sqrt(dx * dx + dy * dy);

        if(d == 0)
        {
            return;
        }

        dx = (dx / d) * arrowSize;
        dy = (dy / d) * arrowSize;

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        xPoints[0] = fwdEndPoint.x;
        yPoints[0] = fwdEndPoint.y;
        xPoints[1] = fwdEndPoint.x - (int)(dx * cos - dy * sin);
        yPoints[1] = fwdEndPoint.y - (int)(dx * sin + dy * cos);
        xPoints[2] = fwdEndPoint.x - (int)(dy * sin + dx * cos);
        yPoints[2] = fwdEndPoint.y - (int)(dy * cos - dx * sin);

        g.fillPolygon(xPoints, yPoints, 3);
    }

    /**
     * Imposta i bounds in base alle posizioni di due {@link Node}, in modo tale che questo {@link GraphicArc} sia il più piccolo
     * rettangolo che contiene completamente entrambi i {@link Node}.
     * @param node1 : primo {@link Node} che sarà contenuto all'interno dei bounds.
     * @param node2 : secondo {@link Node} che sarà contenuto all'interno dei bounds.
     */
    public void setBounds(Node node1, Node node2)
    {
        Point node1Location = node1.getPosition();
        Point node2Location = node2.getPosition();

        setBounds(Math.min(node1Location.x, node2Location.x),
                  Math.min(node1Location.y, node2Location.y),
                  Math.max(node1Location.x + node1.getDimensions().width, node2Location.x + node2.getDimensions().width) - Math.min(node1Location.x, node2Location.x),
                  Math.max(node1Location.y + node1.getDimensions().height, node2Location.y + node2.getDimensions().height) - Math.min(node1Location.y, node2Location.y));

        repaint();
    }

    /**
     * Restituisce <code>true</code> se il cerchio di centro <code>point</code> e raggio 5 interseca il segmento disegnato dal
     * {@link GraphicArc}, <code>false</code> altrimenti.
     * @param point : il centro del cerchio.
     * @return <code>true</code> se e solo se il cerchio interseca la linea del {@link GraphicArc}.
     */
    public boolean lineHit(Point point)
    {
        return (new Line2D.Float(backEndPoint.x, backEndPoint.y, fwdEndPoint.x, fwdEndPoint.y).intersects(point.x - 5, point.y - 5, 10, 10));
    }

}
