package pNeditor;

/**
 * Listener (observer) di una {@link IListSelectionModel}.
 *
 */
public interface IListSelectionListener
{
    /**
     * Specifica cosa deve fare il {@link IListSelectionListener} quando avviene l'evento {@link ListSelectionEvent} <code>e</code>
     * @param e evento di una {@link IListSelectionModel}
     */
    public void valueChanged(ListSelectionEvent e);
}
