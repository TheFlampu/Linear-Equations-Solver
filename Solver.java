package solver;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Solver {
    private final String output;
    private final Matrix matrix;
    private final int coefficientsCount;
    private final int equationsCount;

    public Solver(String input, String output) {
        matrix = new Matrix(getEquations(input));
        this.output = output;

        coefficientsCount = matrix.getEquation(0).size();
        equationsCount = matrix.size();

        System.out.println("Start solving the equation.");
        System.out.println("Rows manipulation:");
        firstStage();

        directSolution();

        if (!matrix.hasSolution()) {
            saveSolution("No solutions");
            System.out.println("No solutions");
            return;
        }

        if (matrix.hasInfinitySolution()) {
            saveSolution("Infinitely many solutions");
            System.out.println("Infinitely many solutions");
            return;
        }

        reverseSolution();

        matrix.undoSwapsColumns();

        saveSolution("");
        System.out.println("The solution is: (" + solutionPrint(matrix.getAnswers()) + ")");
        System.out.println("Saved to file " + output);
    }

    private Equation[] getEquations(String input) {
        try {
            return Files.lines(Paths.get(input))
                    .skip(1)
                    .map(el -> new Equation(
                            Arrays.stream(el.split(" "))
                            .map(Complex::new)
                            .toArray(Complex[]::new)
                            ))
                    .toArray(Equation[]::new);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void firstStage() {
        for (int i = 0; i < coefficientsCount; i++) {
            if (i == equationsCount - 1) break;

            if (!matrix.getEquation(i).getCoefficient(i).isZero()) continue;

            boolean find = false;

            for (int j = i; j < equationsCount; j++) {
                if (matrix.getEquation(j).getCoefficient(i).isZero()) continue;
                matrix.swapRows(i, j);
                find = true;
                break;
            }

            if (find) continue;

            for (int j = i; j < coefficientsCount; j++) {
                if (matrix.getEquation(i).getCoefficient(j).isZero()) continue;
                matrix.swapColumns(i, j);
                find = true;
                break;
            }

            if (find) continue;
            if (i == coefficientsCount - 1 || i == equationsCount - 1) continue;

            for (int j = i + 1; j < equationsCount; j++) {
                for (int k = i + 1; k < coefficientsCount; k++) {
                    if (matrix.getEquation(j).getCoefficient(k).isZero()) continue;
                    matrix.swapRows(i, j);
                    matrix.swapColumns(i, k);
                    find = true;
                    break;
                }
                if (find) break;
            }

            if (!find) break;
        }
    }

    public void directSolution() {
        for (int i = 0; i < Math.min(equationsCount, coefficientsCount); i++) {
            Equation currentEquation = matrix.getEquation(i);
            String currentName = "R" + (i + 1);

            if (!currentEquation.getCoefficient(i).equals(new Complex(1, 0))) {
                if (currentEquation.getCoefficient(i).isZero()) continue;

                Complex k = new Complex(1, 0).divide(currentEquation.getCoefficient(i));

                currentEquation = currentEquation.multiplication(k);

                matrix.setEquations(i, currentEquation);

                System.out.println(k + " * " + currentName + " -> " + currentName);
            }

            for (int j = 1; j < equationsCount - i; j++) {
                Equation nextEquation = matrix.getEquation(i + j);
                if (nextEquation.getCoefficient(i).isZero()) continue;
                String nextName = "R" + (i + j + 1);

                makeZero(i, currentEquation, currentName, nextEquation, nextName);
            }
        }
    }

    public void reverseSolution() {
        for (int i = Math.min(equationsCount, coefficientsCount) - 1; i > 0 ; i--) {
            Equation currentEquation = matrix.getEquation(i);
            String currentName = "R" + (i + 1);

            for (int j = i - 1; j >= 0; j--) {
                Equation nextEquation = matrix.getEquation(j);
                if (currentEquation.getCoefficient(i).isZero()) continue;
                String nextName = "R" + (j + 1);

                makeZero(i, currentEquation, currentName, nextEquation, nextName);
            }
        }
    }

    private void makeZero(int i, Equation currentEquation, String currentName, Equation nextEquation, String nextName) {
        Complex k = nextEquation.getCoefficient(i).divide(currentEquation.getCoefficient(i));

        nextEquation.addition(currentEquation.multiplication(k.multiplication(new Complex(-1, 0))));

        System.out.println(k.multiplication(new Complex(-1, 0)) + " * " + currentName + " + " + nextName + " -> " + nextName);
    }

    public void saveSolution(String message) {
        try (FileWriter writer = new FileWriter(new File(output))) {
            if ("".equals(message)) {
                for (Complex answer : matrix.getAnswers()) {
                    writer.write(answer + "\n");
                }
            } else {
                writer.write(message);
            }
        } catch (Exception ignored) {
        }
    }

    private String solutionPrint(Complex[] answers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < answers.length; i++) {
            if (i == answers.length - 1) {
                stringBuilder.append(answers[i]);
                continue;
            }
            stringBuilder.append(answers[i]).append(", ");
        }
        return stringBuilder.toString();
    }
}
