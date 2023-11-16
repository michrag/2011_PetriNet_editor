package pNeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import pNeditor.exceptions.GraphicElementNotFound;
import pNeditor.exceptions.PNeditorGenericException;
import pNeditor.exceptions.PNelementNotFound;
import petriNetDomain.Arc;
import petriNetDomain.JointNode;
import petriNetDomain.LabelNameArcSatellite;
import petriNetDomain.LabelNameNodeSatellite;
import petriNetDomain.LabelTextSatellite;
import petriNetDomain.Node;
import petriNetDomain.PNelement;
import petriNetDomain.Place;
import petriNetDomain.Satellite;
import petriNetDomain.SelectionSatellite;
import petriNetDomain.Transition;
import framework.FDocument;
import framework.ui.FPanelView;
import graphicDomain.GraphicSatellite;
import graphicDomain.GraphicSelectionRectSatellite;
import graphicDomain.PNgraphicElement;

/**
 * {@link FPanelView} dell'editor. La vista su cui avviene il disegno degli elementi.
 *
 */
public class PNeditorView extends FPanelView implements IListSelectionListener
{
    private IControllerFactory installedControllerFactory;
    private IGrid grid;

    private int zoomInCounter;
    private int zoomOutCounter;

    public PNeditorView()
    {
        setLayout(null);
        installedControllerFactory = null;
        grid = new GridOff();
        setBackground(Color.white);
    }

    @Override
    public void initializeView(JComponent parent, FDocument newDoc)
    {
        super.initializeView(parent, newDoc);

        getDocument().getSelectionModel().addListSelectionListener(this);
        getDocument().initClipboard();

        zoomReset();
    }

    /**
     *
     * @param controllerFactory {@link IControllerFactory} da installare su questa {@link PNeditorView}
     */
    public void install(IControllerFactory controllerFactory)
    {
        if(installedControllerFactory != null)
        {
            installedControllerFactory.uninstallFrom(this);
        }

        controllerFactory.installOn(this);

        installedControllerFactory = controllerFactory;
    }

    /**
     *
     * @param controllerFactory {@link IControllerFactory} di cui si vuole sapere se è attivo su questa {@link PNeditorView}.
     * @return <code>true</code> se e solo se <code>controllerFactory</code> è attualmente installato su questa {@link PNeditorView}.
     */
    public boolean isControllerFactoryInstalled(IControllerFactory controllerFactory)
    {
        return controllerFactory == installedControllerFactory;
    }


    /**
     *
     * @param listenerClass tipo di {@link MouseListener} da rimuovere da questa {@link PNeditorView}
     */
    public void removeMouseListeners(Class <? extends MouseListener > listenerClass)
    {
        for(MouseListener l : getMouseListeners())
            if(listenerClass.isInstance(l))
            {
                removeMouseListener(l);
            }
    }

    /**
     *
     * @param motionListenerClass tipo di {@link MouseMotionListener} da rimuovere da questa {@link PNeditorView}
     */
    public void removeMouseMotionListeners(Class <? extends MouseMotionListener > motionListenerClass)
    {
        for(MouseMotionListener l : getMouseMotionListeners())
            if(motionListenerClass.isInstance(l))
            {
                removeMouseMotionListener(l);
            }
    }

    /**
     *
     * @param wheelListenerClass tipo di {@link MouseWheelListener} da rimuovere da questa {@link PNeditorView}
     */
    public void removeMouseWheelListeners(Class <? extends MouseWheelListener > wheelListenerClass)
    {
        for(MouseWheelListener l : getMouseWheelListeners())
            if(wheelListenerClass.isInstance(l))
            {
                removeMouseWheelListener(l);
            }
    }

    /**
     *
     * @param keyListenerClass tipo di {@link KeyListener} da rimuovere da questa {@link PNeditorView}
     */
    public void removeKeyListeners(Class <? extends KeyListener > keyListenerClass)
    {
        for(KeyListener l : getKeyListeners())
            if(keyListenerClass.isInstance(l))
            {
                removeKeyListener(l);
            }
    }

    /**
     *
     * @param listenerClass tipo di {@link MouseListener} da rimuovere da tutti i {@link Component} appartenti a questa {@link PNeditorView}.
     */
    public void removeMouseListenersFromComponents(Class <? extends MouseListener > listenerClass)
    {
        for(Component comp : getComponents())
        {
            for(MouseListener l : comp.getMouseListeners())
                if(listenerClass.isInstance(l))
                {
                    comp.removeMouseListener(l);
                }
        }
    }

