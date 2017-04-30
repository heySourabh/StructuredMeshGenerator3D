package geom;

import java.util.stream.IntStream;
import mesh.Parameter;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class QuadFace implements Face {

    private final Point p0, p1, p2, p3;
    private final Point[] dirA0, dirA1, dirB0, dirB1;

    /**
     * Quad surface made up of straight edges p0--p1--p2--p3--p0.
     *
     * @param p0 corner point
     * @param p1 corner point
     * @param p2 corner point
     * @param p3 corner point
     * @param numAPoints number of points in A direction
     * @param numBPoints number of points in B direction
     */
    public QuadFace(Point p0, Point p1, Point p2, Point p3, int numAPoints, int numBPoints) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        double dA = 1.0 / (numAPoints - 1);
        double dB = 1.0 / (numAPoints - 1);
        // dirA0: joining p0--p1.
        dirA0 = IntStream.range(0, numAPoints)
                .mapToObj(i -> new Parameter(i * dA))
                .map(parameter -> Point.between(p0, p1, parameter))
                .toArray(size -> new Point[size]);

        dirA1 = IntStream.range(0, numAPoints)
                .mapToObj(i -> new Parameter(i * dA))
                .map(parameter -> Point.between(p3, p2, parameter))
                .toArray(size -> new Point[size]);

        dirB0 = IntStream.range(0, numBPoints)
                .mapToObj(i -> new Parameter(i * dB))
                .map(parameter -> Point.between(p0, p3, parameter))
                .toArray(size -> new Point[size]);

        dirB1 = IntStream.range(0, numBPoints)
                .mapToObj(i -> new Parameter(i * dB))
                .map(parameter -> Point.between(p1, p2, parameter))
                .toArray(size -> new Point[size]);
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
