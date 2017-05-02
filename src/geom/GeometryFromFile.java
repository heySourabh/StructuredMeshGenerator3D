package geom;

import io.GeometryFileReader;
import io.GeometryFileReader.Label;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import mesh.Parameter;
import util.Range;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class GeometryFromFile implements Geometry {

    private static final double TOLERANCE = 1e-8;
    private final int numXiPoints;
    private final int numEtaPoints;
    private final int numZetaPoints;

    // Xi changing curves
    private final Point[] eta0_zeta0;
    private final Point[] eta1_zeta0;
    private final Point[] eta0_zeta1;
    private final Point[] eta1_zeta1;

    // Eta changing curves
    private final Point[] xi0_zeta0;
    private final Point[] xi1_zeta0;
    private final Point[] xi0_zeta1;
    private final Point[] xi1_zeta1;

    // Zeta changing curves
    private final Point[] xi0_eta0;
    private final Point[] xi1_eta0;
    private final Point[] xi0_eta1;
    private final Point[] xi1_eta1;

    private final Map<Label, Point[]> cPts;
    private final Map<Label, double[]> pointToParamMapping;

    public GeometryFromFile(File file, int numXiPoints, int numEtaPoints, int numZetaPoints) throws FileNotFoundException {
        this.numXiPoints = numXiPoints;
        this.numEtaPoints = numEtaPoints;
        this.numZetaPoints = numZetaPoints;

        cPts = new HashMap<>();
        pointToParamMapping = new HashMap<>();

        for (Label label : Label.values()) {
            Point[] points = GeometryFileReader.readPoints(file, label);
            if (points.length < 2) {
                throw new IllegalArgumentException("The number of points per side must be at least 2.");
            }
            cPts.put(label, points);
        }

        // TODO: Check continuity of geometry
        if (!overlapping(cPts.get(Label.xi0_eta0)[0],
                cPts.get(Label.eta0_zeta0)[0],
                cPts.get(Label.xi0_zeta0)[0])
                || !overlapping(cPts.get(Label.xi0_eta1)[0],
                        cPts.get(Label.eta1_zeta0)[0],
                        cPts.get(Label.xi0_zeta0)[cPts.get(Label.xi0_zeta0).length - 1])
                || !overlapping(cPts.get(Label.xi0_eta1)[cPts.get(Label.xi0_eta1).length - 1],
                        cPts.get(Label.xi0_zeta1)[cPts.get(Label.xi0_zeta1).length - 1],
                        cPts.get(Label.eta1_zeta1)[0])
                || !overlapping(cPts.get(Label.xi0_eta0)[cPts.get(Label.xi0_eta0).length - 1],
                        cPts.get(Label.eta0_zeta1)[0],
                        cPts.get(Label.xi0_zeta1)[0])
                || !overlapping(cPts.get(Label.xi1_eta0)[0],
                        cPts.get(Label.eta0_zeta0)[cPts.get(Label.eta0_zeta0).length - 1],
                        cPts.get(Label.xi1_zeta0)[0])
                || !overlapping(cPts.get(Label.xi1_eta1)[0],
                        cPts.get(Label.eta1_zeta0)[cPts.get(Label.eta1_zeta0).length - 1],
                        cPts.get(Label.xi1_zeta0)[cPts.get(Label.xi1_zeta0).length - 1])
                || !overlapping(cPts.get(Label.xi1_eta1)[cPts.get(Label.xi1_eta1).length - 1],
                        cPts.get(Label.xi1_zeta1)[cPts.get(Label.xi1_zeta1).length - 1],
                        cPts.get(Label.eta1_zeta1)[cPts.get(Label.eta1_zeta1).length - 1])
                || !overlapping(cPts.get(Label.xi1_eta0)[cPts.get(Label.xi1_eta0).length - 1],
                        cPts.get(Label.eta0_zeta1)[cPts.get(Label.eta0_zeta1).length - 1],
                        cPts.get(Label.xi1_zeta1)[0])) {
            throw new IllegalArgumentException("The geometry points supplied do not overlap properly.");
        }

        // create the mapping between points and parameters
        for (Label label : Label.values()) {
            pointToParamMapping.put(label, calculateParameterMapping(cPts.get(label)));
        }

        double[] parMap;
        Point[] curvePoints;
        // Xi changing curves
        eta0_zeta0 = new Point[numXiPoints];
        eta1_zeta0 = new Point[numXiPoints];
        eta0_zeta1 = new Point[numXiPoints];
        eta1_zeta1 = new Point[numXiPoints];
        double dXi = 1.0 / (numXiPoints - 1);
        for (int i = 0; i < numXiPoints; i++) {
            Parameter xiLoc = new Parameter(i * dXi);

            parMap = pointToParamMapping.get(Label.eta0_zeta0);
            curvePoints = cPts.get(Label.eta0_zeta0);
            eta0_zeta0[i] = getPoint(xiLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.eta1_zeta0);
            curvePoints = cPts.get(Label.eta1_zeta0);
            eta1_zeta0[i] = getPoint(xiLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.eta0_zeta1);
            curvePoints = cPts.get(Label.eta0_zeta1);
            eta0_zeta1[i] = getPoint(xiLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.eta1_zeta1);
            curvePoints = cPts.get(Label.eta1_zeta1);
            eta1_zeta1[i] = getPoint(xiLoc, parMap, curvePoints);
        }

        // Eta changing curves
        xi0_zeta0 = new Point[numEtaPoints];
        xi1_zeta0 = new Point[numEtaPoints];
        xi0_zeta1 = new Point[numEtaPoints];
        xi1_zeta1 = new Point[numEtaPoints];
        double dEta = 1.0 / (numEtaPoints - 1);
        for (int j = 0; j < numEtaPoints; j++) {
            Parameter etaLoc = new Parameter(j * dEta);

            parMap = pointToParamMapping.get(Label.xi0_zeta0);
            curvePoints = cPts.get(Label.xi0_zeta0);
            xi0_zeta0[j] = getPoint(etaLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.xi1_zeta0);
            curvePoints = cPts.get(Label.xi1_zeta0);
            xi1_zeta0[j] = getPoint(etaLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.xi0_zeta1);
            curvePoints = cPts.get(Label.xi0_zeta1);
            xi0_zeta1[j] = getPoint(etaLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.xi1_zeta1);
            curvePoints = cPts.get(Label.xi1_zeta1);
            xi1_zeta1[j] = getPoint(etaLoc, parMap, curvePoints);
        }

        // Zeta changing curves
        xi0_eta0 = new Point[numZetaPoints];
        xi1_eta0 = new Point[numZetaPoints];
        xi0_eta1 = new Point[numZetaPoints];
        xi1_eta1 = new Point[numZetaPoints];
        double dZeta = 1.0 / (numZetaPoints - 1);
        for (int k = 0; k < numZetaPoints; k++) {
            Parameter zetaLoc = new Parameter(k * dZeta);

            parMap = pointToParamMapping.get(Label.xi0_eta0);
            curvePoints = cPts.get(Label.xi0_eta0);
            xi0_eta0[k] = getPoint(zetaLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.xi1_eta0);
            curvePoints = cPts.get(Label.xi1_eta0);
            xi1_eta0[k] = getPoint(zetaLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.xi0_eta1);
            curvePoints = cPts.get(Label.xi0_eta1);
            xi0_eta1[k] = getPoint(zetaLoc, parMap, curvePoints);

            parMap = pointToParamMapping.get(Label.xi1_eta1);
            curvePoints = cPts.get(Label.xi1_eta1);
            xi1_eta1[k] = getPoint(zetaLoc, parMap, curvePoints);
        }
    }

    private double[] calculateParameterMapping(Point[] points) {
        double[] lengths = IntStream.range(1, points.length)
                .mapToDouble(i -> points[i].dist(points[i - 1]))
                .toArray();

        double[] cumLen = new double[points.length];
        cumLen[0] = 0.0;
        for (int i = 1; i < points.length; i++) {
            cumLen[i] = cumLen[i - 1] + lengths[i - 1];
        }

        for (int i = 0; i < cumLen.length; i++) {
            cumLen[i] = cumLen[i] / cumLen[cumLen.length - 1];
        }

        return cumLen;
    }

    private boolean overlapping(Point p1, Point p2, Point p3) {
        return p1.dist(p2) < TOLERANCE && p1.dist(p3) < TOLERANCE;
    }

    private Point getPoint(Parameter parameter, double[] paramter_map, Point[] points) {
        // where does the parameter lie
        double parVal = parameter.val;
        int indexR = IntStream.range(1, paramter_map.length)
                .filter(i -> (paramter_map[i - 1] - TOLERANCE) < parVal && (paramter_map[i] + TOLERANCE) > parVal)
                .findFirst().getAsInt();
        int indexL = indexR - 1;
        double parL = paramter_map[indexL];
        double parR = paramter_map[indexR];
        Point pointL = points[indexL];
        Point pointR = points[indexR];

        double x = Range.map(parVal, new Range(parL, parR), new Range(pointL.x, pointR.x));
        double y = Range.map(parVal, new Range(parL, parR), new Range(pointL.y, pointR.y));
        double z = Range.map(parVal, new Range(parL, parR), new Range(pointL.z, pointR.z));
        return new Point(x, y, z);
    }

    @Override
    public int numXiPoints() {
        return numXiPoints;
    }

    @Override
    public int numEtaPoints() {
        return numEtaPoints;
    }

    @Override
    public int numZetaPoints() {
        return numZetaPoints;
    }

    @Override
    public Point eta0_zeta0(int indexXi) {
        return eta0_zeta0[indexXi];
    }

    @Override
    public Point eta1_zeta0(int indexXi) {
        return eta1_zeta0[indexXi];
    }

    @Override
    public Point eta0_zeta1(int indexXi) {
        return eta0_zeta1[indexXi];
    }

    @Override
    public Point eta1_zeta1(int indexXi) {
        return eta1_zeta1[indexXi];
    }

    @Override
    public Point xi0_zeta0(int indexEta) {
        return xi0_zeta0[indexEta];
    }

    @Override
    public Point xi1_zeta0(int indexEta) {
        return xi1_zeta0[indexEta];
    }

    @Override
    public Point xi0_zeta1(int indexEta) {
        return xi0_zeta1[indexEta];
    }

    @Override
    public Point xi1_zeta1(int indexEta) {
        return xi1_zeta1[indexEta];
    }

    @Override
    public Point xi0_eta0(int indexZeta) {
        return xi0_eta0[indexZeta];
    }

    @Override
    public Point xi1_eta0(int indexZeta) {
        return xi1_eta0[indexZeta];
    }

    @Override
    public Point xi0_eta1(int indexZeta) {
        return xi0_eta1[indexZeta];
    }

    @Override
    public Point xi1_eta1(int indexZeta) {
        return xi1_eta1[indexZeta];
    }
}
