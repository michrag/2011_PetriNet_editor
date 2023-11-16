package pNeditor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;

import petriNetDomain.PNelement;

/**
 * Implementazione di default di {@link IListSelectionModel}.
 *
 */
public class DefaultListSelectionModel implements IListSelectionModel
{
    private IdentityHashMap<PNelement, PNelement> selectedItems;
    private HashSet<IListSelectionListener> listeners;

    public DefaultListSelectionModel()
    {
        selectedItems = new IdentityHashMap<PNelement, PNelement>();
        listeners = new HashSet<IListSelectionListener>();
    }

    @Override
    public void select(PNelement pnElement, boolean multipleSelection)
    {
        if(!multipleSelection)
        {
            clearSelection();
        }

        selectedItems.put(pnElement, pnElement);
        fireValueChanged();
    }

    @Override
    public void select(Collection<PNelement> pnElements)
    {
        for(PNelement elem : pnElements)
        {
            select(elem, true);
        }
    }

    @Override
    public void deselect(PNelement pnElement)
    {
        selectedItems.remove(pnElement);
        fireValueChanged();
    }

    @Override
    public boolean isSelected(PNelement pnElement)
    {
        return selectedItems.containsKey(pnElement);
    }

    @Override
    public void clearSelection()
    {
        selectedItems.clear();
        fireValueChanged();
    }

    @Override
    public boolean isSelectionEmpty()
    {
        return selectedItems.isEmpty();
    }

    @Override
    public void addListSelectionListener(IListSelectionListener l)
    {
        listeners.add(l);
    }

    @Override
    public void removeListSelectionListener(IListSelectionListener l)
    {
        listeners.remove(l);
    }

    @Override
    public void fireValueChanged()
    {
        ListSelectionEvent e = new ListSelectionEvent(this);

        for(IListSelectionListener l : listeners)
        {
            l.valueChanged(e);
        }

        //stampaElemSelezionati();
    }

    public void stampaElemSelezionati()
    {
        System.out.println("Elementi selezionati:");

        for(PNelement elem : getSelectedItems())
        {
            System.out.println(elem.getName());
        }
    }

    @Override
    public Collection<PNelement> getSelectedItems()
    {
        return Collections.unmodifiableSet(selectedItems.keySet());
    }

}
