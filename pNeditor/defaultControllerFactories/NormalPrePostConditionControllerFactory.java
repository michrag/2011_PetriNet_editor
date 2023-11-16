package pNeditor.defaultControllerFactories;

import graphicDomain.GraphicJointNode;
import graphicDomain.GraphicNode;
import graphicDomain.GraphicPlace;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import pNeditor.IControllerFactory;
import pNeditor.ILinkRules;
import pNeditor.LinkRules;
import pNeditor.PNeditorDocument;
import pNeditor.PNeditorView;
import petriNetDomain.Link.LinkTypeEnum;
import petriNetDomain.Place;
import petriNetDomain.Transition;

/**
 * {@link IControllerFactory} corrispondente alla modalità tramite la quale l'utente può creare precondizioni (normali) o postcondizioni,
 * a seconda dell'ordine in cui clicca sugli elementi collegabili.
 *
 */
public class NormalPrePostConditionControllerFactory implements IControllerFactory
{
    /**
     * {@link MouseAdapter} specifico per i {@link GraphicNode}.
     *
     */
    private class NodeMouseAdapter extends MouseAdapter
    {
        private PNeditorView view;
        private ILinkRules rules;

        public NodeMouseAdapter(PNeditorView view)
        {
            super();
            this.view = view;
            rules = new LinkRules();
        }

        private boolean firstClickDone = false;
        private GraphicNode firstClicked = null;
        private GraphicNode secondClicked = null;
        private boolean linkCreated = false;

        @Override
        public void mouseClicked(MouseEvent e)
        {
            PNeditorDocument doc = view.getDocument();

            if(SwingUtilities.isLeftMouseButton(e))
            {
                if(!firstClickDone)
                {
                    firstClicked = (GraphicNode) e.getComponent();
                    firstClickDone = true;
                    doc.getSelectionModel().select(firstClicked.getPNelement(), false);
                    return;
                }

                if(firstClickDone)
                {
                    secondClicked = (GraphicNode) e.getComponent();

                    if((rules.isSatisfied(firstClicked, secondClicked)))
                    {
                        if(firstClicked instanceof GraphicPlace)
                        {
                            Place place = (Place)firstClicked.getPNelement();
                            Transition transition = (Transition)secondClicked.getPNelement();

                            if(rules.isSatisfied(place, transition, LinkTypeEnum.NORMAL_PRECONDITION, doc.getLinks()))
                            {
                                doc.createLink(place, transition, LinkTypeEnum.NORMAL_PRECONDITION);
                                linkCreated = true;
                            }
                        }
                        else // allora è stata cliccata per prima la transizione...
                        {
                            Place place = (Place)secondClicked.getPNelement();
                            Transition transition = (Transition)firstClicked.getPNelement();

                            if(rules.isSatisfied(place, transition, LinkTypeEnum.POSTCONDITION, doc.getLinks()))
                            {
                                doc.createLink(place, transition, LinkTypeEnum.POSTCONDITION);
                                linkCreated = true;
                            }
                        }

                        if(linkCreated)
                        {
                            doc.getSelectionModel().clearSelection();
                            doc.updateAllViews(null);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getName()
    {
        return "Normal Pre or Post Conditions";
    }

    @Override
    public Icon getIcon()
    {
        return new ImageIcon(NormalPrePostConditionControllerFactory.class.getResource("images/Arc.gif"));
    }

    @Override
    public void installOn(PNeditorView view)
    {
        NodeMouseAdapter nodeMA = new NodeMouseAdapter(view);
        Component[] components = ((Container)view).getComponents();

        for(Component comp : components)
            if(comp instanceof GraphicNode && !(comp instanceof GraphicJointNode))
            {
                comp.addMouseListener(nodeMA);
            }
    }

    @Override
    public void uninstallFrom(PNeditorView view)
    {
        view.removeMouseListenersFromComponents(NodeMouseAdapter.class);
    }

}
