package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorView;
import framework.FApplication;
import framework.IView;
import framework.action.FAction;

/**
 * {@link FAction} che esegue lo zoom in.
 *
 */
public class ZoomInAction extends FAction
{
    private static ZoomInAction instance = null;

    public static ZoomInAction getInstance()
    {
        if(instance == null)
        {
            instance = new ZoomInAction();
        }

        return instance;
    }

    protected ZoomInAction()
    {
        super("Zoom In", new ImageIcon(ZoomInAction.class.getResource("images/ZoomIn.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // così è giusto (l'ha fatto jacopo!!)
        IView view = FApplication.getApplication().getActiveDocument().getActiveView();

        if(view instanceof PNeditorView)
        {
            ((PNeditorView) view).zoomIn();
        }
    }

}
