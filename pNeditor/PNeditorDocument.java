package pNeditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.swing.JPanel;

import org.freehep.util.export.ExportDialog;
import org.qenherkhopeshef.graphics.vectorClipboard.PictureFormat;
import org.qenherkhopeshef.graphics.vectorClipboard.SimpleClipGraphics;

import pNeditor.exceptions.PNeditorGenericException;
import petriNetDomain.Arc;
import petriNetDomain.JointNode;
import petriNetDomain.LabelNameNodeSatellite;
import petriNetDomain.Link;
import petriNetDomain.Link.LinkTypeEnum;
import petriNetDomain.Node;
import petriNetDomain.PNelement;
import petriNetDomain.PetriNet;
import petriNetDomain.Place;
import petriNetDomain.Transition;
import framework.FDocument;
import framework.IView;
import framework.ccp.ICutCopyPaste;
import framework.plugin.FPluginDocTemplate;
import framework.undoredo.HistoryComposite;
import framework.undoredo.HistoryException;
import framework.undoredo.HistoryManager;
import framework.undoredo.IHistoryComposite;
import framework.undoredo.IHistoryMemento;
import framework.undoredo.IRedoable;
import graphicDomain.GraphicSatellite;

/**
 * {@link FDocument} dell'editor.
 *
 */
public class PNeditorDocument extends FDocument implements IRedoable, ICutCopyPaste, ClipboardOwner
{
    private PetriNet petriNet;

    private int placeCounter = 1;
    private int transitionCounter = 1;

    private IListSelectionModel selectionModel;
    private HistoryManager historyManager;
    private Clipboard clipboard;
    private DataFlavor dataFlavor;

    public PNeditorDocument()
    {
        petriNet = new PetriNet();
        selectionModel = new DefaultListSelectionModel();
        historyManager = new HistoryManager();

        setDefaultOptionsValues();
    }

    private void setDefaultOptionsValues()
    {
        // questo è attualmente praticamente l'unico punto del codice in cui ci sono costanti numeriche, altrove è tutto parametrizzato...
        // (Non è vero, vedi graphicArc ma è giusto così).

        Options.getInstance().setPlaceDimensionPredef(new Dimension(50, 50));
        Options.getInstance().setTransitionDimensionPredef(new Dimension(10, 70));
        Options.getInstance().setJointnodeDimensionPredef(new Dimension(10, 10));
        Options.getInstance().setArcCircleDiameterPredef(Options.getInstance().getTransitionDimensionPredef().width);
        Options.getInstance().setArcArrowSizePredef(16);
        Options.getInstance().setLabelFontSizePredef(12);
        Options.getInstance().setLabelNodeDimensionPredef(new Dimension(100, 30));
        Options.getInstance().setLabelArcDimensionPredef(new Dimension(300, 30));

        Options.getInstance().setGridXstep(20);
        Options.getInstance().setGridYstep(20);

        Options.getInstance().setZoomScaleFactor(5); // le dimensioni verranno diminuite o aumentate di 1/5

        Options.getInstance().setJointNodeRemovingEpsilon(0.01); // epsilon più vicina a 0 (0.001 o 0.0001) => richiesta più precisione da parte dell'utente per eliminare il jn
        Options.getInstance().setJointnodesVisibility(false); // true per debug
    }

    /**
     * Fa ottenere a questo {@link PNeditorDocument} la cartella degli appunti interna all'editor.
     */
    public void initClipboard()
    {
        FPluginDocTemplate pnEditorDocTemplate = (FPluginDocTemplate) getDocTemplate();
        PNeditorPlugin pnEditorPlugin = (PNeditorPlugin) pnEditorDocTemplate.getPlugin();
        clipboard = pnEditorPlugin.getClipboard();
        dataFlavor = pnEditorPlugin.getDataFlavor();
    }

    /**
     *
     * @param toDelete {@link Collection} di {@link PNelement} che saranno eliminati da questo {@link PNeditorDocument}
     */
    public void deleteElements(Collection<PNelement> toDelete)
    {
        Set<PNelement> deletingElements = new LinkedHashSet<PNelement>(toDelete);

        selectionModel.clearSelection();

        IHistoryComposite hc = new HistoryComposite("elimina");

        deleteItemsImpl(deletingElements, hc);

        setModified(true);
        updateAllViews(null);
    }

