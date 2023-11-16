package pNeditor;

import java.awt.Component;

import javax.swing.JScrollPane;

import framework.ui.IComponentDecorator;

/**
 * {@link IComponentDecorator} per avere le barre di scorrimento sulla {@link PNeditorView}.
 *
 */
public class PNeditorScrollableViewDecorator implements IComponentDecorator
{
    @Override
    public Component createDecorator(Component c)
    {
        return new JScrollPane(c);
    }

}
