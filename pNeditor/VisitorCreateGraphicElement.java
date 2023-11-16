package pNeditor;

import graphicDomain.GraphicArc;
import graphicDomain.GraphicJointNode;
import graphicDomain.GraphicLabelSatellite;
import graphicDomain.GraphicPlace;
import graphicDomain.GraphicSelectionRectSatellite;
import graphicDomain.GraphicTransition;
import graphicDomain.PNgraphicElement;
import petriNetDomain.Arc;
import petriNetDomain.IPNEVisitor;
import petriNetDomain.JointNode;
import petriNetDomain.LabelTextSatellite;
import petriNetDomain.PNelement;
import petriNetDomain.Place;
import petriNetDomain.SelectionSatellite;
import petriNetDomain.Transition;

/**
 * Implementazione di {@link IPNEVisitor} che crea il {@link PNgraphicElement} adatto a ciascun {@link PNelement} visitato.
 *
 */
public class VisitorCreateGraphicElement implements IPNEVisitor
{
    private PNgraphicElement graphicElement;

    public static PNgraphicElement getGraphicElement(PNelement pnElement)
    {
        VisitorCreateGraphicElement visitor = new VisitorCreateGraphicElement();
        pnElement.accept(visitor);
        return visitor.getProperGraphicElement();
    }

    private PNgraphicElement getProperGraphicElement()
    {
        return graphicElement;
    }

    @Override
    public void visitPlace(Place p)
    {
        GraphicPlace comp = new GraphicPlace(p);
        comp.setBounds(p.getRect());
        graphicElement = comp;
    }

    @Override
    public void visitTransition(Transition t)
    {
        GraphicTransition comp = new GraphicTransition(t);
        comp.setBounds(t.getRect());
        graphicElement = comp;
    }

    @Override
    public void visitArc(Arc arc)
    {
        GraphicArc garc = new GraphicArc(arc);
        garc.setBounds(arc.getBackNode(), arc.getFwdNode());
        graphicElement = garc;
    }

    @Override
    public void visitJointNode(JointNode jn)
    {
        GraphicJointNode comp = new GraphicJointNode(jn);
        comp.setBounds(jn.getRect());
        graphicElement = comp;
    }

    @Override
    public void visitLabelSatellite(LabelTextSatellite ls)
    {
        GraphicLabelSatellite comp = new GraphicLabelSatellite(ls);
        comp.setBounds(ls.getRect());
        graphicElement = comp;
    }

    @Override
    public void visitSelectionSatellite(SelectionSatellite ss)
    {
        GraphicSelectionRectSatellite comp = new GraphicSelectionRectSatellite(ss);
        comp.setBounds(ss.getRect());
        graphicElement = comp;
    }

}
