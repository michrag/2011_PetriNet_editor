package pNeditor.defaultControllerFactories;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import pNeditor.IControllerFactory;
import pNeditor.PNeditorDocument;
import pNeditor.PNeditorView;
import petriNetDomain.Place;

/**
 * {@link IControllerFactory} corrispondente alla modalità tramite la quale l'utente può creare nuovi posti.
 *
 */
public class PlaceControllerFactory implements IControllerFactory
{
    /**
     * {@link MouseAdapter} specifico per una {@link PNeditorView}.
     *
     */
    private class ViewMouseAdapter extends MouseAdapter
    {
        private PNeditorView view;

        public ViewMouseAdapter(PNeditorView view)
        {
            this.view = view;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            PNeditorDocument doc = (PNeditorDocument) view.getDocument();

            if(SwingUtilities.isLeftMouseButton(e))
            {
                Point upperLeftCorner = e.getPoint();
                upperLeftCorner.translate(-Place.getDimension().width / 2, -Place.getDimension().height / 2);
                doc.createPlaceAt(view.getGrid().getPoint(upperLeftCorner));
                doc.updateAllViews(null);
            }
        }
    }

    @Override
    public String getName()
    {
        return "Places";
    }

    @Override
    public Icon getIcon()
    {
        return new ImageIcon(PlaceControllerFactory.class.getResource("images/Place.gif"));
    }

    @Override
    public void installOn(PNeditorView view)
    {
        ViewMouseAdapter viewMA = new ViewMouseAdapter(view);
        view.addMouseListener(viewMA);
    }

    @Override
    public void uninstallFrom(PNeditorView view)
    {
        view.removeMouseListeners(ViewMouseAdapter.class);
    }

}
