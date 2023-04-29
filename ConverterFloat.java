import java.util.Arrays;

public class ConverterFloat extends Converter {
    private Spring[] integerSprings;
    private Spring[] fractionalSprings;
    private int integerLength;
    private int fractionalLength;
    private double[] frequencies;

    public ConverterFloat(String integerBits, String fractionalBits) {
        integerLength = integerBits.length();
        fractionalLength = fractionalBits.length();
        integerSprings = new Spring[integerLength];
        fractionalSprings = new Spring[fractionalLength];

        // create springs for integer part
        for (int i = 0; i < integerLength; i++) {
            char bit = integerBits.charAt(i);
            if (bit == '1') {
                integerSprings[i] = new Spring(1);
            } else {
                integerSprings[i] = new Spring(0);
            }
        }

        // create springs for fractional part
        for (int i = 0; i < fractionalLength; i++) {
            char bit = fractionalBits.charAt(i);
            if (bit == '1') {
                fractionalSprings[i] = new Spring(1);
            } else {
                fractionalSprings[i] = new Spring(0);
            }
        }
    }

    @Override
    public void connectMass(double mass) {
        // connect the mass to both systems of springs
        for (Spring spring : integerSprings) {
            spring.setMass(mass);
        }
        for (Spring spring : fractionalSprings) {
            spring.setMass(mass);
        }
    }

    @Override
    public void computeFrequencies() {
        // compute the frequency amplitudes using Fourier transform
        double[] integerAmplitudes = FT.computeAmplitudes(integerSprings);
        double[] fractionalAmplitudes = FT.computeAmplitudes(fractionalSprings);

        // combine the amplitudes for integer and fractional parts
        frequencies = new double[integerLength + fractionalLength];
        System.arraycopy(integerAmplitudes, 0, frequencies, 0, integerLength);
        System.arraycopy(fractionalAmplitudes, 0, frequencies, integerLength, fractionalLength);
    }

    @Override
    public double convertToDecimal() {
        double result = 0.0;
        double power = 1.0;

        // calculate the integer part
        for (int i = integerLength - 1; i >= 0; i--) {
            if (integerSprings[i].getCurrentDisplacement() >= 0) {
                result += power;
            }
            power *= 2;
        }

        // calculate the fractional part
        power = 0.5;
        for (int i = 0; i < fractionalLength; i++) {
            if (fractionalSprings[i].getCurrentDisplacement() >= 0) {
                result += power;
            }
            power /= 2;
        }

        return result;
    }

    @Override
    public Spring[] equivalentSpring(String bitSequence) {
        int length = bitSequence.length();
        Spring[] springs = new Spring[length];
        for (int i = 0; i < length; i++) {
            char bit = bitSequence.charAt(i);
            if (bit == '1') {
                springs[i] = new Spring(1);
            } else {
                springs[i] = new Spring(0);
            }
        }
        return springs;
    }

     public static void main(String[] args) {
      // Creating a ConverterFloat object with integer part of length 4 and fractional part of length 8
      ConverterFloat converter = new ConverterFloat(4, 8);

      // Test case 1: Binary representation of 3.75
      int[] integerBits = {0, 0, 1, 1}; // 3 in binary
      int[] fractionalBits = {1, 1, 1, 0, 0, 0, 0, 0}; // 0.75 in binary
      double result = converter.convert(integerBits, fractionalBits);
      System.out.println("Binary: " + Arrays.toString(integerBits) + "." + Arrays.toString(fractionalBits)
              + " -> Decimal: " + result); // Expected output: 3.75

      // Test case 2: Binary representation of -2.25
      int[] integerBits2 = {1, 1, 1, 1}; // -1 in two's complement binary
      int[] fractionalBits2 = {0, 1, 1, 0, 0, 0, 0, 0}; // 0.25 in binary
      double result2 = converter.convert(integerBits2, fractionalBits2);
      System.out.println("Binary: " + Arrays.toString(integerBits2) + "." + Arrays.toString(fractionalBits2)
              + " -> Decimal: " + result2); // Expected output: -2.25
  }
}
