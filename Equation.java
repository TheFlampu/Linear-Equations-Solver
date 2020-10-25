package solver;

import java.util.Arrays;

public class Equation {
    private Complex[] coefficients;

    public Equation(Complex[] coefficients) {
        this.coefficients = coefficients;
    }

    public Complex getCoefficient(int i) {
        return coefficients[i];
    }

    public Complex getAnswer() {
        return coefficients[coefficients.length - 1];
    }

    public int size() {
        return coefficients.length - 1;
    }

    public void swapColumns(int from, int to) {
        Complex tmp = getCoefficient(from);
        coefficients[from] = getCoefficient(to);
        coefficients[to] = tmp;
    }

    public boolean hasSolution() {
        long countNumber = Arrays.stream(coefficients).limit(coefficients.length - 1).filter(el -> !el.isZero()).count();

        return countNumber == 0 && !getAnswer().isZero();
    }

    public Equation multiplication(Complex num) {
        return new Equation(Arrays.stream(coefficients).map(el -> el.multiplication(num)).toArray(Complex[]::new));
    }

    public void addition(Equation equation) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = coefficients[i].addition(equation.getCoefficient(i));
        }
    }
}
