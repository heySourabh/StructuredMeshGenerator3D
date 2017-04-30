package geom;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class Surface implements Face {

    private final Point[] dirA0;
    private final Point[] dirA1;
    private final Point[] dirB0;
    private final Point[] dirB1;

    public Surface(Point[] dirA0, Point[] dirA1, Point[] dirB0, Point[] dirB1) {
        this.dirA0 = dirA0;
        this.dirA1 = dirA1;
        this.dirB0 = dirB0;
        this.dirB1 = dirB1;
    }

    @Override
    public Point[] dirA0() {
        return dirA0;
    }

    @Override
    public Point[] dirA1() {
        return dirA1;
    }

    @Override
    public Point[] dirB0() {
        return dirB0;
    }

    @Override
    public Point[] dirB1() {
        return dirB1;
    }
}
