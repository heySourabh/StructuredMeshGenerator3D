package mesh;

import geom.Face;
import geom.Geometry;
import geom.Point;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class TransfiniteInterpolation {

    public static Point[][][] interpolate(Geometry geom) {
        int numXiPoints = geom.numXiPoints();
        int numEtaPoints = geom.numEtaPoints();
        int numZetaPoints = geom.numZetaPoints();
        Point[][][] volPoints
                = new Point[numXiPoints][numEtaPoints][numZetaPoints];

        int i, j, k;

        // Calculate interpolaetd points for all faces
        // xi = 0
        Point[][] xi0 = interpolateFace(geom.xi0());
        i = 0;
        for (j = 0; j < numEtaPoints; j++) {
            for (k = 0; k < numZetaPoints; k++) {
                volPoints[i][j][k] = xi0[j][k];
            }
        }

        // xi = 1
        Point[][] xi1 = interpolateFace(geom.xi1());
        i = numXiPoints - 1;
        for (j = 0; j < numEtaPoints; j++) {
            for (k = 0; k < numZetaPoints; k++) {
                volPoints[i][j][k] = xi1[j][k];
            }
        }

        // eta = 0
        Point[][] eta0 = interpolateFace(geom.eta0());
        j = 0;
        for (i = 0; i < numXiPoints; i++) {
            for (k = 0; k < numZetaPoints; k++) {
                volPoints[i][j][k] = eta0[i][k];
            }
        }

        // eta = 1
        Point[][] eta1 = interpolateFace(geom.eta1());
        j = numEtaPoints - 1;
        for (i = 0; i < numXiPoints; i++) {
            for (k = 0; k < numZetaPoints; k++) {
                volPoints[i][j][k] = eta1[i][k];
            }
        }

        // zeta = 0
        Point[][] zeta0 = interpolateFace(geom.zeta0());
        k = 0;
        for (i = 0; i < numXiPoints; i++) {
            for (j = 0; j < numEtaPoints; j++) {
                volPoints[i][j][k] = zeta0[i][j];
            }
        }

        // zeta = 1
        Point[][] zeta1 = interpolateFace(geom.zeta1());
        k = numZetaPoints - 1;
        for (i = 0; i < numXiPoints; i++) {
            for (j = 0; j < numEtaPoints; j++) {
                volPoints[i][j][k] = zeta1[i][j];
            }
        }

        double dXi = 1.0 / (numXiPoints - 1);
        double dEta = 1.0 / (numEtaPoints - 1);
        double dZeta = 1.0 / (numZetaPoints - 1);
        int xiEnd = numXiPoints - 1;
        int etaEnd = numEtaPoints - 1;
        int zetaEnd = numZetaPoints - 1;
        for (i = 1; i < numXiPoints - 1; i++) {
            double xi = i * dXi;
            for (j = 1; j < numEtaPoints - 1; j++) {
                double eta = j * dEta;
                for (k = 1; k < numZetaPoints - 1; k++) {
                    double zeta = k * dZeta;

                    // Linear projection
                    Point proj_xi = volPoints[0][j][k].mul(1 - xi)
                            .add(volPoints[xiEnd][j][k].mul(xi));

                    Point proj_eta = volPoints[i][0][k].mul(1 - eta)
                            .add(volPoints[i][etaEnd][k].mul(eta));

                    Point proj_zeta = volPoints[i][j][0].mul(1 - zeta)
                            .add(volPoints[i][j][zetaEnd].mul(zeta));

                    // Bi-linear projection
                    Point proj_xi_eta
                            = volPoints[0][0][k].mul((1 - xi) * (1 - eta))
                            .add(volPoints[0][etaEnd][k].mul((1 - xi) * eta))
                            .add(volPoints[xiEnd][0][k].mul(xi * (1 - eta)))
                            .add(volPoints[xiEnd][etaEnd][k].mul(xi * eta));

                    Point proj_eta_zeta
                            = volPoints[i][0][0].mul((1 - eta) * (1 - zeta))
                            .add(volPoints[i][0][zetaEnd].mul((1 - eta) * zeta))
                            .add(volPoints[i][etaEnd][0].mul(eta * (1 - zeta)))
                            .add(volPoints[i][etaEnd][zetaEnd].mul(eta * zeta));

                    Point proj_xi_zeta
                            = volPoints[0][j][0].mul((1 - xi) * (1 - zeta))
                            .add(volPoints[0][j][zetaEnd].mul((1 - xi) * zeta))
                            .add(volPoints[xiEnd][j][0].mul(xi * (1 - zeta)))
                            .add(volPoints[xiEnd][j][zetaEnd].mul(xi * zeta));

                    // Tri-linear projection
                    Point proj_xi_eta_zeta
                            = volPoints[0][0][0].mul((1 - xi) * (1 - eta) * (1 - zeta))
                            .add(volPoints[xiEnd][0][0].mul(xi * (1 - eta) * (1 - zeta)))
                            .add(volPoints[0][etaEnd][0].mul((1 - xi) * eta * (1 - zeta)))
                            .add(volPoints[0][0][zetaEnd].mul((1 - xi) * (1 - eta) * zeta))
                            .add(volPoints[xiEnd][etaEnd][0].mul(xi * eta * (1 - zeta)))
                            .add(volPoints[xiEnd][0][zetaEnd].mul(xi * (1 - eta) * zeta))
                            .add(volPoints[0][etaEnd][zetaEnd].mul((1 - xi) * eta * zeta))
                            .add(volPoints[xiEnd][etaEnd][zetaEnd].mul(xi * eta * zeta));

                    volPoints[i][j][k] = proj_xi
                            .add(proj_eta)
                            .add(proj_zeta)
                            .sub(proj_eta_zeta)
                            .sub(proj_xi_eta)
                            .sub(proj_xi_zeta)
                            .add(proj_xi_eta_zeta);
                }
            }
        }

        return volPoints;
    }

    public static Point[][] interpolateFace(Face face) {
        Point[] xi0 = face.dirA0(); // left
        Point[] xi1 = face.dirA1(); // right
        Point[] eta0 = face.dirB0(); // bottom
        Point[] eta1 = face.dirB1(); // top

        int numEtaPoints = xi0.length;
        int numXiPoints = eta0.length;
        Point[][] surfacePoints = new Point[numXiPoints][numEtaPoints];

        double dXi = 1.0 / (numXiPoints - 1);
        double dEta = 1.0 / (numEtaPoints - 1);
        for (int iXi = 0; iXi < numXiPoints; iXi++) {
            double xi = iXi * dXi;
            for (int iEta = 0; iEta < numEtaPoints; iEta++) {
                double eta = iEta * dEta;
                surfacePoints[iXi][iEta] = (xi0[iEta].mul(1 - xi))
                        .add(xi1[iEta].mul(xi))
                        .add(eta0[iXi].mul(1 - eta))
                        .add(eta1[iXi].mul(eta))
                        .sub(eta0[0].mul((1 - xi) * (1 - eta)))
                        .sub(eta1[0].mul((1 - xi) * eta))
                        .sub(eta0[numXiPoints - 1].mul(xi * (1 - eta)))
                        .sub(eta1[numXiPoints - 1].mul(xi * eta));
            }
        }

        return surfacePoints;
    }
}
