package main;

import mesh.TransfiniteInterpolation;
import geom.Geometry;
import geom.Point;
import geom.SimpleHexahedralGeom;
import io.MeshFileWriter;
import java.io.IOException;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class StructuredMeshGenerator3D {

    public static void main(String[] args) throws IOException {
        Geometry geom = new SimpleHexahedralGeom(20, 30, 15,
                new Point(0, 0, 0), new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0),
                new Point(0, 0, 1), new Point(1, 0, 1), new Point(1, 1, 1), new Point(0, 1, 1));

        Point[][][] points = TransfiniteInterpolation.interpolate(geom);
        MeshFileWriter.writeMeshVtkFormat(points, "mesh");
    }
}
