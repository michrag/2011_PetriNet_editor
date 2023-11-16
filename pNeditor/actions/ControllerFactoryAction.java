package pNeditor.actions;

import java.awt.event.ActionEvent;

import pNeditor.IControllerFactory;
import pNeditor.PNeditorView;
import framework.FApplication;
import framework.IView;
import framework.action.FCheckAction;

/**
 * {@link FCheckAction} che installa una determinata {@link IControllerFactory} su una {@link PNeditorView}.
 *
 */
public class ControllerFactoryAction extends FCheckAction
{
    private IControllerFactory controllerFactory;

    /**
     * Costruisce una {@link ControllerFactoryAction} specifica per la {@link IControllerFactory} cf.
     * @param cf la {@link IControllerFactory} da installare su una {@link PNeditorView}.
     */
    public ControllerFactoryAction(IControllerFactory cf)
    {
        super(cf.getName(), cf.getIcon());
        controllerFactory = cf;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // così è giusto (l'ha fatto jacopo!!)
        IView view = FApplication.getApplication().getActiveDocument().getActiveView();

        if(view instanceof PNeditorView)
        {
            ((PNeditorView) view).install(controllerFactory);
        }
    }

    @Override
    public boolean getCheckingCondition()
    {
        IView view = FApplication.getApplication().getActiveDocument().getActiveView();

        if(view instanceof PNeditorView)
        {
            return ((PNeditorView) view).isControllerFactoryInstalled(controllerFactory);
        }

        return false;
    }

}
