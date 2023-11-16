package pNeditor;

import java.util.EventObject;

/**
 * {@link EventObject} specifico per una {@link IListSelectionModel}.
 *
 *
 * @see IListSelectionListener
 */
public class ListSelectionEvent extends EventObject
{
    public ListSelectionEvent(Object source)
    {
        super(source);
    }
}
