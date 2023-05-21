import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BilliardSimulation2 {
    public static void main(String[] args) {
        simulateVerticalCircularBilliard(5, 0.001); // Run the simulation for 5 reflections with a deviation threshold of 0.001
        simulateVerticalCircularBilliard(4, 0.001);
        simulateVerticalCircularBilliard(7, 0.001);
    }

    public static void simulateVerticalCircularBilliard(int numReflections, double deviationThreshold) {
        Random random = new Random();

        List<double[]> reflectionPoints = new ArrayList<>();

        // Generate initial position and momentum
        double x = random.nextDouble() * 2 - 1; // Random x value between -1 and 1
        double y = Math.sqrt(1 - x * x); // Calculate y value on the unit circle
        double px = random.nextDouble() * 6 + 5; // Random momentum between 5 and 10
        double py = 0; // Since the motion is in a vertical circle, py is initially 0

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
        double xReflection = (Math.pow(y, 2) - Math.pow(x, 2)) * x - 2 * x * y * slope;
        double yReflection = -2 * x * y * x + (Math.pow(x, 2) - Math.pow(y, 2)) * y;
        return new double[]{xReflection, yReflection};
    }

    public static List<double[]> calculateStraightPath(double[] lastReflection, double[] momentum, int numReflections) {
        List<double[]> straightPath = new ArrayList<>();
        double x = lastReflection[0];
        double y = lastReflection[1];
        double px = momentum[0];
        double py = momentum[1];

        straightPath.add(lastReflection);

        for (int i = 0; i < numReflections; i++) {
            double[] reflection = calculateReflectionPoint(x, y, px, py);
            straightPath.add(reflection);
            x = reflection[0];
            y = reflection[1];
        }

        return straightPath;
    }

    public static double calculateDeviation(List<double[]> reflectionPoints, List<double[]> straightPath) {
        double deviation = 0;
        for (int i = 0; i < reflectionPoints.size(); i++) {
            double[] reflection = reflectionPoints.get(i);
            double[] straightPoint = straightPath.get(i);
            double dx = reflection[0] - straightPoint[0];
            double dy = reflection[1] - straightPoint[1];
            deviation += Math.sqrt(dx * dx + dy * dy);
        }
        return deviation;
    }
}
