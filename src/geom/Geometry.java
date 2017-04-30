package geom;

import java.util.stream.IntStream;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public interface Geometry {

    public int numXiPoints();

    public int numEtaPoints();

    public int numZetaPoints();

    // Xi changing curves
    Point eta0_zeta0(int indexXi);

    Point eta1_zeta0(int indexXi);

    Point eta0_zeta1(int indexXi);

    Point eta1_zeta1(int indexXi);

    // Eta changing curves
    Point xi0_zeta0(int indexEta);

    Point xi1_zeta0(int indexEta);

    Point xi0_zeta1(int indexEta);

    Point xi1_zeta1(int indexEta);

    // Zeta changing curves
    Point xi0_eta0(int indexZeta);

    Point xi1_eta0(int indexZeta);

    Point xi0_eta1(int indexZeta);

    Point xi1_eta1(int indexZeta);

    default Face xi0() {
        Point[] xi0_eta0 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi0_eta0(i))
                .toArray(size -> new Point[size]);
        Point[] xi0_eta1 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi0_eta1(i))
                .toArray(size -> new Point[size]);
        Point[] xi0_zeta0 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi0_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] xi0_zeta1 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi0_zeta1(i))
                .toArray(size -> new Point[size]);

        return new Surface(xi0_eta0, xi0_eta1, xi0_zeta0, xi0_zeta1);
    }

    default Face xi1() {
        Point[] xi1_eta0 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi1_eta0(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_eta1 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi1_eta1(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_zeta0 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi1_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_zeta1 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi1_zeta1(i))
                .toArray(size -> new Point[size]);

        return new Surface(xi1_eta0, xi1_eta1, xi1_zeta0, xi1_zeta1);
    }

    default Face eta0() {
        Point[] xi0_eta0 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi0_eta0(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_eta0 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi1_eta0(i))
                .toArray(size -> new Point[size]);
        Point[] eta0_zeta0 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta0_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] eta0_zeta1 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta0_zeta1(i))
                .toArray(size -> new Point[size]);

        return new Surface(xi0_eta0, xi1_eta0, eta0_zeta0, eta0_zeta1);
    }

    default Face eta1() {
        Point[] xi0_eta1 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi0_eta1(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_eta1 = IntStream.range(0, numZetaPoints())
                .mapToObj(i -> xi1_eta1(i))
                .toArray(size -> new Point[size]);
        Point[] eta1_zeta0 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta1_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] eta1_zeta1 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta1_zeta1(i))
                .toArray(size -> new Point[size]);

        return new Surface(xi0_eta1, xi1_eta1, eta1_zeta0, eta1_zeta1);
    }

    default Face zeta0() {
        Point[] xi0_zeta0 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi0_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_zeta0 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi1_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] eta0_zeta0 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta0_zeta0(i))
                .toArray(size -> new Point[size]);
        Point[] eta1_zeta0 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta1_zeta0(i))
                .toArray(size -> new Point[size]);

        return new Surface(xi0_zeta0, xi1_zeta0, eta0_zeta0, eta1_zeta0);
    }

    default Face zeta1() {
        Point[] xi0_zeta1 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi0_zeta1(i))
                .toArray(size -> new Point[size]);
        Point[] xi1_zeta1 = IntStream.range(0, numEtaPoints())
                .mapToObj(i -> xi1_zeta1(i))
                .toArray(size -> new Point[size]);
        Point[] eta0_zeta1 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta0_zeta1(i))
                .toArray(size -> new Point[size]);
        Point[] eta1_zeta1 = IntStream.range(0, numXiPoints())
                .mapToObj(i -> eta1_zeta1(i))
                .toArray(size -> new Point[size]);

        return new Surface(xi0_zeta1, xi1_zeta1, eta0_zeta1, eta1_zeta1);
    }
}
