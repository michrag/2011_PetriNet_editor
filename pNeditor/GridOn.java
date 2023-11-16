package pNeditor;

import java.awt.Point;

/**
 * Implementazione "on" della griglia di posizionamento: la griglia è attiva e permette di posizionare gli elementi solamente su punti posti
 * ad intervalli regolari. Il passo di tali intervalli è specificato in {@link Options}.
 *
 */
public class GridOn implements IGrid
{
    private float xstep;
    private float ystep;

    public GridOn()
    {
        xstep = Options.getInstance().getGridXstep();
        ystep = Options.getInstance().getGridYstep();
    }

    @Override
    public Point getPoint(Point point)
    {
        return getPoint(point.x, point.y);
    }

    @Override
    public Point getPoint(int x, int y)
    {
        return new Point(getX(x), getY(y));
    }

    @Override
    public int getX(int x)
    {
        return (int) xstep * Math.round(((float)x) / xstep);
    }

    @Override
    public int getY(int y)
    {
        return (int) ystep * Math.round(((float)y) / ystep);
    }

}
