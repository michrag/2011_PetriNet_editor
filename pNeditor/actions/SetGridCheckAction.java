package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorView;
import framework.FApplication;
import framework.FDocument;
import framework.IView;
import framework.action.FCheckAction;

/**
 * {@link FCheckAction} per abilitare o disabilitare la griglia di posizionamento.
 *
 */
public class SetGridCheckAction extends FCheckAction
{
    private static SetGridCheckAction instance = null;

    public static SetGridCheckAction getInstance()
    {
        if(instance == null)
        {
            instance = new SetGridCheckAction();
        }

        return instance;
    }

    protected SetGridCheckAction()
    {
        super("Grid On / Off", new ImageIcon(
                  PrintNetInfoAction.class.getResource("images/Grid.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // così è giusto (l'ha fatto jacopo!!)
        IView view = FApplication.getApplication().getActiveDocument().getActiveView();

        if(view instanceof PNeditorView)
        {
            ((PNeditorView) view).toggleGrid();
        }
    }

    @Override
    public boolean getCheckingCondition()
    {
        FDocument doc = FApplication.getApplication().getActiveDocument();

        if(doc != null)
        {
            IView view = doc.getActiveView();

            if(view instanceof PNeditorView)
            {
                return ((PNeditorView) view).isGridOn();
            }
        }

        return false;
    }

}
