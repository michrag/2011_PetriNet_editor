package graphicDomain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import petriNetDomain.SelectionSatellite;

/**
 * Rappresentazione grafica di {@link SelectionSatellite}.
 *
 *
 */
public class GraphicSelectionRectSatellite extends GraphicSatellite
{
    /**
     * Costruisce un oggetto {@link GraphicSelectionRectSatellite} che rappresenta il {@link SelectionSatellite} <code>selSat</code>.
     */
    public GraphicSelectionRectSatellite(SelectionSatellite selSat)
    {
        super(selSat);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.gray);
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
    }

    @Override
    public void drawYourselfOn(Graphics g, Rectangle graphicsRect) {} // non voglio che si disegni

}
