package main;

import mesh.TransfiniteInterpolation;
import geom.Geometry;
import geom.Point;
import geom.HexahedronGeom;
import io.MeshFileWriter;
import java.io.IOException;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class StructuredMeshGenerator3D {

    public static void main(String[] args) throws IOException {
        Geometry geom = new HexahedronGeom(100, 10, 10,
                new Point(0, 0, 0), new Point(1, 0, 0), new Point(1, 0.1, 0), new Point(0, 0.1, 0),
                new Point(0, 0, 0.1), new Point(1, 0, 0.1), new Point(1, 0.1, 0.1), new Point(0, 0.1, 0.1));

        Point[][][] points = TransfiniteInterpolation.interpolate(geom);
        MeshFileWriter.writeMeshVtkFormat(points, "mesh");
    }
}
