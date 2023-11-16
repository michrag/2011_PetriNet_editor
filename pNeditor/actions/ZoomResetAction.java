package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorView;
import framework.FApplication;
import framework.IView;
import framework.action.FAction;

/**
 * {@link FAction} che reimposta il livello di zoom al 100%.
 *
 */
public class ZoomResetAction extends FAction
{

    private static ZoomResetAction instance = null;

    public static ZoomResetAction getInstance()
    {
        if(instance == null)
        {
            instance = new ZoomResetAction();
        }

        return instance;
    }

    protected ZoomResetAction()
    {
        super("Zoom Reset", new ImageIcon(ZoomResetAction.class.getResource("images/ZoomReset.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // così è giusto (l'ha fatto jacopo!!)
        IView view = FApplication.getApplication().getActiveDocument().getActiveView();

        if(view instanceof PNeditorView)
        {
            ((PNeditorView) view).zoomReset();
        }
    }

}
