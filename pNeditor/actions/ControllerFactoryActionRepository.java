package pNeditor.actions;

import java.util.ArrayList;
import java.util.List;

import pNeditor.IControllerFactory;
import framework.plugin.ExtensionPointManager;
import framework.plugin.IExtensionPoint;
import framework.plugin.exception.ExtensionPointException;

/**
 * Repository di {@link ControllerFactoryAction} che fornisce tutte le {@link ControllerFactoryAction} disponibili, ciascuna creata per ognuna
 * delle {@link IControllerFactory} definite nei punti d'estensione del Plugin, cioè recuperate tramite l'{@link ExtensionPointManager}.
 *
 */
public class ControllerFactoryActionRepository
{
    private static List<ControllerFactoryAction> actions = null;

    /**
     *
     * @return tutte le {@link ControllerFactoryAction} definite nei punti d'estensione del Plugin.
     */
    public static List<ControllerFactoryAction> getActions()
    {
        if(actions == null)
        {
            createActions();
        }

        return actions;
    }

    private static void createActions()
    {
        try
        {
            actions = new ArrayList<ControllerFactoryAction>();

            IExtensionPoint extPt = ExtensionPointManager.getInstance().getExtensionPoint("PNeditor-extension-point");
            IControllerFactory[] controllerFactories = (IControllerFactory[]) extPt.getExtensions("controller-contributors");

            for(IControllerFactory cf : controllerFactories)
            {
                actions.add(new ControllerFactoryAction(cf));
            }
        }
        catch(ExtensionPointException e)
        {
            e.printStackTrace();
        }
    }

}
