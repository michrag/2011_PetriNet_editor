package pNeditor.defaultControllerFactories;

import graphicDomain.GraphicArc;
import graphicDomain.GraphicNode;
import graphicDomain.GraphicSatellite;
import graphicDomain.GraphicTransition;
import graphicDomain.MultipleSelectionRectangle;
import graphicDomain.PNgraphicElement;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import pNeditor.IControllerFactory;
import pNeditor.PNeditorDocument;
import pNeditor.PNeditorView;
import pNeditor.exceptions.PNeditorGenericException;
import petriNetDomain.Arc;
import petriNetDomain.JointNode;
import petriNetDomain.Link;
import petriNetDomain.Node;
import petriNetDomain.PNelement;
import petriNetDomain.Satellite;
import petriNetDomain.Transition;

/**
 * {@link IControllerFactory} corrispondente alla modalità tramite la quale l'utente può selezionare e spostare gli elementi.
 *
 */
public class SelectControllerFactory implements IControllerFactory
{
    /**
     * {@link KeyListener} specifico per una {@link PNeditorView}.
     *
     */
    private class ViewKeyboardListener implements KeyListener
    {
        private PNeditorView view;

        public ViewKeyboardListener(PNeditorView view)
        {
            super();
            this.view = view;
            view.requestFocusInWindow(); // fondamentale!
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e)
        {
            PNeditorDocument doc = view.getDocument();

            if(e.getKeyCode() == KeyEvent.VK_DELETE)
            {
                view.getDocument().deleteElements(doc.getSelectionModel().getSelectedItems());
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {}

    }

    /**
     * {@link MouseAdapter} specifico per una {@link PNeditorView}.
     *
     */
    private class ViewMouseAdapter extends MouseAdapter
    {
        private PNeditorView view;

        public ViewMouseAdapter(PNeditorView view)
        {
            super();
            this.view = view;
        }

        private NodeMouseAdapter nodeMA;

        public ViewMouseAdapter(PNeditorView view, NodeMouseAdapter nodeMA)
        {
            this(view);
            this.nodeMA = nodeMA;
        }

        private Point startPt;
        private boolean dragging = false;
        private MultipleSelectionRectangle rect;

        @Override
        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                PNeditorDocument doc = (PNeditorDocument) view.getDocument();

                if(!e.isControlDown())
                {
                    doc.getSelectionModel().clearSelection();
                }

                startPt = e.getPoint();
                rect = new MultipleSelectionRectangle();
                view.add(rect);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                dragging = true;

                if(startPt != null)   // se ci arriva tramite un arco alle volte è null...
                {
                    rect.setBounds(startPt, e.getPoint());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            PNeditorDocument doc = (PNeditorDocument) view.getDocument();

            if(SwingUtilities.isLeftMouseButton(e))
            {
                if(rect != null)   // se ci arriva tramite un arco alle volte è null...
                {
                    if(dragging)
                    {
                        for(PNelement elem : doc.getNodesAndArcs())
                        {
                            Point elemCenter = new Point(elem.getCenter());
                            elemCenter.translate(-rect.getX(), -rect.getY()); // perchè contains vuole le coord del punto nel suo SdR

                            if(rect.contains(elemCenter))
                            {
                                if(elem instanceof Arc)
                                {
                                    doc.getSelectionModel().select(((Arc) elem).getLink(), true);
                                }
                                else
                                {
                                    doc.getSelectionModel().select(elem, true);
                                }
                            }
                        }

                        dragging = false;
                        nodeMA.multipleSelection = true;

                    }

                    view.remove(rect); // qua per forza, se rect è null --> nullPointerException!!!
                }
            }

            view.revalidate();
            view.repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
            {
                PNeditorDocument doc = view.getDocument();
                doc.getSelectionModel().select(doc.getNodesAndLinks());
                nodeMA.multipleSelection = true;
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            if(e.getWheelRotation() == 1 && e.isControlDown())
            {
                view.zoomOut();
            }

            if(e.getWheelRotation() == -1 && e.isControlDown())
            {
                view.zoomIn();
            }
        }

    }

    /**
     * {@link MouseAdapter} specifico per i {@link GraphicArc}.
     *
     */
    private class ArcMouseAdapter extends MouseAdapter
    {
        private PNeditorView view;
        private PNeditorDocument doc;

        public ArcMouseAdapter(PNeditorView view)
        {
            super();
            this.view = view;
            doc = view.getDocument();
        }

        private boolean deselectAllowed = false;
        private Component overlappedComp; // c'è sicuramente un component coperto da un arc (almeno la vista)

        @Override
        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                GraphicArc garc = (GraphicArc) e.getComponent();
                overlappedComp = getOverlappedComponent(garc, (Container) view, e.getPoint());
                Arc arc = (Arc) garc.getPNelement();

                // quando clicco su un arco voglio selezionare il LINK, non l'arco: selezionare un singolo arco non ha senso.
                Link link = arc.getLink();

                if(garc.lineHit(e.getPoint()))
                {
                    if(doc.getSelectionModel().isSelected(link) && e.isControlDown())
                    {
                        deselectAllowed = true;
                    }

                    doc.getSelectionModel().select(link, e.isControlDown());
                }
                else
                {
                    overlappedComp.dispatchEvent(SwingUtilities.convertMouseEvent(garc, e, overlappedComp));
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                GraphicArc garc = (GraphicArc) e.getComponent();

                if(!(garc.lineHit(e.getPoint())))
                {
                    if(overlappedComp != null && overlappedComp != e.getComponent())
                    {
                        overlappedComp.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, overlappedComp));
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                GraphicArc garc = (GraphicArc) e.getComponent();

                if(!(garc.lineHit(e.getPoint())))
                {
                    if(overlappedComp != null && overlappedComp != e.getComponent())
                    {
                        overlappedComp = getOverlappedComponent(garc, (Container) view, e.getPoint());
                        overlappedComp.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, overlappedComp));
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                GraphicArc garc = (GraphicArc)e.getComponent();
                Arc arc = (Arc) garc.getPNelement();

                if(garc.lineHit(e.getPoint()))
                {
                    if(e.getClickCount() == 2)
                    {
                        Point upperLeftCorner = e.getPoint();
                        upperLeftCorner.translate(-JointNode.getDimension().width / 2 + garc.getX(), -JointNode.getDimension().height / 2 + garc.getY());
                        JointNode jn = doc.createJointNodeAt(upperLeftCorner, arc);
                        doc.getSelectionModel().clearSelection();
                        doc.getSelectionModel().select(jn, false);
                    }
                    else
                    {
                        if(deselectAllowed)
                        {
                            doc.getSelectionModel().deselect(arc.getLink());
                            deselectAllowed = false;
                        }
                    }
                }
                else
                {
                    overlappedComp = getOverlappedComponent(garc, (Container) view, e.getPoint());
                    overlappedComp.dispatchEvent(SwingUtilities.convertMouseEvent(garc, e, overlappedComp));
                }
            }

        }

        private Component getOverlappedComponent(Component overlappingComp, Container view, Point intersectionPoint)
        {
            int overlappingCompZorder = view.getComponentZOrder(overlappingComp);

            ArrayList<Component> overlappedComponents = new ArrayList<Component>();

            for(Component comp : view.getComponents())
            {
                Point tpoint = new Point(intersectionPoint);
                tpoint.translate(overlappingComp.getX() - comp.getX(), overlappingComp.getY() - comp.getY()); // converto le coord nel SdR di comp

                if(comp.contains(tpoint) && view.getComponentZOrder(comp) > overlappingCompZorder)   // se comp contiene il punto ed è sotto
                {
                    if(!(comp instanceof GraphicSatellite))    // TODO: rimuovere questa condizione quando i GraphicSatellite saranno cliccabili
                    {
                        overlappedComponents.add(comp);
                    }
                }
            }

            if(!(overlappedComponents.isEmpty()))
            {
                Component overlappedComp = overlappedComponents.get(0);

                for(Component comp : overlappedComponents)
                {
                    if(view.getComponentZOrder(comp) < view.getComponentZOrder(overlappedComp))   // tra tutti i component sotto, voglio ritornare quello più in alto
                    {
                        overlappedComp = comp;
                    }
                }

                return overlappedComp;
            }
            else
            {
                return view;
            }
        }

    }

    /**
     * {@link MouseAdapter} specifico per i {@link GraphicNode}.
     *
     */
    private class NodeMouseAdapter extends MouseAdapter
    {
        private PNeditorView view;
        private PNeditorDocument doc;

        private Map<Node, Point> oldNodesPositionsMap;

        public NodeMouseAdapter(PNeditorView view)
        {
            super();
            this.view = view;
            doc = view.getDocument();
            oldNodesPositionsMap = new HashMap<Node, Point>();
        }

        private Point startPt;
        private boolean dragging = false;
        private boolean deselectAllowed = false;
        private boolean multipleSelection = false;

        @Override
        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                PNelement item = ((GraphicNode) e.getComponent()).getPNelement();

                if(doc.getSelectionModel().isSelected(item) && (e.isControlDown() || multipleSelection))
                {
                    deselectAllowed = true;
                }

                doc.getSelectionModel().select(item, (e.isControlDown() || multipleSelection));
                startPt = e.getPoint();

                for(PNelement elem : doc.getSelectionModel().getSelectedItems())
                {
                    if(elem instanceof Node)
                    {
                        oldNodesPositionsMap.put((Node) elem, elem.getPosition());
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                Point newLocation;
                dragging = true;

                if(SwingUtilities.isLeftMouseButton(e))
                {
                    for(PNelement selectedItem : doc.getSelectionModel().getSelectedItems())
                    {
                        if(selectedItem instanceof Node)
                        {
                            try
                            {
                                Node node = (Node) selectedItem;
                                GraphicNode gNode = (GraphicNode) view.getGraphicElementOf(selectedItem);
                                newLocation = view.getGrid().getPoint(gNode.getX() + e.getX() - startPt.x, gNode.getY() + e.getY() - startPt.y);
                                gNode.setLocation(newLocation);

                                node.setPosition(newLocation); // fondamentale, tutto si basa sulla posizione reale dei nodi

                                // devo fare in modo che i satelliti grafici seguano il pianeta durante il dragging
                                for(GraphicSatellite gsat : view.getGraphicSatellitesOf(selectedItem))
                                {
                                    gsat.setBounds(((Satellite)gsat.getPNelement()).getBoundsWhileDragging(newLocation));
                                }

                                // devo aggiornare i bounds degli archi grafici e dei relativi satelliti
                                for(Arc fwdArc : node.getForwardArcs())
                                {
                                    GraphicArc gFwdArc = (GraphicArc) view.getGraphicElementOf(fwdArc);
                                    gFwdArc.setBounds(node, fwdArc.getFwdNode());

                                    for(GraphicSatellite gsat : view.getGraphicSatellitesOf(fwdArc))
                                    {
                                        gsat.setBounds(((Satellite)gsat.getPNelement()).getBoundsWhileDragging(gFwdArc.getLocation()));
                                    }
                                }

                                for(Arc backArc : node.getBackwardArcs())
                                {
                                    GraphicArc gBackArc = (GraphicArc) view.getGraphicElementOf(backArc);
                                    gBackArc.setBounds(backArc.getBackNode(), node);

                                    for(GraphicSatellite gsat : view.getGraphicSatellitesOf(backArc))
                                    {
                                        gsat.setBounds(((Satellite)gsat.getPNelement()).getBoundsWhileDragging(gBackArc.getLocation()));
                                    }
                                }
                            }
                            catch(PNeditorGenericException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                if(dragging)
                {
                    doc.move(oldNodesPositionsMap, e.getX() - startPt.x, e.getY() - startPt.y, view.getGrid());
                    doc.removeAllUselessJointNodes();
                    doc.updateAllViews(null);
                    dragging = false;
                    deselectAllowed = false;
                }

                if(deselectAllowed)
                {
                    doc.getSelectionModel().deselect(((PNgraphicElement)e.getComponent()).getPNelement());
                    deselectAllowed = false;
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
            {
                if(e.getComponent() instanceof GraphicTransition)
                {
                    ((Transition)((GraphicTransition) e.getComponent()).getPNelement()).flip();
                    doc.updateAllViews(null);
                }
            }
        }

    }


    @Override
    public String getName()
    {
        return "Select";
    }

    @Override
    public Icon getIcon()
    {
        return new ImageIcon(SelectControllerFactory.class.getResource("images/Select.gif"));
    }

    @Override
    public void installOn(PNeditorView view)
    {
        NodeMouseAdapter nodeMA = new NodeMouseAdapter(view);

        ViewMouseAdapter viewMA = new ViewMouseAdapter(view, nodeMA);
        view.addMouseListener(viewMA);
        view.addMouseMotionListener(viewMA); // fondamentale!
        view.addMouseWheelListener(viewMA);

        ViewKeyboardListener viewKL = new ViewKeyboardListener(view);
        view.addKeyListener(viewKL);

        ArcMouseAdapter arcMA = new ArcMouseAdapter(view);

        Component[] components = ((Container)view).getComponents();

        for(Component comp : components)
        {
            if(comp instanceof GraphicArc)
            {
                comp.addMouseListener(arcMA);
                comp.addMouseMotionListener(arcMA);
            }

            if(comp instanceof GraphicNode)
            {
                comp.addMouseListener(nodeMA);
                comp.addMouseMotionListener(nodeMA);
            }
        }
    }

    @Override
    public void uninstallFrom(PNeditorView view)
    {
        view.removeMouseListeners(ViewMouseAdapter.class);
        view.removeMouseMotionListeners(ViewMouseAdapter.class);
        view.removeMouseWheelListeners(ViewMouseAdapter.class);

        view.removeKeyListeners(ViewKeyboardListener.class);

        view.removeMouseListenersFromComponents(NodeMouseAdapter.class);
        view.removeMouseMotionListenersFromComponents(NodeMouseAdapter.class);

        view.removeMouseListenersFromComponents(ArcMouseAdapter.class);
        view.removeMouseMotionListenersFromComponents(ArcMouseAdapter.class);
    }

}