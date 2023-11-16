package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorDocument;
import framework.FApplication;
import framework.FDocument;
import framework.action.FAction;

/**
 * {@link FAction} che permette, tramite la libreria <a href="http://java.freehep.org/vectorgraphics/">freeHEP VectorGraphics</a>,
 * di esportare come immagine (vettoriale e non) gli elementi selezionati.
 */
public class ExportSelectionAction extends FAction
{
    private static ExportSelectionAction instance = null;

    public static ExportSelectionAction getInstance()
    {
        if(instance == null)
        {
            instance = new ExportSelectionAction();
        }

        return instance;
    }

    protected ExportSelectionAction()
    {
        super("Export selection as image", new ImageIcon(ExportSelectionAction.class.getResource("images/Export.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        FDocument doc = FApplication.getApplication().getActiveDocument();

        if(doc instanceof PNeditorDocument)
        {
            ((PNeditorDocument) doc).exportSelection();
        }
    }

}
