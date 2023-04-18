public class Spring {
    private double k = 1.0; // default stiffness value

    //the default constructor
    public Spring() {}

    //overloaded constructor
    public Spring(double k) {
        setStiffness(k);
    }
   
    //accessor and mutator methods
    public void setStiffness(double k) {
        this.k = k;
    }

    public double getStiffness() {
        return k;
    }

    public double[] move(double t, double dt, double x0, double v0) {
        int numSteps = (int) (t / dt);
        double[] coords = new double[numSteps];
        double x = x0;
        double v = v0;

        for (int i = 0; i < numSteps; i++) {
            double F = -k * x;
            double a = F;
            v += a * dt;
            x += v * dt;
            coords[i] = x;
        }

        return coords;
    }

    public double[] move(double t, double dt, double x0) {
        return move(t, dt, x0, 0);
    }

    public double[] move(double t0, double t1, double dt, double x0, double v0) {
        int numSteps = (int) ((t1 - t0) / dt);
        double[] coords = new double[numSteps];
        double x = x0;
        double v = v0;

        for (int i = 0; i < numSteps; i++) {
            double F = -k * x;
            double a = F;
            v += a * dt;
            x += v * dt;
            coords[i] = x;
        }

        return coords;
    }

    public double[] move(double t0, double t1, double dt, double x0, double v0, double m) {
        int numSteps = (int) ((t1 - t0) / dt);
        double[] coords = new double[numSteps];
        double x = x0;
        double v = v0;

        for (int i = 0; i < numSteps; i++) {
            double F = -k * x;
            double a = F / m;
            v += a * dt;
            x += v * dt;
            coords[i] = x;
        }

        return coords;
    }
}
