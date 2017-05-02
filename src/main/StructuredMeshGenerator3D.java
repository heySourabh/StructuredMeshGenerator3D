package main;

import mesh.TransfiniteInterpolation;
import geom.Geometry;
import geom.GeometryFromFile;
import geom.Point;
import geom.HexahedronGeom;
import io.MeshFileWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class StructuredMeshGenerator3D {

    public static void main(String[] args) throws IOException {
//        Geometry geom = new HexahedronGeom(100, 10, 10,
//                new Point(0, 0, 0), new Point(1, 0, 0), new Point(1, 0.1, 0), new Point(0, 0.1, 0),
//                new Point(0, 0, 0.1), new Point(1, 0, 0.1), new Point(1, 0.1, 0.1), new Point(0, 0.1, 0.1));

        Geometry geom = new GeometryFromFile(new File("geom.dat"), 100, 50, 50);

        Point[][][] points = TransfiniteInterpolation.interpolate(geom);
        MeshFileWriter.writeMesh(points, "mesh");
    }
}
