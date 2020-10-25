package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Matrix {
    private final Equation[] equations;
    private final ArrayList<Swap> swaps;

    public Matrix(Equation[] equations) {
        this.equations = equations;
        swaps = new ArrayList<>();
    }

    public Equation getEquation(int i) {
        return equations[i];
    }

    public void setEquations(int i, Equation equation) {
        equations[i] = equation;
    }

    public int size() {
        return equations.length;
    }

    public void swapRows(int from, int to) {
        System.out.printf("R%d <-> R%d\n", from + 1, to + 1);
        Equation tmp = getEquation(from);
        equations[from] = getEquation(to);
        equations[to] = tmp;
    }

    public void swapColumns(int from, int to) {
        System.out.printf("C%d <-> C%d\n", from + 1, to + 1);
        for (Equation equation : equations) {
            equation.swapColumns(from, to);
        }
        swaps.add(new Swap(from, to));
    }

    public void undoSwapsColumns() {
        Collections.reverse(swaps);
        for (Swap swap : swaps) {
            for (Equation equation : equations) {
                equation.swapColumns(swap.getFrom(), swap.getTo());
            }
        }
    }

    public boolean hasSolution() {
        for (Equation equation : equations) {
            if (equation.hasSolution()) return false;
        }
        return true;
    }

    public boolean hasInfinitySolution() {
        return size() < equations[0].size() || equations[equations[0].size() - 1].getCoefficient(equations[0].size() - 1).isZero();
    }

    public Complex[] getAnswers() {
        return Arrays.stream(equations).limit(equations[0].size()).map(Equation::getAnswer).toArray(Complex[]::new);
    }
}