    /**
     *
     * @param motionListenerClass tipo di {@link MouseMotionListener} da rimuovere da tutti i {@link Component} appartenti a questa {@link PNeditorView}.
     */
    public void removeMouseMotionListenersFromComponents(Class <? extends MouseMotionListener > motionListenerClass)
    {
        for(Component comp : getComponents())
        {
            for(MouseMotionListener l : comp.getMouseMotionListeners())
                if(motionListenerClass.isInstance(l))
                {
                    comp.removeMouseMotionListener(l);
                }
        }
    }

    /**
     *
     * @param wheelListenerClass tipo di {@link MouseWheelListener} da rimuovere da tutti i {@link Component} appartenti a questa {@link PNeditorView}.
     */
    public void removeMouseWheelListenersFromComponents(Class <? extends MouseWheelListener > wheelListenerClass)
    {
        for(Component comp : getComponents())
        {
            for(MouseWheelListener l : comp.getMouseWheelListeners())
                if(wheelListenerClass.isInstance(l))
                {
                    comp.removeMouseWheelListener(l);
                }
        }
    }

    /**
     *
     * @param keyListenerClass tipo di {@link KeyListener} da rimuovere da tutti i {@link Component} appartenti a questa {@link PNeditorView}.
     */
    public void removeKeylListenersFromComponents(Class <? extends KeyListener > keyListenerClass)
    {
        for(Component comp : getComponents())
        {
            for(KeyListener l : comp.getKeyListeners())
                if(keyListenerClass.isInstance(l))
                {
                    comp.removeKeyListener(l);
                }
        }
    }

    /**
     *
     * @return la {@link IGrid} attualmente attiva su questa {@link PNeditorView}.
     */
    public IGrid getGrid()
    {
        return grid;
    }

    /**
     * Se la {@link IGrid} attiva era una {@link GridOff} la sostituisce con una {@link GridOn} e viceversa.
     */
    public void toggleGrid()
    {
        if(grid instanceof GridOff)
        {
            grid = new GridOn();
        }
        else
        {
            grid = new GridOff();
        }
    }

    /**
     *
     * @return <code>true</code> se e solo se la {@link IGrid} attiva è un'istanza di {@link GridOn}.
     */
    public boolean isGridOn()
    {
        return(grid instanceof GridOn);
    }

    @Override
    public PNeditorDocument getDocument()
    {
        return (PNeditorDocument) super.getDocument();
    }

    @Override
    public void updateView(Object caller)
    {
        removeAll();

        for(PNelement elem : getDocument().getNodesAndArcs())
        {
            PNgraphicElement gelem = VisitorCreateGraphicElement.getGraphicElement(elem);
            add(gelem);

            for(Satellite sat : elem.getSatellites())
            {
                PNgraphicElement gsat = VisitorCreateGraphicElement.getGraphicElement(sat);
                add(gsat);
            }
        }

        updatePreferredSize();

        revalidate();
        repaint();

        // necessario quando updateview è chiamato da un controllerfactory che rimane attivo... deve reinstallarsi!
        if(installedControllerFactory != null)
        {
            installedControllerFactory.uninstallFrom(this);
            installedControllerFactory.installOn(this);
        }

        valueChanged(new ListSelectionEvent(new Object())); // per creare i rettangoli di selezione corretti.
    }

    private void updatePreferredSize()
    {
        Rectangle rect = getSmallestRectangleContaining(getDocument().getNodesAndArcs());
        setPreferredSize(new Dimension(rect.x + rect.width, rect.y + rect.height));
    }

    /**
     *
     * @param pnElement {@link PNelement} di cui si vuole ottenere il corrispondente {@link PNgraphicElement}
     * @return il {@link PNgraphicElement} corrispondente a <code>pnElement</code>
     * @throws PNelementNotFound se il {@link PNelement} non è presente nel {@link PNeditorDocument} proprietario di questa {@link PNeditorView}.
     * @throws GraphicElementNotFound se questa {@link PNeditorView} non possiede il {@link PNgraphicElement} corrispondente a <code>pnElement</code>
     */
    public PNgraphicElement getGraphicElementOf(PNelement pnElement) throws PNelementNotFound, GraphicElementNotFound
    {
        if(!(getDocument().getNodesAndArcs().contains(pnElement)))
        {
            throw new PNelementNotFound(pnElement);
        }

        for(Component comp : getComponents())
        {
            if(comp instanceof PNgraphicElement)
            {
                if(((PNgraphicElement) comp).getPNelement() == pnElement)
                {
                    return (PNgraphicElement) comp;
                }
            }
        }

        throw new GraphicElementNotFound(pnElement);

    }

