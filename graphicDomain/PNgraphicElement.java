package graphicDomain;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.qenherkhopeshef.graphics.vectorClipboard.SimpleClipGraphics;

import petriNetDomain.PNelement;

/**
 * Classe astratta. Rappresentazione grafica di {@link PNelement}.
 *
 *
 */
public abstract class PNgraphicElement extends JComponent
{
    private PNelement elem; // nodo (place, transition, jointnode) o arco

    /**
     * Costruisce un oggetto {@link PNgraphicElement} che rappresenta il {@link PNelement} <code>elem</code>.
     */
    public PNgraphicElement(PNelement elem)
    {
        this.elem = elem;
        setOpaque(false);
    }

    /**
     * Restituisce il {@link PNelement} che è rappresentato da questo {@link PNgraphicElement}.
     * @return il {@link PNelement} rappresentato.
     */
    public PNelement getPNelement()
    {
        return elem;
    }

    /**
     * Restituisce il centro di questo {@link PNgraphicElement}, nelle coordinate del parent.
     * @return il centro nelle coordinate del parent.
     */
    public Point getCenter()
    {
        return new Point(getX() + getSize().width / 2, getY() + getSize().height / 2);
    }

    /**
     * Metodo necessario alla libreria <a href="http://comp.qenherkhopeshef.org/jvectCutAndPaste">jVectCliboard</a> affinché
     * questo {@link PNgraphicElement} sia disegnato nell'immagine che sarà posta nella Clipboard di sistema.
     * @param g : oggetto {@link Graphics} ottenuto da jVectClipboard
     * @param graphicsRect : rettangolo che identifica la porzione del parent di questo {@link PNgraphicElement} che verrà disegnata:
     * deve avere Location nelle coordinate del parent di questo {@link PNgraphicElement} e dimensioni pari a quelle della {@link SimpleClipGraphics}
     * di jVectClipboard che ha fornito <code>g</code>.
     */
    public abstract void drawYourselfOn(Graphics g, Rectangle graphicsRect);

}
