package pnEditorApp;

import framework.dockbar.FDockBar;
import framework.dockbar.FToolBar;
import framework.plugin.FPluginApplication;
import framework.predefmt.BasicToolBar;

public class PNeditorApplication extends FPluginApplication
{

    public PNeditorApplication()
    {
        super("plugins");
        setAppName(Messages.getString("APPLICATION_NAME"));
    }

    @Override
    protected boolean initInstance()
    {

        setMainFrameClass(MainFrame.class,
                          Messages.getString("MAIN_FRAME_TITLE"));    //$NON-NLS-1$

        setDocTemplateSelectorEnabled(true);

        return true;
    }

    protected boolean onPostCreate()
    {
        FToolBar tb = new BasicToolBar();
        tb.createDocked(m_mainFrame, FDockBar.TOP, 0, 0, null);
        return true;
    }

}