    /**
     *
     * @param pnElement il {@link PNelement} di cui si vogliono ottenere tutti i {@link GraphicSatellite}.
     * @return {@link List} di tutti i {@link GraphicSatellite} corrispondenti ai {@link Satellite} di <code>pnElement</code>
     */
    public List<GraphicSatellite> getGraphicSatellitesOf(PNelement pnElement)
    {
        List<GraphicSatellite> gsatellites = new ArrayList<GraphicSatellite>();

        for(Component gsat : getComponents())
        {
            if(gsat instanceof GraphicSatellite)
            {
                if(((Satellite)((GraphicSatellite) gsat).getPNelement()).getPlanet() == pnElement)
                {
                    gsatellites.add((GraphicSatellite) gsat);
                }
            }
        }

        return gsatellites;
    }

    /**
     *
     * @param nodesAndArcs il {@link Set} di {@link PNelement} di cui vogliamo il più piccolo rettangolo che lo contiene interamente.
     * @return il {@link Rectangle} con area minore tra tutti i rettangoli che contengono completamente tutti i {@link PNelement}
     * (e relativi satelliti) di <code>nodesAndArcs</code>.
     */
    public Rectangle getSmallestRectangleContaining(Set<PNelement> nodesAndArcs)
    {
        Rectangle minCoveringRect;

        if(nodesAndArcs.size() > 0)
        {
            minCoveringRect = new Rectangle(((PNelement) nodesAndArcs.toArray()[0]).getRect());
        }
        else
        {
            return new Rectangle();
        }

        for(PNelement elem : nodesAndArcs)
        {
            minCoveringRect = minCoveringRect.union(elem.getRect());

            for(Satellite sat : elem.getSatellites())
            {
                minCoveringRect = minCoveringRect.union(sat.getRect());
            }
        }

        return minCoveringRect;
    }

    /**
     *
     * @param nodesAndArcs il {@link Set} di {@link PNelement} di cui vogliamo il più piccolo {@link JPanel} che lo contiene interamente.
     * @return il più piccolo (si veda {@link #getSmallestRectangleContaining(Set)}) {@link JPanel} che contiene interamente tutti i {@link PNgraphicElement}
     * corrispondenti ai {@link PNelement} (e ai relativi satelliti) di <code>nodesAndArcs</code>.
     */
    public JPanel getJPanelContaining(Set<PNelement> nodesAndArcs)
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(null); // è fondamentale!
        jpanel.setBackground(Color.white);

        if(nodesAndArcs.isEmpty())
        {
            return jpanel;
        }
        else
        {
            Rectangle minCoveringRect = getSmallestRectangleContaining(nodesAndArcs);
            jpanel.setBounds(minCoveringRect);

            for(PNelement elem : nodesAndArcs)
            {
                try
                {
                    PNgraphicElement comp = getGraphicElementOf(elem);
                    Point compLocation = new Point(comp.getLocation());
                    compLocation.translate(-minCoveringRect.x, -minCoveringRect.y);
                    comp.setLocation(compLocation);
                    jpanel.add(comp);

                }
                catch(PNeditorGenericException e)
                {
                    e.printStackTrace();
                }

                for(GraphicSatellite gsat : getGraphicSatellitesOf(elem))
                {
                    if(!(gsat instanceof GraphicSelectionRectSatellite)) // verosimilmente non si vorrano copiare i rettangoli di selezione
                    {
                        Point gsatLocation = new Point(gsat.getLocation());
                        gsatLocation.translate(-minCoveringRect.x, -minCoveringRect.y);
                        gsat.setLocation(gsatLocation);
                        jpanel.add(gsat);
                    }
                }
            }
        }

        jpanel.revalidate();
        jpanel.repaint();

        updateView(null);
        return jpanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        // elimino tutte le decorazioni...
        for(PNelement pnElement : getDocument().getNodesAndArcs())
        {
            // sia grafiche che...
            for(GraphicSatellite gsat : getGraphicSatellitesOf(pnElement))
                if(gsat instanceof GraphicSelectionRectSatellite)
                {
                    remove(gsat);
                }

            // ... "reali"
            Set<Satellite> ssats = new HashSet<Satellite>(pnElement.getSatellites()); // per evitare ConcurrentModificationException

            for(Satellite sat : ssats)
                if(sat instanceof SelectionSatellite)
                {
                    pnElement.removeSatellite(sat);
                }
        }

        // creo tutte le decorazioni necessarie...
        for(PNelement pnElement : getDocument().getSelectionModel().getSelectedItems())
        {
            for(SelectionSatellite selsat : pnElement.getProperSelectionSatellites())
            {
                pnElement.addSatellite(selsat);
                PNgraphicElement s = VisitorCreateGraphicElement.getGraphicElement(selsat);
                add((Component) s);
            }
        }

