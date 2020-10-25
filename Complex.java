package solver;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex  {
    private double real = 0.0;
    private double imaginary = 0.0;

    public Complex(String str) {
        if (str.contains("i")) {
            Matcher matcher = Pattern.compile("[-+]?\\d*\\.?\\d*i").matcher(str);
            if (matcher.find()) {
                String img = matcher.group().replace("+", "");
                if (img.startsWith("i")) {
                    imaginary = 1;
                } else if (img.indexOf("i") == 1 && img.startsWith("-")) {
                    imaginary = -1;
                } else {
                    imaginary = Double.parseDouble(img.replace("i", ""));
                }
            }
            str = str.replaceAll("[-+]?\\d*\\.?\\d*i", "");
        }

        if (!str.isEmpty()) {
            real = Double.parseDouble(str);
        }
    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public boolean isZero() {
        return real == 0 && imaginary == 0;
    }

    public Complex multiplication(Complex complex) {
        double rl = real * complex.getReal() - imaginary * complex.getImaginary();
        double img = real * complex.getImaginary() + imaginary * complex.real;
        return new Complex(rl, img);
    }

    public Complex addition(Complex complex) {
        return new Complex(real + complex.getReal(), imaginary + complex.getImaginary());
    }

    public Complex divide(Complex complex) {
        double rl = (real * complex.getReal() + imaginary * complex.getImaginary()) / (complex.getReal() * complex.getReal() + complex.getImaginary() * complex.getImaginary());
        double img = (imaginary * complex.getReal() - real * complex.getImaginary()) / (complex.getReal() * complex.getReal() + complex.getImaginary() * complex.getImaginary());

        return new Complex(rl, img);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.real, real) == 0 &&
                Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public String toString()
    {
        if (real != 0 && imaginary != 0) return real + (imaginary > 0 ? "+" + imaginary : Double.toString(imaginary)) + "i";
        if (isZero()) return "0.0";
        if (real == 0) return imaginary + "i";
        return Double.toString(real);
    }
}
