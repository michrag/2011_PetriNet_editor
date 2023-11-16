package pnEditorApp;

import javax.swing.UIManager;

import framework.exception.ApplicationException;

public class Main
{

    public static void main(String[] args)
    {

        try
        {
            UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        PNeditorApplication app = new PNeditorApplication();

        try
        {
            app.run();
        }
        catch(ApplicationException e)
        {
            e.printStackTrace();
        }

    }
}
