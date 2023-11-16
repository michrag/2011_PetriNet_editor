package pNeditor;

import java.awt.Point;

/**
 * Implementazione "off" della griglia di posizionamento: la griglia è disattivata e tutte le funzioni forniscono in uscita i valori di
 * ingresso immutati.
 *
 */
public class GridOff implements IGrid
{
    @Override
    public Point getPoint(Point point)
    {
        return point;
    }

    @Override
    public Point getPoint(int x, int y)
    {
        return new Point(x, y);
    }

    @Override
    public int getX(int x)
    {
        return x;
    }

    @Override
    public int getY(int y)
    {
        return y;
    }

}