        revalidate();
        repaint();

    }

    /**
     * Effettua lo zoom in di questa {@link PNeditorView}.
     */
    public void zoomIn()
    {
        zoomInCounter++;
        Rectangle oldMinCoveringRect = getSmallestRectangleContaining(getDocument().getNodesAndArcs());

        float scaleFactor = Options.getInstance().getZoomScaleFactor();

        // PLACE
        Place.zoomIn(scaleFactor);
        // TRANSITION
        Transition.zoomIn(scaleFactor);
        // JOINTNODE
        JointNode.zoomIn(scaleFactor);
        // ARC
        Arc.zoomIn(scaleFactor);
        // LABELSAT
        LabelTextSatellite.zoomIn(scaleFactor);
        LabelNameNodeSatellite.zoomIn(scaleFactor);
        LabelNameArcSatellite.zoomIn(scaleFactor);

        for(Node node : getDocument().getNodes())
        {
            Point newPosition = new Point(node.getPosition());
            newPosition.x += Math.abs(Math.round(newPosition.x / scaleFactor));
            newPosition.y += Math.abs(Math.round(newPosition.y / scaleFactor));
            node.setPosition(grid.getPoint(newPosition));
        }

        for(PNelement elem : getDocument().getNodesAndArcs())
        {
            elem.notifyAllSatellitesNewPosition();
        }

        recenterAfterZoom(oldMinCoveringRect, true);
    }

    /**
     * Effettua lo zoom out di questa {@link PNeditorView}.
     */
    public void zoomOut()
    {
        zoomOutCounter++;
        Rectangle oldMinCoveringRect = getSmallestRectangleContaining(getDocument().getNodesAndArcs());

        float scaleFactor = Options.getInstance().getZoomScaleFactor();

        // PLACE
        Place.zoomOut(scaleFactor);
        // TRANSITION
        Transition.zoomOut(scaleFactor);
        // JOINTNODE
        JointNode.zoomOut(scaleFactor);
        // ARC
        Arc.zoomOut(scaleFactor);
        // LABELSAT
        LabelTextSatellite.zoomOut(scaleFactor);
        LabelNameNodeSatellite.zoomOut(scaleFactor);
        LabelNameArcSatellite.zoomOut(scaleFactor);

        for(Node node : getDocument().getNodes())
        {
            Point newPosition = new Point(node.getPosition());
            newPosition.x -= Math.abs(Math.round(newPosition.x / scaleFactor));
            newPosition.y -= Math.abs(Math.round(newPosition.y / scaleFactor));
            node.setPosition(grid.getPoint(newPosition));
        }

        for(PNelement elem : getDocument().getNodesAndArcs())
        {
            elem.notifyAllSatellitesNewPosition();
        }

        recenterAfterZoom(oldMinCoveringRect, false);
    }

    /**
     * Resetta lo zoom di questa {@link PNeditorView}.
     */
    public void zoomReset()
    {
        int zoomInCounter = this.zoomInCounter;
        int zoomOutCounter = this.zoomOutCounter;

        if(zoomInCounter > zoomOutCounter)
        {
            for(int i = 0; i < (zoomInCounter - zoomOutCounter); i++)
            {
                zoomOut();
            }
        }

        if(zoomInCounter < zoomOutCounter)
        {
            for(int i = 0; i < (zoomOutCounter - zoomInCounter); i++)
            {
                zoomIn();
            }
        }

        zoomInCounter = 0;
        zoomOutCounter = 0;

        // PLACE
        Place.zoomReset();
        // TRANSITION
        Transition.zoomReset();
        // JOINTNODE
        JointNode.zoomReset();
        // ARC
        Arc.zoomReset();
        // LABELSAT
        LabelTextSatellite.zoomReset();
        LabelNameNodeSatellite.zoomReset();
        LabelNameArcSatellite.zoomReset();

        for(PNelement elem : getDocument().getNodesAndArcs())
        {
            elem.notifyAllSatellitesNewPosition();
        }

        updateView(null);
    }

    private void recenterAfterZoom(Rectangle oldMinCoveringRect, boolean zoomedIn)
    {
        Rectangle newMinCoveringRect = getSmallestRectangleContaining(getDocument().getNodesAndArcs());

        int xOffset = Math.abs(newMinCoveringRect.x - oldMinCoveringRect.x) + Math.abs(newMinCoveringRect.width - oldMinCoveringRect.width) / 2;
        int yOffset = Math.abs(newMinCoveringRect.y - oldMinCoveringRect.y) + Math.abs(newMinCoveringRect.height - oldMinCoveringRect.height) / 2;

        for(Node node : getDocument().getNodes())
        {
            Point newPosition = new Point(node.getPosition());

            if(zoomedIn)
            {
                newPosition.translate(-xOffset, -yOffset);
            }
            else
            {
                newPosition.translate(xOffset, yOffset);
            }

            node.setPosition(grid.getPoint(newPosition));
        }

        for(PNelement elem : getDocument().getNodesAndArcs())
        {
            elem.notifyAllSatellitesNewPosition();
        }

        updateView(null);
    }

}
