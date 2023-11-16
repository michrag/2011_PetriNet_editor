package pNeditor;

import java.awt.Point;

/**
 * Interfaccia per una griglia di posizionamento. Una griglia di posizionamento è un insieme di funzioni (in senso matematico) da ZxZ in ZxZ
 * o da Z in Z.
 *
 */
public interface IGrid
{
    /**
     *
     * @param point {@link Point} in ingresso
     * @return {@link Point} trasformato da questa {@link IGrid}
     */
    public Point getPoint(Point point);

    /**
     *
     * @param x coordinata x in ingresso
     * @param y coordinata y in ingresso
     * @return {@link Point} trasformato da questa {@link IGrid}
     */
    public Point getPoint(int x, int y);

    /**
     *
     * @param x coordinata x in ingresso
     * @return coordinata x trasformata da questa {@link IGrid}
     */
    public int getX(int x);

    /**
     *
     * @param y coordinata y in ingresso
     * @return coordinata y trasformata da questa {@link IGrid}
     */
    public int getY(int y);
}
