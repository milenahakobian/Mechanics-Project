public abstract class Converter {
    protected Spring[] systemOfSprings;
    protected double[] amplitudes;

    protected abstract Spring[] bitsToSprings(int[] bits);

    public void connectBody(double t, double dt, double x0, double v0) {
        double[] x = new double[systemOfSprings.length];
        double[] v = new double[systemOfSprings.length];

        for (int i = 0; i < systemOfSprings.length; i++) {
            x[i] = systemOfSprings[i].move(t, dt, x0, v0)[0];
            v[i] = systemOfSprings[i].move(t, dt, x0, v0)[1];
        }

        amplitudes = new FT(x).transform();
    }

    public abstract double evaluateDecimalValue(int[] bits);
}
