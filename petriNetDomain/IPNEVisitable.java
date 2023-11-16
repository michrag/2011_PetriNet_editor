package petriNetDomain;

/**
 * Interfaccia che permette alla classe che la implementa di essere visitata
 * da un oggetto Visitor (che implementa {@link IPNEVisitor}).
 *
 */
public interface IPNEVisitable
{
    /**
     * Accetta l'{@link IPNEVisitor} v.
     * @param v il Visitor da accettare.
     */
    public void accept(IPNEVisitor v);
}
