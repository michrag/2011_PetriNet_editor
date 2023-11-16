package petriNetDomain;

/**
 * Interfaccia che permette alla classe che la implementa di visitare le classi per le quali esiste un metodo adatto in questa interfaccia.
 *
 */
public interface IPNEVisitor
{
    /**
     * Visita la {@link Place} p.
     * @param p la {@link Place} da visitare
     */
    public void visitPlace(Place p);

    /**
     * Visita la {@link Transition} t.
     * @param t la {@link Transition} da visitare
     */
    public void visitTransition(Transition t);

    /**
     * Visita l'{@link Arc} a.
     * @param a l'{@link Arc} da visitare.
     */
    public void visitArc(Arc a);

    /**
     * Visita il {@link JointNode} jn.
     * @param jn il {@link JointNode} da visitare.
     */
    public void visitJointNode(JointNode jn);

    /**
     * Visita il {@link LabelTextSatellite} ls.
     * @param ls il {@link LabelTextSatellite} da visitare.
     */
    public void visitLabelSatellite(LabelTextSatellite ls);

    /**
     * Visita il {@link SelectionSatellite} ss.
     * @param ss il {@link SelectionSatellite} da visitare.
     */
    public void visitSelectionSatellite(SelectionSatellite ss);
}
