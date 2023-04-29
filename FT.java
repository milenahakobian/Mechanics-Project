import java.util.Arrays;

public class FT {
    private int N; // number of samples
    private double[] x; // array of sample values
    private double[] re; // array of real parts of Fourier coefficients
    private double[] im; // array of imaginary parts of Fourier coefficients
    
    public FT(double[] x) {
        this.N = x.length;
        this.x = x.clone();
        this.re = new double[N];
        this.im = new double[N];
        for (int k = 0; k < N; k++) {
            for (int n = 0; n < N; n++) {
                double angle = 2 * Math.PI * k * n / N;
                re[k] += x[n] * Math.cos(angle);
                im[k] -= x[n] * Math.sin(angle);
            }
        }
    }
    
    public double[] amplitudeSpectrum() {
        double[] amplitude = new double[N/2+1];
        amplitude[0] = Math.abs(re[0]) / N;
        for (int k = 1; k <= N/2; k++) {
            amplitude[k] = Math.sqrt(re[k]*re[k] + im[k]*im[k]) / N * 2;
        }
        return amplitude;
    }
    
    public double[] phaseSpectrum() {
        double[] phase = new double[N/2+1];
        phase[0] = 0;
        for (int k = 1; k <= N/2; k++) {
            phase[k] = Math.atan2(im[k], re[k]);
        }
        return phase;
    }
    
    public double[] inverseTransform() {
        double[] y = new double[N];
        for (int n = 0; n < N; n++) {
            for (int k = 0; k < N; k++) {
                double angle = 2 * Math.PI * k * n / N;
                y[n] += re[k] * Math.cos(angle) - im[k] * Math.sin(angle);
            }
            y[n] /= N;
        }
        return y;
    }
    
    public static double[] fourierSeries(double[] amplitude, double[] phase, int N) {
        double[] x = new double[N];
        for (int n = 0; n < N; n++) {
            for (int k = 1; k <= amplitude.length-1; k++) {
                double angle = 2 * Math.PI * k * n / N;
                x[n] += amplitude[k] * Math.cos(angle + phase[k]);
            }
            x[n] += amplitude[0];
        }
        return x;
    }
}
