package pNeditor;

import framework.FApplication;
import framework.predefmt.EditURMenu;
import framework.predefmt.FileMenu;
import framework.predefmt.WindowMenu;
import framework.tool.FMenuBar;
import framework.ui.FMainFrameWnd;

/**
 * {@link FMenuBar} specifica per l'editor.
 *
 */
public class PNeditorMenuBar extends FMenuBar
{
    public PNeditorMenuBar()
    {
        FMainFrameWnd mainFrame = FApplication.getApplication().getMainFrame();
        add(new FileMenu(mainFrame));
        add(new EditURMenu());
        add(new WindowMenu(mainFrame));
    }

}
