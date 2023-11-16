package pNeditor;

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;

import javax.swing.Icon;

/**
 * Interfaccia che deve essere implementata da una Controller Factory. Un "Controller" è, ad esempio ma non necessariamente, un
 * {@link MouseAdapter} o un {@link KeyListener}.
 *
 */
public interface IControllerFactory
{
    /**
     *
     * @return il nome della {@link IControllerFactory}
     */
    public String getName();

    /**
     *
     * @return l'icona della {@link IControllerFactory}
     */
    public Icon getIcon();

    /**
     * Installa sulla {@link PNeditorView} view i Controller prodotti da questa {@link IControllerFactory}.
     * @param view la vista su cui installare i Controller prodotti dalla {@link IControllerFactory}
     */
    public void installOn(PNeditorView view);

    /**
     * Disinstalla dalla {@link PNeditorView} view i Controller prodotti da questa {@link IControllerFactory}.
     * @param view la vista da cui disinstallare i Controller prodotti dalla {@link IControllerFactory}
     */
    public void uninstallFrom(PNeditorView view);
}
