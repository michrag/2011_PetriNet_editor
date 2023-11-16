package pNeditor;

import javax.swing.JMenuBar;

import framework.DefaultDocTemplateUI;
import framework.IMenuBarBuilder;
import framework.plugin.FPluginApplication;
import framework.plugin.FPluginDocTemplate;
import framework.plugin.IFeaturePlugin;
import framework.ui.FChildFrameWnd;

/**
 * {@link FPluginDocTemplate} specifico per l'editor (che è, appunto, un {@link IFeaturePlugin} per una {@link FPluginApplication}).
 *
 */
public class PNeditorDocTemplate extends FPluginDocTemplate
{
    public PNeditorDocTemplate(IFeaturePlugin plugin)
    {
        super(plugin, PNeditorDocument.class, FChildFrameWnd.class, PNeditorView.class, new PNeditorScrollableViewDecorator());

        setDocTemplateName("File PetriNetEditor");
        setNewDocumentName("Nuovo documento PetriNetEditor ");

        DefaultDocTemplateUI ui = (DefaultDocTemplateUI) getUI();
        ui.setFileTypes(new String[] { "File PNEditor (*.pne)", "pne" });

        ui.setMenuBarBuilder(new IMenuBarBuilder()
        {
            @Override
            public JMenuBar buildMenuBar()
            {
                return new PNeditorMenuBar();
            }
        });

    }

}
