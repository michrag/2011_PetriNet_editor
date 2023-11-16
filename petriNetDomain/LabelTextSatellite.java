package petriNetDomain;

import pNeditor.Options;

/**
 * Classe astratta. Un satellite adatto a contenere un'etichetta di testo.
 *
 */
public abstract class LabelTextSatellite extends Satellite
{
    private static int fontSize;

    /**
     * Restituisce la dimensione del carattere del testo.
     * @return la dimensione del carattere del testo.
     */
    public static int getFontSize()
    {
        return fontSize;
    }

    /**
     * Aumenta di <code>scaleFactor</code> la dimensione del carattere del testo.
     * @param scaleFactor fattore di incremento.
     */
    public static void zoomIn(float scaleFactor)
    {
        fontSize += Math.round(fontSize / scaleFactor);
    }

    /**
     * Riduce di <code>scaleFactor</code> la dimensione del carattere del testo.
     * @param scaleFactor fattore di decremento.
     */
    public static void zoomOut(float scaleFactor)
    {
        fontSize -= Math.round(fontSize / scaleFactor);
    }

    /**
     * Reimposta la dimensione del carattere del testo al valore predefinito.
     */
    public static void zoomReset()
    {
        fontSize = Options.getInstance().getLabelFontSizePredef();
    }

    /**
     * Costruisce un oggetto {@link LabelTextSatellite}, il cui pianeta è <code>planet</code>.
     * @param planet il "pianeta" di questo {@link LabelTextSatellite}.
     */
    public LabelTextSatellite(PNelement planet)
    {
        super(planet);
    }

    @Override
    public void accept(IPNEVisitor v)
    {
        v.visitLabelSatellite(this);
    }

    @Override
    public String getName()
    {
        return "LabelSatellite of " + getPlanet().getName();
    }

    /**
     * Restituisce il testo che sarà scritto nell'etichetta.
     * @return la stringa da scrivere nell'etichetta.
     */
    public abstract String getText();

}
