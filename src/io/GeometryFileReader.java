package io;

import geom.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class GeometryFileReader {

    public enum Label {
        xi0_eta0, xi0_eta1, xi1_eta0, xi1_eta1,
        eta0_zeta0, eta0_zeta1, eta1_zeta0, eta1_zeta1,
        xi0_zeta0, xi0_zeta1, xi1_zeta0, xi1_zeta1
    }

    private GeometryFileReader() {
    }

    public static Point[] readPoints(File file, Label label) throws FileNotFoundException {
        Point[] points;
        try (Scanner fileScanner = new Scanner(file, "UTF-8")) {
            int numPoints = 0;
            String nextValidLine;
            while ((nextValidLine = getNextValidLine(fileScanner)) != null) {
                if (nextValidLine.startsWith(label.toString())) {
                    String[] tokens = nextValidLine.split("[\\s:=]+");
                    try {
                        numPoints = Integer.parseInt(tokens[tokens.length - 1]);
                    } catch (NumberFormatException ex) {
                        numPoints = 0;
                    }
                    break;
                }
            }

            points = new Point[numPoints];
            for (int i = 0; i < numPoints; i++) {
                String nextLine = getNextValidLine(fileScanner);
                String[] coordinates = nextLine.split("[\\s]+");
                double x = Double.parseDouble(coordinates[0].trim());
                double y = Double.parseDouble(coordinates[1].trim());
                double z = Double.parseDouble(coordinates[2].trim());
                points[i] = new Point(x, y, z);
            }
        }

        return points;
    }

    private static String getNextValidLine(Scanner fileScanner) {
        while (fileScanner.hasNextLine()) {
            String nextLine = fileScanner.nextLine().split("#")[0].trim();
            if (!nextLine.isEmpty()) {
                return nextLine;
            }
        }

        return null;
    }
}
