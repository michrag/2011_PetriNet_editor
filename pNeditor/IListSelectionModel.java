package pNeditor;

import java.util.Collection;

import javax.swing.event.ListSelectionListener;

import petriNetDomain.PNelement;

/**
 * Interfaccia che definisce un modello di selezione per gli elementi dell'editor.
 *
 */
public interface IListSelectionModel
{
    /**
     * Seleziona <code>pnElement</code>, dopo aver eseguito {@link #clearSelection()} se <code>multipleSelection</code> è <code>false</code>.
     * @param pnElement elemento da selezionare
     * @param multipleSelection <code>true</code> se si vuole aggiungere l'elemento senza prima azzerare la selezione, <code>false</code> altrimenti.
     */
    public void select(PNelement pnElement, boolean multipleSelection);

    /**
     * Seleziona tutti gli elementi della collezione.
     * @param pnElements {@link Collection} di elementi da selezionare.
     */
    public void select(Collection<PNelement> pnElements);

    /**
     * Deseleziona <code>pnElement</code>.
     * @param pnElement elemento da deselezionare.
     */
    public void deselect(PNelement pnElement);

    /**
     *
     * @param pnElement elemento di cui si vuole conoscere lo stato di selezione.
     * @return <code>true</code> se <code>pnElement</code> è selezionato, <code>false</code> altrimenti.
     */
    public boolean isSelected(PNelement pnElement);

    /**
     * Azzera la selezione (deseleziona tutti gli elementi che erano selezionati).
     */
    public void clearSelection();

    /**
     *
     * @return <code>true</code> se la selezione è vuota.
     */
    public boolean isSelectionEmpty();

    /**
     *
     * @return tutti gli elementi selezionati.
     */
    public Collection<PNelement> getSelectedItems();

    /**
     * Registra il {@link IListSelectionListener} <code>l</code> su questa {@link IListSelectionModel}.
     * @param l {@link IListSelectionListener} da aggiungere ai {@link IListSelectionListener} registrati.
     */
    public void addListSelectionListener(IListSelectionListener l);

    /**
     * Rimuove il {@link IListSelectionListener} <code>l</code> da questa {@link IListSelectionModel}.
     * @param l {@link IListSelectionListener} da rimuovere dai {@link IListSelectionListener} registrati.
     */
    public void removeListSelectionListener(IListSelectionListener l);

    /**
     * Notifica a tutti i {@link ListSelectionListener} registrati che la lista di oggetti selezionati è cambiata.
     */
    public void fireValueChanged();
}
