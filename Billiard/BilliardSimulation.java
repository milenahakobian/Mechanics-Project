import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BilliardSimulation {
    public static void main(String[] args) {
        simulateBilliard(5, 0.001); // simulation for 5 reflections with a deviation threshold delta = 0.001
        simulateBilliard(5, 0.001); // simulation for 4 reflections with a deviation threshold delta = 0.001
        simulateBilliard(10, 0.001); // simulation for 10 reflections with a deviation threshold delta = 0.001
    }

    public static void simulateBilliard(int numReflections, double deviationThreshold) {
        Random random = new Random();

        List<double[]> reflectionPoints = new ArrayList<>();

        // Generate initial position and momentum
        double x = random.nextDouble() * 2 - 1;
        double y = random.nextDouble() * 2 - 1;
        double px, py;

        do {
            px = random.nextDouble() * 2 - 1;
            py = random.nextDouble() * 2 - 1;
        } while (px * px + py * py > 1);

        double norm = Math.sqrt(px * px + py * py);
        px /= norm;
        py /= norm;

        double[] initialPosition = {x, y};
        double[] initialMomentum = {px, py};

        for (int i = 0; i < numReflections; i++) {
            double[] lastReflection = calculateReflectionPoint(x, y, px, py);
            reflectionPoints.add(lastReflection);
            x = lastReflection[0];
            y = lastReflection[1];

            double pxReflection = (y * y - x * x) * px - 2 * x * y * py;
            double pyReflection = -2 * x * y * px + (x * x - y * y) * py;

            px = pxReflection;
            py = pyReflection;
        }

        // Print the coordinates of all reflection points
        System.out.println("Reflection Points:");
        for (double[] point : reflectionPoints) {
            System.out.println("(" + point[0] + ", " + point[1] + ")");
        }

        // Reverse the momentum after n reflections
        double[] reversedMomentum = {-px, -py};

        // Calculate the straight path starting from the last reflection point
        List<double[]> straightPath = calculateStraightPath(reflectionPoints.get(numReflections - 1), reversedMomentum, numReflections);

        // Check deviation between the straight path and the reversed path
        double deviation = calculateDeviation(reflectionPoints, straightPath);

        // Determine after how many reflections the paths deviate more than the specified threshold
        int deviationIndex = -1;
        for (int i = 0; i < reflectionPoints.size(); i++) {
            if (calculateDeviation(reflectionPoints.subList(0, i + 1), straightPath.subList(0, i + 1)) > deviationThreshold) {
                deviationIndex = i;
                break;
            }
        }

        if (deviationIndex == -1) {
            System.out.println("The reversed path coincides with the straight path.");
        } else {
            System.out.println("The paths deviate more than the specified threshold after " + deviationIndex + " reflections.");
        }
    }

    public static double[] calculateReflectionPoint(double x, double y, double px, double py) {
    double slope = py / px;
    double xReflection = (y * y - x * x) / (x * x + y * y);
    double yReflection = slope * xReflection;
    return new double[]{xReflection, yReflection};
}


    public static List<double[]> calculateStraightPath(double[] startPoint, double[] momentum, int numReflections) {
        List<double[]> path = new ArrayList<>();
        path.add(startPoint);

        double x = startPoint[0];
        double y = startPoint[1];
        double px = momentum[0];
        double py = momentum[1];

        for (int i = 0; i < numReflections; i++) {
            double[] reflection = calculateReflectionPoint(x, y, px, py);
            path.add(reflection);

            x = reflection[0];
            y = reflection[1];
        }

        return path;
    }

    public static double calculateDeviation(List<double[]> path1, List<double[]> path2) {
        double deviation = 0.0;

        int minLength = Math.min(path1.size(), path2.size());

        for (int i = 0; i < minLength; i++) {
            double[] point1 = path1.get(i);
            double[] point2 = path2.get(i);

            double dx = point1[0] - point2[0];
            double dy = point1[1] - point2[1];

            deviation += Math.sqrt(dx * dx + dy * dy);
        }

        return deviation / minLength;
    }
}
