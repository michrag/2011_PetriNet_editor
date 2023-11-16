package pNeditor;

import java.awt.Dimension;

/**
 * Questa classe, realizzata con un Singleton, memorizza e fornisce tutte le costanti numeriche che si vorrebbe poter modificare,
 * alcune delle quali verosimilmente anche da parte dell'utente stesso.
 *
 */
public class Options
{
    private static Options instance = null;

    public static Options getInstance()
    {
        if(instance == null)
        {
            instance = new Options();
        }

        return instance;
    }

    private Options()
    {
        super();
    }

    private Dimension placeDimensionPredef;
    private Dimension transitionDimensionPredef;
    private Dimension jointnodeDimensionPredef;
    private int arcCircleDiameterPredef;
    private int arcArrowSizePredef;
    private int labelFontSizePredef;
    private Dimension labelNodeDimensionPredef;
    private Dimension labelArcDimensionPredef;

    public Dimension getPlaceDimensionPredef()
    {
        return placeDimensionPredef;
    }

    public void setPlaceDimensionPredef(Dimension placeDimensionPredef)
    {
        this.placeDimensionPredef = placeDimensionPredef;
    }

    public Dimension getTransitionDimensionPredef()
    {
        return transitionDimensionPredef;
    }

    public void setTransitionDimensionPredef(Dimension transitionDimensionPredef)
    {
        this.transitionDimensionPredef = transitionDimensionPredef;
    }

    public Dimension getJointnodeDimensionPredef()
    {
        return jointnodeDimensionPredef;
    }

    public void setJointnodeDimensionPredef(Dimension jointnodeDimensionPredef)
    {
        this.jointnodeDimensionPredef = jointnodeDimensionPredef;
    }

    public void setArcCircleDiameterPredef(int arcCircleDiameterPredef)
    {
        this.arcCircleDiameterPredef = arcCircleDiameterPredef;
    }

    public int getArcCircleDiameterPredef()
    {
        return arcCircleDiameterPredef;
    }

    public int getArcArrowSizePredef()
    {
        return arcArrowSizePredef;
    }

    public void setArcArrowSizePredef(int arcArrowSizePredef)
    {
        this.arcArrowSizePredef = arcArrowSizePredef;
    }

    public int getLabelFontSizePredef()
    {
        return labelFontSizePredef;
    }

    public void setLabelFontSizePredef(int labelFontSizePredef)
    {
        this.labelFontSizePredef = labelFontSizePredef;
    }

    public Dimension getLabelNodeDimensionPredef()
    {
        return labelNodeDimensionPredef;
    }

    public void setLabelNodeDimensionPredef(Dimension labelNodeDimensionPredef)
    {
        this.labelNodeDimensionPredef = labelNodeDimensionPredef;
    }

    public Dimension getLabelArcDimensionPredef()
    {
        return labelArcDimensionPredef;
    }

    public void setLabelArcDimensionPredef(Dimension labelArcDimensionPredef)
    {
        this.labelArcDimensionPredef = labelArcDimensionPredef;
    }


    private float gridXstep;
    private float gridYstep;

    public float getGridXstep()
    {
        return gridXstep;
    }

    public void setGridXstep(float gridXstep)
    {
        this.gridXstep = gridXstep;
    }

    public float getGridYstep()
    {
        return gridYstep;
    }

    public void setGridYstep(float gridYstep)
    {
        this.gridYstep = gridYstep;
    }

    private float zoomScaleFactor;

    public float getZoomScaleFactor()
    {
        return zoomScaleFactor;
    }

    public void setZoomScaleFactor(float zoomScaleFactor)
    {
        this.zoomScaleFactor = zoomScaleFactor;
    }


    private double jointNodeRemovingEpsilon;

    public void setJointNodeRemovingEpsilon(double jointNodeRemovingEpsilon)
    {
        this.jointNodeRemovingEpsilon = jointNodeRemovingEpsilon;
    }

    public double getJointNodeRemovingEpsilon()
    {
        return jointNodeRemovingEpsilon;
    }

    private boolean jointnodesVisibility;

    public void setJointnodesVisibility(boolean jointnodesVisibility)
    {
        this.jointnodesVisibility = jointnodesVisibility;
    }

    public boolean getJointnodesVisibility()
    {
        return jointnodesVisibility;
    }

}
