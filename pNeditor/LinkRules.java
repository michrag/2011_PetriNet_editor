package pNeditor;

import graphicDomain.GraphicJointNode;
import graphicDomain.GraphicNode;

import java.util.Set;

import petriNetDomain.Link;
import petriNetDomain.Link.LinkTypeEnum;
import petriNetDomain.Place;
import petriNetDomain.Transition;

/**
 * Implementazione di {@link ILinkRules} che rispecchia la sintassi delle reti di Petri.
 *
 */
public class LinkRules implements ILinkRules
{
    @Override
    public boolean isSatisfied(GraphicNode backNode, GraphicNode fwdNode)
    {
        // superfluo, non c'ho installato i listeners sopra!
        if(backNode instanceof GraphicJointNode || fwdNode instanceof GraphicJointNode)
        {
            return false;
        }

        if(backNode.getClass() == fwdNode.getClass())
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean isSatisfied(Place place, Transition transition, Link.LinkTypeEnum attemptingToCreateLinkType, Set<Link> existingLinks)
    {
        for(Link link : existingLinks)
        {
            // vietato creare più di una volta lo stesso tipo di link tra una place e una transition!
            if(link.getPlace() == place && link.getTransition() == transition)
                if(link.getType() == attemptingToCreateLinkType)
                {
                    return false;
                }
        }

        for(Link link : existingLinks)
        {
            // una place e una transition non possono essere linkate da una precondizione normale E da una una inibitrice!
            if(link.getPlace() == place && link.getTransition() == transition)
            {
                if((link.getType() == LinkTypeEnum.INHIBITOR_PRECONDITION) && (attemptingToCreateLinkType == LinkTypeEnum.NORMAL_PRECONDITION))
                {
                    return false;
                }

                if((link.getType() == LinkTypeEnum.NORMAL_PRECONDITION) && (attemptingToCreateLinkType == LinkTypeEnum.INHIBITOR_PRECONDITION))
                {
                    return false;
                }
            }
        }

        // tutto il resto è permesso?!
        return true;
    }

}
