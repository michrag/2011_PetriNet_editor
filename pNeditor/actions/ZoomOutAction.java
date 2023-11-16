package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorView;
import framework.FApplication;
import framework.IView;
import framework.action.FAction;

/**
 * {@link FAction} che esegue lo zoom out.
 *
 */
public class ZoomOutAction extends FAction
{
    private static ZoomOutAction instance = null;

    public static ZoomOutAction getInstance()
    {
        if(instance == null)
        {
            instance = new ZoomOutAction();
        }

        return instance;
    }

    protected ZoomOutAction()
    {
        super("Zoom Out", new ImageIcon(ZoomOutAction.class.getResource("images/ZoomOut.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // così è giusto (l'ha fatto jacopo!!)
        IView view = FApplication.getApplication().getActiveDocument().getActiveView();

        if(view instanceof PNeditorView)
        {
            ((PNeditorView) view).zoomOut();
        }
    }

}
