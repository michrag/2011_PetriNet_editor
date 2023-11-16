package pNeditor;

import graphicDomain.GraphicNode;

import java.util.Set;

import petriNetDomain.Link;
import petriNetDomain.Link.LinkTypeEnum;
import petriNetDomain.Place;
import petriNetDomain.Transition;

/**
 * Interfaccia che definisce le regole che stabiliscono quali collegamenti sono leciti e quali no.
 *
 *
 * @see Link
 */
public interface ILinkRules
{
    /**
     * Determina se è lecito collegare tra loro due {@link GraphicNode}.
     * @param backNode primo {@link GraphicNode}
     * @param fwdNode secondo {@link GraphicNode}
     * @return <code>true</code> se e solo se i due {@link GraphicNode} possono essere collegati tra loro.
     */
    public boolean isSatisfied(GraphicNode backNode, GraphicNode fwdNode);

    /**
     * Determina se una particolare {@link Place} e una particolare {@link Transition} possono essere collegate tra loro con un {@link Link}
     * di tipo {@link LinkTypeEnum}.
     * @param place la {@link Place} coinvolta.
     * @param transition la {@link Transition} coinvolta.
     * @param attemptingToCreateLinkType il {@link LinkTypeEnum} del {@link Link} che si vorrebbe creare.
     * @param existingLinks l'insieme di {@link Link} esistenti al quale si vorrebbe aggiungere il Link che si vorrebbe creare.
     * @return <code>true</code> se e solo se è lecito collegare la {@link Place} e la {@link Transition} con un {@link Link} di tipo {@link LinkTypeEnum}.
     */
    public boolean isSatisfied(Place place, Transition transition, Link.LinkTypeEnum attemptingToCreateLinkType, Set<Link> existingLinks);
}
