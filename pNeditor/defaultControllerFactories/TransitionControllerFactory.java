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
import petriNetDomain.Transition;

/**
 * {@link IControllerFactory} corrispondente alla modalità tramite la quale l'utente può creare nuove transizioni.
 *
 */
public class TransitionControllerFactory implements IControllerFactory
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
                upperLeftCorner.translate(-Transition.getDimension().width / 2, -Transition.getDimension().height / 2);
                doc.createTransitionAt(view.getGrid().getPoint(upperLeftCorner));
                doc.updateAllViews(null);
            }

        }
    }

    @Override
    public String getName()
    {
        return "Transitions";
    }

    @Override
    public Icon getIcon()
    {
        return new ImageIcon(TransitionControllerFactory.class.getResource("images/Transition.gif"));
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
