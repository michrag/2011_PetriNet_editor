package pnEditorApp;

import framework.predefmt.FileMenu;
import framework.tool.FMenuBar;
import framework.ui.FMainFrameWnd;
import framework.ui.MDIMainFrameUIManager;

public class MainFrame extends FMainFrameWnd
{

    public MainFrame()
    {
        super(new MDIMainFrameUIManager());
        FMenuBar myMenuBar = new FMenuBar();
        FileMenu menu = new FileMenu(this);
        //menu.addSeparator();
        myMenuBar.add(menu);
        setJMenuBar(myMenuBar);
    }

}
