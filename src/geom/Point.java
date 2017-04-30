package geom;

import mesh.Parameter;
import util.Range;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class Point {

    public final double x, y, z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y, z + p.z);
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y, z - p.z);
    }

    public Point mul(double s) {
        return new Point(x * s, y * s, z * s);
    }

    /**
     * Linear interpolation between p1 and p2
     *
     * @param p1 point
     * @param p2 point
     * @param at How far from p1. 0=p1, 1=p2
     * @return A point whose location is linearly interpolated between p1 and p2
     * by using the parameter.
     */
    public static Point between(Point p1, Point p2, Parameter at) {
        Range xRange = new Range(p1.x, p2.x);
        Range yRange = new Range(p1.y, p2.y);
        Range zRange = new Range(p1.z, p2.z);

        Range parameterRange = new Range(0, 1);

        double x = Range.map(at.val, parameterRange, xRange);
        double y = Range.map(at.val, parameterRange, yRange);
        double z = Range.map(at.val, parameterRange, zRange);

        return new Point(x, y, z);
    }
}
