import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StadiumBilliardSimulation {
    private double L; // Length of the straight lines
    private double r; // Radius of the circle
    private int M; // Number of bins for testing uniformity

    public StadiumBilliardSimulation(double L, double r, int M) {
        this.L = L;
        this.r = r;
        this.M = M;
    }

    public List<double[]> simulateStadiumBilliard(double[] startingPoint, double[] momentum, int numReflections) {
        List<double[]> reflectionPoints = new ArrayList<>();
        double x = startingPoint[0];
        double y = startingPoint[1];
        double px = momentum[0];
        double py = momentum[1];

        reflectionPoints.add(new double[]{x, y});

        for (int i = 0; i < numReflections; i++) {
            // Determine the side the particle will hit and calculate the next position
            int side = calculateReflection(x, y, px, py);
            double[] nextPoint = calculateNextPoint(x, y, px, py, side);
            x = nextPoint[0];
            y = nextPoint[1];

            // Save the reflection point
            reflectionPoints.add(new double[]{x, y});
        }

        return reflectionPoints;
    }

    private double[] calculateNextPoint(double x, double y, double px, double py, int side) {
        double newX, newY;

        if (side == 0) {
            // Reflection off the top line segment
            newX = x + (2 * r - y) * px / py;
            newY = 2 * r - y;
            return new double[]{newX, newY};
        } else if (side == 1) {
            // Reflection off the bottom line segment
            newX = x - y * px / py;
            newY = -y;
            return new double[]{newX, newY};
        } else if (side == 2) {
            // Reflection off the left semicircle
            double xc = 0; // x-coordinate of the center of the left semicircle
            newX = (y * y - (x - xc) * (x - xc)) * px - 2 * (x - xc) * y * py;
            newY = -2 * (x - xc) * y * px + ((x - xc) * (x - xc) - y * y) * py;
            return new double[]{newX, newY};
        }

        // Default case (no reflection)
        return new double[]{x, y};
    }

    private int calculateReflection(double x, double y, double px, double py) {
        if (y >= r && y <= 2 * r) {
            // Particle hits the top line segment
            return 0;
        } else if (y >= -2 * r && y <= -r) {
            // Particle hits the bottom line segment
            return 1;
        } else if (x <= 0) {
            // Particle hits the left semicircle
            return 2;
        } else if (x >= L) {
            // Particle hits the right semicircle
            return 3;
        }

        // Default case (no reflection)
        return -1;
    }

    public void testUniformity(List<double[]> reflectionPoints) {
        int[] binCounts = new int[M];

        for (double[] point : reflectionPoints) {
            double x = point[0];
            int bin = (int) (x * M);
            binCounts[bin]++;
        }

        // Check for approximate equality of bin counts
        // Compute the mean and variance of the number of entries in the bins
        // ...

    }

    public static void main(String[] args) {
        // Specify the parameters for the stadium billiard simulation
        double L = 1.0; // Length of the straight lines
        double r = 1.0; // Radius of the circle
        int M = 10; // Number of bins for testing uniformity

        // Specify the initial conditions
        double[] startingPoint = {0.5, 0.5}; // Initial position inside the circle
        double[] momentum = {1.0, 1.0}; // Initial momentum (|p| = 1)

        int numReflections = 10; // Number of reflections

        // Create an instance of the StadiumBilliardSimulation class
        StadiumBilliardSimulation simulation = new StadiumBilliardSimulation(L, r, M);

        // Run the simulation and save the coordinates of all reflection points
        List<double[]> reflectionPoints = simulation.simulateStadiumBilliard(startingPoint, momentum, numReflections);

        // Test the uniformity of the reflection points
        simulation.testUniformity(reflectionPoints);

        // Print the reflection points
        for (double[] point : reflectionPoints) {
            System.out.println("(" + point[0] + ", " + point[1] + ")");
        }
    }
}
