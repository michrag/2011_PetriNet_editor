package pNeditor;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import pNeditor.actions.ControllerFactoryAction;
import pNeditor.actions.ControllerFactoryActionRepository;
import pNeditor.actions.DeleteSelectedElementsAction;
import pNeditor.actions.ExportSelectionAction;
import pNeditor.actions.SetGridCheckAction;
import pNeditor.actions.ZoomInAction;
import pNeditor.actions.ZoomOutAction;
import pNeditor.actions.ZoomResetAction;
import framework.action.FAction;
import framework.action.FCheckAction;
import framework.action.IAction;
import framework.action.predef.EditCopyAction;
import framework.action.predef.EditCutAction;
import framework.action.predef.EditPasteAction;
import framework.action.predef.EditRedoAction;
import framework.action.predef.EditUndoAction;
import framework.dockbar.FDockBar;
import framework.dockbar.FToolBar;
import framework.plugin.FPluginApplication;
import framework.plugin.FPluginDocTemplate;
import framework.plugin.IFeaturePlugin;
import framework.plugin.IPluginDescriptor;
import framework.plugin.exception.PluginInitException;
import framework.ui.FMainFrameWnd;

/**
 * L'editor è un {@link IFeaturePlugin} per una {@link FPluginApplication}. Questa classe è l'implementazione specifica per l'editor
 * di un {@link IFeaturePlugin}.
 *
 */
public class PNeditorPlugin implements IFeaturePlugin
{
    private FToolBar toolbar = null;

    private Clipboard clipboard;
    private DataFlavor dataFlavor;

    private PNeditorDocTemplate pNeditorDocTemplate;

    @Override
    public void initializePlugin(IPluginDescriptor pd)
    throws PluginInitException
    {
    }

    @Override
    public void postInitializePlugin(IPluginDescriptor pd)
    throws PluginInitException
    {
        initClipboard();

    }

    private void initClipboard()
    {
        clipboard = new Clipboard("PNeditorClipboard");

        try
        {
            dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.LinkedHashSet");
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public Clipboard getClipboard()
    {
        return clipboard;
    }

    public DataFlavor getDataFlavor()
    {
        return dataFlavor;
    }

    @Override
    public FPluginDocTemplate[] getFeatures()
    {
        pNeditorDocTemplate = new PNeditorDocTemplate(this);
        return new FPluginDocTemplate[] { pNeditorDocTemplate };
    }

    @Override
    public FDockBar[] createBars(FMainFrameWnd mainFrame)
    {
        if(toolbar == null)
        {
            toolbar = new FToolBar("PNeditor ToolBar");

            for(ControllerFactoryAction action : ControllerFactoryActionRepository.getActions())
            {
                pNeditorDocTemplate.addAction(action);
            }

            for(ControllerFactoryAction action : ControllerFactoryActionRepository.getActions())
            {
                toolbar.add(action);
            }

            toolbar.addSeparator();
            FCheckAction setGridCheckAction = SetGridCheckAction.getInstance();
            toolbar.add(setGridCheckAction);

            FAction zoomInAction = ZoomInAction.getInstance();
            toolbar.add(zoomInAction);

            FAction zoomResetAction = ZoomResetAction.getInstance();
            toolbar.add(zoomResetAction);

            FAction zoomOutAction = ZoomOutAction.getInstance();
            toolbar.add(zoomOutAction);

            toolbar.addSeparator();
            FAction deleteSelectedElementsAction = DeleteSelectedElementsAction
                                                   .getInstance();
            toolbar.add(deleteSelectedElementsAction);

            FAction cutAction = EditCutAction.instance();
            toolbar.add(cutAction);

            FAction copyAction = EditCopyAction.instance();
            toolbar.add(copyAction);

            FAction pasteAction = EditPasteAction.instance();
            toolbar.add(pasteAction);

            FAction undoAction = EditUndoAction.instance();
            toolbar.add(undoAction);

            FAction redoAction = EditRedoAction.instance();
            toolbar.add(redoAction);

            toolbar.addSeparator();
            FAction exportAction = ExportSelectionAction.getInstance();
            toolbar.add(exportAction);

            //FAction printNetInfo = PrintNetInfoAction.getInstance(); // per debug
            //toolbar.add(printNetInfo);
        }

        toolbar.createDocked(mainFrame, FDockBar.TOP, 0, -1, null);

        return new FDockBar[] { toolbar };
    }

    @Override
    public IAction[] getActions()
    {
        return null;
    }

    @Override
    public boolean activate()
    {
        return true;
    }

    @Override
    public void deactivate()
    {
    }

    @Override
    public void shutDownPlugin(IPluginDescriptor pd)
    {
    }

}