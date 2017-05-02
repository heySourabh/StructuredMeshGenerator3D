package io;

import geom.Point;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class MeshFileWriter {

    private MeshFileWriter() {
    }

    public static void writeSurface(Point[][] points, String fileName) throws IOException {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        System.out.println("Writing mesh file: " + fileName);
        try (OutputStreamWriter fileWriter
                = new OutputStreamWriter(new FileOutputStream(fileName),
                        StandardCharsets.UTF_8)) {
            int numXiPoints = points.length;
            int numEtaPoints = points[0].length;

            fileWriter.write(String.format("dimension=%d\n", 2));
            fileWriter.write(String.format("xi=%d\n", numXiPoints));
            fileWriter.write(String.format("eta=%d\n", numEtaPoints));
            fileWriter.write(String.format("%-20s %-20s %-20s\n", "x", "y", "z"));
            for (int i = 0; i < numXiPoints; i++) {
                for (int j = 0; j < numEtaPoints; j++) {
                    fileWriter.write(String.format("%-20f %-20f %-20f\n", points[i][j].x, points[i][j].y, points[i][j].z));
                }
            }
        }
    }

    public static void writeMesh(Point[][][] points, String fileName) throws IOException {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        System.out.println("Writing mesh file: " + fileName);
        try (OutputStreamWriter fileWriter
                = new OutputStreamWriter(new FileOutputStream(fileName),
                        StandardCharsets.UTF_8)) {
            int numXiPoints = points.length;
            int numEtaPoints = points[0].length;
            int numZetaPoints = points[0][0].length;

            fileWriter.write(String.format("dimension=%d\n", 3));
            fileWriter.write(String.format("xi=%d\n", numXiPoints));
            fileWriter.write(String.format("eta=%d\n", numEtaPoints));
            fileWriter.write(String.format("zeta=%d\n", numZetaPoints));
            fileWriter.write(String.format("%-20s %-20s %-20s\n", "x", "y", "z"));
            for (int i = 0; i < numXiPoints; i++) {
                for (int j = 0; j < numEtaPoints; j++) {
                    for (int k = 0; k < numZetaPoints; k++) {
                        fileWriter.write(String.format("%-20f %-20f %-20f\n",
                                points[i][j][k].x, points[i][j][k].y, points[i][j][k].z));
                    }
                }
            }
        }
    }

    public static void writeSurfaceVtkFormat(Point[][] points, String fileName) throws IOException {
        if (!fileName.endsWith(".vtk")) {
            fileName += ".vtk";
        }
        System.out.println("Writing mesh file in vtk format: " + fileName);

        try (DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(fileName))) {
            int numXiPoints = points.length;
            int numEtaPoints = points[0].length;
            int numZetaPoints = 1;

            int totalNodes = numXiPoints * numEtaPoints * numZetaPoints;

            dataStream.write(("# vtk DataFile Version 2.0" + "\n").getBytes("UTF-8"));
            dataStream.write(("Structured mesh file." + "\n").getBytes("UTF-8"));
            dataStream.write(("BINARY" + "\n").getBytes("UTF-8"));

            dataStream.write(("DATASET " + "STRUCTURED_GRID" + "\n").getBytes("UTF-8"));
            dataStream.write((String.format("DIMENSIONS %d %d %d", numXiPoints, numEtaPoints, numZetaPoints) + "\n").getBytes("UTF-8"));

            dataStream.write((String.format("POINTS %d %s", totalNodes, "double") + "\n").getBytes("UTF-8"));

            for (int k = 0; k < numZetaPoints; k++) {
                for (int j = 0; j < numEtaPoints; j++) {
                    for (int i = 0; i < numXiPoints; i++) {
                        dataStream.writeDouble(points[i][j].x);
                        dataStream.writeDouble(points[i][j].y);
                        dataStream.writeDouble(points[i][j].z);
                    }
                }
            }
        }
    }

    public static void writeMeshVtkFormat(Point[][][] points, String fileName) throws IOException {
        if (!fileName.endsWith(".vtk")) {
            fileName += ".vtk";
        }
        System.out.println("Writing mesh file in vtk format: " + fileName);

        try (FileOutputStream outStream = new FileOutputStream(fileName)) {
            int numXiPoints = points.length;
            int numEtaPoints = points[0].length;
            int numZetaPoints = points[0][0].length;

            int totalNodes = numXiPoints * numEtaPoints * numZetaPoints;

            outStream.write(("# vtk DataFile Version 2.0" + "\n").getBytes("UTF-8"));
            outStream.write(("Structured mesh file." + "\n").getBytes("UTF-8"));
            outStream.write(("BINARY" + "\n").getBytes("UTF-8"));

            outStream.write(("DATASET " + "STRUCTURED_GRID" + "\n").getBytes("UTF-8"));
            outStream.write((String.format("DIMENSIONS %d %d %d", numXiPoints, numEtaPoints, numZetaPoints) + "\n").getBytes("UTF-8"));

            outStream.write((String.format("POINTS %d %s", totalNodes, "float") + "\n").getBytes("UTF-8"));

            ByteBuffer buffer = ByteBuffer.allocate(totalNodes * Float.BYTES * 3);
            for (int k = 0; k < numZetaPoints; k++) {
                for (int j = 0; j < numEtaPoints; j++) {
                    for (int i = 0; i < numXiPoints; i++) {
                        buffer.putFloat((float) points[i][j][k].x);
                        buffer.putFloat((float) points[i][j][k].y);
                        buffer.putFloat((float) points[i][j][k].z);
                    }
                }
            }
            outStream.write(buffer.array());
        }
    }
}
