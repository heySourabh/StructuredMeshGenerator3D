package geom;

import mesh.Parameter;

public class SimpleHexahedralGeom implements Geometry {

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

    /**
     * The points are defined as described by the VTK cell type VTK_HEXAHEDRON.
     *
     * @param numXiPoints Number of points for defining the curves changing in
     * xi direction.
     * @param numEtaPoints Number of points for defining the curves changing in
     * eta direction.
     * @param numZetaPoints Number of points for defining the curves changing in
     * zeta direction.
     * @param p0 point at xi = 0, eta = 0, zeta = 0
     * @param p1 point at xi = 0, eta = 1, zeta = 0
     * @param p2 point at xi = 0, eta = 1, zeta = 1
     * @param p3 point at xi = 0, eta = 0, zeta = 1
     * @param p4 point at xi = 1, eta = 0, zeta = 0
     * @param p5 point at xi = 1, eta = 1, zeta = 0
     * @param p6 point at xi = 1, eta = 1, zeta = 1
     * @param p7 point at xi = 1, eta = 0, zeta = 1
     */
    public SimpleHexahedralGeom(int numXiPoints, int numEtaPoints, int numZetaPoints,
            Point p0, Point p1, Point p2, Point p3,
            Point p4, Point p5, Point p6, Point p7) {

        this.numXiPoints = numXiPoints;
        this.numEtaPoints = numEtaPoints;
        this.numZetaPoints = numZetaPoints;

        Point pa, pb;

        // Xi changing curves
        eta0_zeta0 = new Point[numXiPoints];
        eta1_zeta0 = new Point[numXiPoints];
        eta0_zeta1 = new Point[numXiPoints];
        eta1_zeta1 = new Point[numXiPoints];
        double dXi = 1.0 / (numXiPoints - 1);
        for (int i = 0; i < numXiPoints; i++) {
            Parameter xiLoc = new Parameter(i * dXi);

            pa = p0;
            pb = p4;
            eta0_zeta0[i] = Point.between(pa, pb, xiLoc);

            pa = p1;
            pb = p5;
            eta1_zeta0[i] = Point.between(pa, pb, xiLoc);

            pa = p3;
            pb = p7;
            eta0_zeta1[i] = Point.between(pa, pb, xiLoc);

            pa = p2;
            pb = p6;
            eta1_zeta1[i] = Point.between(pa, pb, xiLoc);
        }

        // Eta changing curves
        xi0_zeta0 = new Point[numEtaPoints];
        xi1_zeta0 = new Point[numEtaPoints];
        xi0_zeta1 = new Point[numEtaPoints];
        xi1_zeta1 = new Point[numEtaPoints];
        double dEta = 1.0 / (numEtaPoints - 1);
        for (int j = 0; j < numEtaPoints; j++) {
            Parameter etaLoc = new Parameter(j * dEta);

            pa = p0;
            pb = p1;
            xi0_zeta0[j] = Point.between(pa, pb, etaLoc);

            pa = p4;
            pb = p5;
            xi1_zeta0[j] = Point.between(pa, pb, etaLoc);

            pa = p3;
            pb = p2;
            xi0_zeta1[j] = Point.between(pa, pb, etaLoc);

            pa = p7;
            pb = p6;
            xi1_zeta1[j] = Point.between(pa, pb, etaLoc);
        }

        // Zeta changing curves
        xi0_eta0 = new Point[numZetaPoints];
        xi1_eta0 = new Point[numZetaPoints];
        xi0_eta1 = new Point[numZetaPoints];
        xi1_eta1 = new Point[numZetaPoints];
        double dZeta = 1.0 / (numZetaPoints - 1);
        for (int k = 0; k < numZetaPoints; k++) {
            Parameter zetaLoc = new Parameter(k * dZeta);

            pa = p0;
            pb = p3;
            xi0_eta0[k] = Point.between(pa, pb, zetaLoc);

            pa = p4;
            pb = p7;
            xi1_eta0[k] = Point.between(pa, pb, zetaLoc);

            pa = p1;
            pb = p2;
            xi0_eta1[k] = Point.between(pa, pb, zetaLoc);

            pa = p5;
            pb = p6;
            xi1_eta1[k] = Point.between(pa, pb, zetaLoc);
        }
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
