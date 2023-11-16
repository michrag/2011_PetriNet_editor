package graphicDomain;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JLabel;

import petriNetDomain.LabelTextSatellite;

/**
 * Rappresentazione grafica di {@link LabelTextSatellite}.
 *
 *
 */
public class GraphicLabelSatellite extends GraphicSatellite
{
    private JLabel label;

    /**
     * Costruisce un oggetto {@link GraphicLabelSatellite} che rappresenta il {@link LabelTextSatellite} <code>labelSat</code>.
     */
    public GraphicLabelSatellite(LabelTextSatellite labelSat)
    {
        super(labelSat);
        setLayout(new FlowLayout()); // fondamentale!
        label = new JLabel(labelSat.getText());
        label.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, LabelTextSatellite.getFontSize()));
        add(label);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g.drawRect( 0, 0, getSize().width - 1, getSize().height - 1 ); // rettangolo intorno alla label
    }


    @Override
    public void drawYourselfOn(Graphics g, Rectangle graphicsRect)
    {
        g.setFont(label.getFont());
        g.drawString(label.getText(), getX() + label.getX() - graphicsRect.x, getY() + label.getY() + 5 - graphicsRect.y); // +5 sulle ordinate altrimenti testo nascosto
    }

}