    private void deleteItemsImpl(Collection<PNelement> toDelete, IHistoryComposite historyComposite)
    {
        for(PNelement elem : toDelete)
        {
            if(elem instanceof Place)
            {
                removePlaceFromPetriNet((Place) elem, historyComposite);
            }

            if(elem instanceof Transition)
            {
                removeTransitionFromPetriNet((Transition) elem, historyComposite);
            }

            if(elem instanceof JointNode)
            {
                removeJointNode((JointNode) elem, historyComposite);
            }

            if(elem instanceof Link)
            {
                removeLinkFromPetriNet((Link) elem, historyComposite);
            }
        }

        historyManager.addMemento(historyComposite);
    }

    /**
     * Elimina tutti i {@link JointNode} che dichiarano di essere inutili.
     */
    public void removeAllUselessJointNodes()
    {
        IHistoryComposite hc = new HistoryComposite("elimina tutti i jointnodes inutili");

        boolean removed = false;

        for(Link link : getLinks())
        {
            ArrayList<JointNode> jointnodes = new ArrayList<JointNode>(link.getJointNodes());

            for(JointNode jn : jointnodes)
            {
                if(jn.shouldBeRemoved())
                {
                    removeJointNode(jn, hc);
                    removed = true;
                }
            }
        }

        if(removed)
        {
            historyManager.addMemento(hc);
        }

        setModified(true);
        updateAllViews(null);
    }

    /**
     * Istanzia una nuova {@link Place}.
     * @param position la posizione della {@link Place} da creare.
     * @return la {@link Place} creata.
     */
    public Place createPlaceAt(Point position)
    {
        Place place = new Place(new String("place" + placeCounter), position);
        placeCounter++;
        place.addSatellite(new LabelNameNodeSatellite(place));

        addPlaceToPetriNet(place, historyManager);

        setModified(true);
        updateAllViews(null);

        return place;
    }

