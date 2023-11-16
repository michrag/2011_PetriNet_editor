package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorDocument;
import framework.FApplication;
import framework.FDocument;
import framework.action.FAction;

/**
 * {@link FAction} che consente di eliminare dal documento attivo gli elementi selezionati.
 */
public class DeleteSelectedElementsAction extends FAction
{
    private static DeleteSelectedElementsAction instance = null;

    public static DeleteSelectedElementsAction getInstance()
    {
        if(instance == null)
        {
            instance = new DeleteSelectedElementsAction();
        }

        return instance;
    }

    protected DeleteSelectedElementsAction()
    {
        super("Delete Selected Elements", new ImageIcon(DeleteSelectedElementsAction.class.getResource("images/Delete.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        FDocument doc = FApplication.getApplication().getActiveDocument();

        if(doc instanceof PNeditorDocument)
        {
            ((PNeditorDocument) doc).deleteElements(((PNeditorDocument) doc).getSelectionModel().getSelectedItems());
        }

    }

}
