import java.util.Arrays;

public class ConverterInt extends Converter {
    private Spring[] springs;

    public ConverterInt(String binary) {
        int length = binary.length();
        springs = new Spring[length];
        for (int i = 0; i < length; i++) {
            int bit = Character.getNumericValue(binary.charAt(i));
            double stiffness = Math.pow(2, i) * bit;
            springs[i] = new Spring(stiffness);
        }
    }

    @Override
    public Spring getSprings() {
        Spring system = springs[0];
        for (int i = 1; i < springs.length; i++) {
            system = system.inSeries(springs[i]);
        }
        return system;
    }

    @Override
    public double[] getOscillations(double t, double dt, double x0, double v0) {
        return getSprings().move(t, dt, x0, v0);
    }

    @Override
    public double[] getFrequencyAmplitudes(double t, double dt, double x0, double v0) {
        return new FT(getOscillations(t, dt, x0, v0)).transform();
    }

    @Override
    public int getDecimalValue(double t, double dt, double x0, double v0) {
        double[] amplitudes = getFrequencyAmplitudes(t, dt, x0, v0);
        int decimalValue = 0;
        for (int i = 0; i < amplitudes.length; i++) {
            decimalValue += Math.round(amplitudes[i]) * Math.pow(2, i);
        }
        return decimalValue;
    }

    public static void main(String[] args) {
        ConverterInt converter = new ConverterInt("11011011");
        System.out.println("Binary: 11011011");
        System.out.println("Decimal: " + converter.getDecimalValue(10, 0.01, 0, 0));
        System.out.println(Arrays.toString(converter.getFrequencyAmplitudes(10, 0.01, 0, 0)));
    }
}