    private void addPlaceToPetriNet(final Place p, IHistoryComposite historyManager)
    {
        petriNet.addPlace(p);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    removePlaceFromPetriNet(p, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    addPlaceToPetriNet(p, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "aggiungi " + p.getName();
                }
            };

            historyManager.addMemento(hm);

        }
    }

    private void removePlaceFromPetriNet(final Place p, IHistoryComposite historyManager)
    {
        selectionModel.deselect(p);

        // per prima cosa rimuovo tutti i link collegati...
        Set<Link> links = new LinkedHashSet<Link>(getLinks());

        for(Link link : links)
        {
            if(link.getPlace() == p)
            {
                removeLinkFromPetriNet(link, historyManager);
            }
        }

        petriNet.removePlace(p);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    addPlaceToPetriNet(p, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    removePlaceFromPetriNet(p, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "rimuovi " + p.getName();
                }
            };

            historyManager.addMemento(hm);
        }
    }


    /**
     * Istanzia una nuova {@link Transition}
     * @param position la posizione della {@link Transition} da creare.
     * @return la {@link Transition} creata.
     */
    public Transition createTransitionAt(Point position)
    {
        Transition transition = new Transition(new String("transition" + transitionCounter), position);
        transitionCounter++;
        transition.addSatellite(new LabelNameNodeSatellite(transition));

        addTransitionToPetriNet(transition, historyManager);

        setModified(true);
        updateAllViews(null);

        return transition;
    }

    private void addTransitionToPetriNet(final Transition t, IHistoryComposite historyManager)
    {
        petriNet.addTransition(t);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    removeTransitionFromPetriNet(t, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    addTransitionToPetriNet(t, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "aggiungi " + t.getName();
                }
            };

            historyManager.addMemento(hm);
        }
    }

    private void removeTransitionFromPetriNet(final Transition t, IHistoryComposite historyManager)
    {
        selectionModel.deselect(t);

        // per prima cosa rimuovo tutti i link collegati...
        Set<Link> links = new LinkedHashSet<Link>(getLinks());

        for(Link link : links)
        {
            if(link.getTransition() == t)
            {
                removeLinkFromPetriNet(link, historyManager);
            }
        }

        petriNet.removeTransition(t);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    addTransitionToPetriNet(t, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    removeTransitionFromPetriNet(t, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "rimuovi " + t.getName();
                }
            };

            historyManager.addMemento(hm);
        }

    }


    /**
     * Istanzia un nuovo {@link Link},
     * @param place la {@link Place} del Link.
     * @param transition la {@link Transition} del Link
     * @param type il {@link LinkTypeEnum} del Link
     * @return il {@link Link} creato.
     */
    public Link createLink(Place place, Transition transition, Link.LinkTypeEnum type)
    {
        Link link = new Link(place, transition, type);

        addLinkToPetriNet(link, historyManager);

        setModified(true);
        updateAllViews(null);

        return link;
    }

    private void addLinkToPetriNet(final Link l, IHistoryComposite historyManager)
    {
        l.reLink();
        petriNet.addLink(l);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    removeLinkFromPetriNet(l, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    addLinkToPetriNet(l, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "aggiungi " + l.getName();
                }
            };

            historyManager.addMemento(hm);

        }
    }


    private void removeLinkFromPetriNet(final Link l, IHistoryComposite historyManager)
    {
        selectionModel.deselect(l);

        l.unLink();
        petriNet.removeLink(l);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    addLinkToPetriNet(l, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    removeLinkFromPetriNet(l, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "rimuovi " + l.getName();
                }
            };

            historyManager.addMemento(hm);
        }

    }

    /**
     * Istanzia un nuovo {@link JointNode}
     * @param position la posizione del {@link JointNode} da creare.
     * @param arcToRemove l'{@link Arc} che sarà sostituito dal {@link JointNode} creato (e da due nuovi {@link Arc}).
     * @return il {@link JointNode} creato.
     */
    public JointNode createJointNodeAt(Point position, Arc arcToRemove)
    {
        JointNode jn = arcToRemove.getLink().createJointNodeAt(position);

        addJointNodeToLink(jn, arcToRemove, historyManager);

        setModified(true);
        updateAllViews(null);

        return jn;
    }

    private void addJointNodeToLink(final JointNode jointnode, Arc arcToRemove, IHistoryComposite historyManager)
    {
        jointnode.getLink().addJointNodeReplacingArc(jointnode, arcToRemove);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    removeJointNode(jointnode, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    Arc arcToRemove = jointnode.getLink().getArcBetween(jointnode.getBackArc().getBackNode(), jointnode.getFwdArc().getFwdNode());
                    addJointNodeToLink(jointnode, arcToRemove, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "aggiungi " + jointnode.getName();
                }
            };

            historyManager.addMemento(hm);
        }

        updateAllViews(null);

    }


    private void removeJointNode(final JointNode jointnode, IHistoryComposite historyManager)
    {
        selectionModel.deselect(jointnode);

        jointnode.getLink().deleteJointNode(jointnode);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    Arc arcToRemove = jointnode.getLink().getArcBetween(jointnode.getBackArc().getBackNode(), jointnode.getFwdArc().getFwdNode());
                    addJointNodeToLink(jointnode, arcToRemove, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    removeJointNode(jointnode, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "rimuovi " + jointnode.getName();
                }
            };

            historyManager.addMemento(hm);
        }

        updateAllViews(null);

    }

    /**
     * Modifica le posizioni di tutti i {@link Node} contenuti nel {@link Map#keySet()} di <code>oldPositionsMap</code>.
     * @param oldPositionsMap mappa le cui chiavi sono i {@link Node} da muovere. Il valore di una chiave è la posizione prima dello spostamento.
     * @param offsetX valore da sommare alla coordinata x di ciascuna posizione
     * @param offsetY valore da sommare alla coordinata y di ciascuna posizione
     * @param grid {@link IGrid} da utilizzare per effettuare le modifiche alle posizioni
     */
    public void move(Map<Node, Point> oldPositionsMap, int offsetX, int offsetY, IGrid grid)
    {
        IHistoryComposite hc = new HistoryComposite("sposta");

        for(Node node : oldPositionsMap.keySet())
        {
            setNodePosition(node, oldPositionsMap.get(node), grid.getPoint(new Point(node.getPosition().x + offsetX, node.getPosition().y + offsetY)), hc);
        }

        historyManager.addMemento(hc);

        setModified(true);
        updateAllViews(null);
    }

    private void setNodePosition(final Node node, final Point oldPosition, final Point newPosition, IHistoryComposite historyManager)
    {
        node.setPosition(newPosition); // questo lo devo fare in ogni caso!

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    setNodePosition(node, newPosition, oldPosition, null);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    setNodePosition(node, oldPosition, newPosition, null);
                }

                @Override
                public String getHistoryName()
                {
                    return "sposta " + node.getName();
                }
            };

            historyManager.addMemento(hm);
        }

    }

    /**
     *
     * @return tutti i {@link Node} presenti in questo {@link PNeditorDocument}
     */
    public Set<Node> getNodes()
    {
        return petriNet.getNodes();
    }

    /**
     *
     * @return tutti i {@link Link} presenti in questo {@link PNeditorDocument}
     */
    public Set<Link> getLinks()
    {
        return petriNet.getLinks();
    }

    /**
     *
     * @return tutti i {@link Node} e tutti i {@link Link} presenti in questo {@link PNeditorDocument}
     */
    public Set<PNelement> getNodesAndLinks()
    {
        return petriNet.getNodesAndLinks();
    }

    /**
     *
     * @return tutti i {@link Node} e tutti gli {@link Arc} presenti in questo {@link PNeditorDocument}
     */
    public Set<PNelement> getNodesAndArcs()
    {
        return petriNet.getNodesAndArcs();
    }

    /**
     *
     * @return la {@link IListSelectionModel} utilizzata da questo {@link PNeditorDocument}
     */
    public IListSelectionModel getSelectionModel()
    {
        return selectionModel;
    }

    public String getNetInfo()
    {
        return petriNet.getInfo();
    }

    @Override
    public boolean canUndo()
    {
        return historyManager.canUndo();
    }

    @Override
    public boolean canRedo()
    {
        return historyManager.canRedo();
    }

    @Override
    public void undo() throws HistoryException
    {
        historyManager.undo(null);
        updateAllViews(null);
    }

    @Override
    public void redo() throws HistoryException
    {
        historyManager.redo(null);
        updateAllViews(null);
    }

    @Override
    public String getNextUndoActionName()
    {
        return historyManager.getNextUndoMementoName();
    }

    @Override
    public String getNextRedoActionName()
    {
        return historyManager.getNextRedoMementoName();
    }

    /**
     * Esporta gli elementi selezionati su un file immagine, tramite la libreria
     * <a href="http://java.freehep.org/vectorgraphics/">freeHEP VectorGraphics</a>.
     */
    public void exportSelection()
    {
        PNeditorView view = null;

        for(IView iview : getViews())
        {
            if(iview instanceof PNeditorView && ((PNeditorView) iview).isActive())
            {
                view = (PNeditorView) iview;
            }
        }

        if(view != null)
        {
            JPanel target = view.getJPanelContaining(getElementsToExport());

            // let FreeHep VectorGraphics do the magic!!!
            ExportDialog export = new ExportDialog();
            export.showExportDialog(view, "Export selection as image with FreeHEP VectorGraphics", target, "PetriNet");
        }
    }

    private Set<PNelement> getElementsToExport()
    {
        // voglio copiare gli elementi selezionati, con un accorgimento...
        LinkedHashSet<PNelement> elementsToExport = new LinkedHashSet<PNelement>(selectionModel.getSelectedItems());

        // ...per ogni link selezionato vorrò copiare in realtà gli archi e i jn (che in realtà attualmente non si disegnano) e NON il link!
        for(PNelement elem : selectionModel.getSelectedItems())
        {
            if(elem instanceof Link)
            {
                elementsToExport.addAll(((Link) elem).getArcs());
                elementsToExport.addAll(((Link) elem).getJointNodes());
                elementsToExport.remove(elem);
            }
        }

        return elementsToExport;
    }


    @Override
    public boolean canCut()
    {
        return canCopy();
    }

    @Override
    public boolean canCopy()
    {
        return !(selectionModel.isSelectionEmpty());
    }

    @Override
    public boolean canPaste()
    {
        return clipboard.isDataFlavorAvailable(dataFlavor);
    }

    @Override
    public void cut()
    {
        // cut = prima copia, poi cancella gli elementi selezionati, con un accorgimento...
        copy();

        // ...NON devo rimuovere eventuali jointnodes, altrimenti quando incollo il link, quello nuovo sarà sempre semplice!!!
        Set<PNelement> toDelete = new LinkedHashSet<PNelement>(selectionModel.getSelectedItems());

        for(PNelement jn : selectionModel.getSelectedItems())
        {
            if(jn instanceof JointNode)
            {
                toDelete.remove(jn);
            }
        }

        selectionModel.clearSelection();
        deleteElements(toDelete);
    }

    @Override
    public void copy()
    {
        //ottieni l'insieme di elementi da copiare all'interno dell'editor e mettilo nella clipboard locale
        DataHandler dh = new DataHandler(getElementsToCopy(), dataFlavor.getMimeType());
        clipboard.setContents(dh, this);

        // e poi, con jVectClipboard, metti l'immagine nella clipboard di sistema...
        PNeditorView view = null;

        for(IView iview : getViews())
        {
            if(iview instanceof PNeditorView)
            {
                view = (PNeditorView) iview;
            }
        }

        // copia la rete come immagine vettoriale e mettila nella clipboard di sistema
        if(view != null)
        {
            Set<PNelement> elementsToCopyAsVectorGraphics = getElementsToCopyAsVectorGraphic();

            Rectangle minCoveringRect = view.getSmallestRectangleContaining(elementsToCopyAsVectorGraphics);

            SimpleClipGraphics clipGraphics = new SimpleClipGraphics(minCoveringRect.width, minCoveringRect.height);
            clipGraphics.setPictureFormat(PictureFormat.EMF);
            // il PictureFormat di default è MACPICT. Su OpenOffice i risultati con EMF sono identici (pessimi).
            // Con Word EMF funziona alla grande e quindi si lascia EMF
            Graphics2D graphics = clipGraphics.getGraphics();

            for(PNelement elem : elementsToCopyAsVectorGraphics)
            {
                try
                {
                    view.getGraphicElementOf(elem).drawYourselfOn(graphics, minCoveringRect);
                }
                catch(PNeditorGenericException e)
                {
                    e.printStackTrace();
                }

                for(GraphicSatellite gsat : view.getGraphicSatellitesOf(elem))
                {
                    gsat.drawYourselfOn(graphics, minCoveringRect);
                }
            }

            graphics.dispose();
            clipGraphics.copyToClipboard();
        }
    }

    private Set<PNelement> getElementsToCopy()
    {
        // voglio copiare gli elementi selezionati, con un accorgimento...
        LinkedHashSet<PNelement> elementsToCopy = new LinkedHashSet<PNelement>(selectionModel.getSelectedItems());

        // ...per ogni link selezionato dovrò copiare anche place e transition!
        for(PNelement elem : selectionModel.getSelectedItems())
        {
            if(elem instanceof Link)
            {
                elementsToCopy.add(((Link) elem).getPlace());
                elementsToCopy.add(((Link) elem).getTransition());
            }
        }

        return elementsToCopy;
    }

    private Set<PNelement> getElementsToCopyAsVectorGraphic()
    {
        // voglio copiare gli elementi selezionati, con un accorgimento...
        LinkedHashSet<PNelement> elementsToCopy = new LinkedHashSet<PNelement>(selectionModel.getSelectedItems());

        // ...per ogni link selezionato vorrò copiare in realtà gli archi e i jn (che in realtà attualmente non si disegnano) e NON il link!
        for(PNelement elem : selectionModel.getSelectedItems())
        {
            if(elem instanceof Link)
            {
                elementsToCopy.addAll(((Link) elem).getArcs());
                elementsToCopy.addAll(((Link) elem).getJointNodes());
                elementsToCopy.remove(elem);
            }
        }

        return elementsToCopy;
    }


    @Override
    public void paste()
    {
        pasteImpl(historyManager);
    }

    @SuppressWarnings("unchecked")
    private void pasteImpl(IHistoryComposite historyManager)
    {
        // voglio creare una NUOVA ISTANZA per ogni oggetto copiato
        // i nodi verranno creati un po' più in basso e un po' più a destra dell'originale
        int xOffset = 50;
        int yOffset = xOffset;

        // mi serve una mappa "vecchio nodo (key) - nuovo nodo (value)" per creare i nuovi link
        Map<Node, Node> nodesMap = new HashMap<Node, Node>();

        // voglio anche l'insieme degli elementi che creo per poterli selezionare alla fine dell'operazione
        final Set<PNelement> newElements = new LinkedHashSet<PNelement>();

        try
        {
            for(PNelement oldPlace : (Set<PNelement>)clipboard.getData(dataFlavor))   // prima le places...
            {
                if(oldPlace instanceof Place)
                {
                    Point position = new Point(oldPlace.getPosition());
                    position.translate(xOffset, yOffset);
                    Place newPlace = createPlaceAt(position);
                    nodesMap.put((Node) oldPlace, newPlace);
                    newElements.add(newPlace);
                }
            }

            for(PNelement oldTransition : (Set<PNelement>)clipboard.getData(dataFlavor))   // poi le transitions...
            {
                if(oldTransition instanceof Transition)
                {
                    Point position = new Point(oldTransition.getPosition());
                    position.translate(xOffset, yOffset);
                    Transition newTransition = createTransitionAt(position);
                    newTransition.setVertical(((Transition) oldTransition).isVertical());
                    nodesMap.put((Node) oldTransition, newTransition);
                    newElements.add(newTransition);
                }
            }

            for(PNelement oldLink : (Set<PNelement>)clipboard.getData(dataFlavor))
            {
                if(oldLink instanceof Link)   // ... e infine i link!
                {
                    Place oldPlace = ((Link) oldLink).getPlace();
                    Transition oldTransition = ((Link) oldLink).getTransition();
                    Place newPlace = (Place) nodesMap.get(oldPlace);
                    Transition newTransition = (Transition) nodesMap.get(oldTransition);
                    Link newLink = createLink(newPlace, newTransition, ((Link) oldLink).getType());
                    newElements.add(newLink);

                    // i link composti richiedono un po' di lavoro extra...
                    Set<JointNode> oldJointNodes = new LinkedHashSet<JointNode>(((Link) oldLink).getJointNodes());
                    Set<Arc> oldArcs = new LinkedHashSet<Arc>(((Link) oldLink).getArcs());

                    newLink.deleteArc(newLink.getArcBetween(newPlace, newTransition)); // rimuovo l'unico arco che c'era

                    for(JointNode oldJointNode : oldJointNodes)
                    {
                        Point position = new Point(oldJointNode.getPosition());
                        position.translate(xOffset, yOffset);
                        JointNode newJointNode = newLink.createJointNodeAt(position);
                        nodesMap.put(oldJointNode, newJointNode);
                        newElements.add(newJointNode);
                    }

                    for(Arc oldArc : oldArcs)
                    {
                        Node newBackNode = nodesMap.get(oldArc.getBackNode());
                        Node newFwdNode = nodesMap.get(oldArc.getFwdNode());
                        newLink.createArc(newBackNode, newFwdNode, oldArc.getType());
                    }
                }
            }

        }
        catch(UnsupportedFlavorException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        // voglio che gli elementi appena incollati risultino selezionati (verosimilmente l'utente vorrà spostarli!)
        selectionModel.clearSelection();
        selectionModel.select(newElements);

        if(historyManager != null)
        {
            IHistoryMemento hm = new IHistoryMemento()
            {

                @Override
                public void undo(Object context) throws HistoryException
                {
                    deleteElements(newElements);
                }

                @Override
                public void redo(Object context) throws HistoryException
                {
                    pasteImpl(null);
                }

                @Override
                public String getHistoryName()
                {
                    return "incolla";
                }
            };

            historyManager.addMemento(hm);
        }

        updateAllViews(null);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents)
    {
        //System.out.println(getDocName()+" ha perso il possesso della clipboard");
    }

}
