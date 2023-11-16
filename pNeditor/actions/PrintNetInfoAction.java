package pNeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import pNeditor.PNeditorDocument;
import framework.FApplication;
import framework.FDocument;
import framework.action.FAction;

public class PrintNetInfoAction extends FAction
{
    private static PrintNetInfoAction instance = null;

    public static PrintNetInfoAction getInstance()
    {
        if(instance == null)
        {
            instance = new PrintNetInfoAction();
        }

        return instance;
    }

    protected PrintNetInfoAction()
    {
        super("Print Net Info", new ImageIcon(
                  PrintNetInfoAction.class.getResource("images/Print.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        FDocument doc = FApplication.getApplication().getActiveDocument();

        if(doc instanceof PNeditorDocument)
        {
            System.out.println(((PNeditorDocument) doc).getNetInfo());
        }
    }

}
