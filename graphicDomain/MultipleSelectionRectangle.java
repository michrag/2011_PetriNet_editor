package graphicDomain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * Rettangolo di selezione multipla che l'utente disegna tenendo premuto il pulsante sinistro del mouse e muovendo il mouse.
 *
 */
public class MultipleSelectionRectangle extends JComponent
{

    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension dim = getSize();
        g.setColor(Color.gray);
        g.drawRect(0, 0, dim.width - 1, dim.height - 1);
    }

    /**
     * Imposta i bounds di {@link MultipleSelectionRectangle} in modo tale che un vertice sia fisso in <code>startPt</code> e il vertice opposto
     * sia in <code>mousePoint</code>.
     * @param startPt : un vertice del rettangolo
     * @param mousePoint : vertice opposto a quello in <code>startPt</code>
     */
    public void setBounds(Point startPt, Point mousePoint)
    {
        setBounds(Math.min(startPt.x, mousePoint.x),
                  Math.min(startPt.y, mousePoint.y),
                  Math.abs(startPt.x - mousePoint.x),
                  Math.abs(startPt.y - mousePoint.y));

        repaint();
    }

}
