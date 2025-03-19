package org.tpo.logarithms;

import org.tpo.interfaces.LgFunctions;

public class LnFunction implements LgFunctions {
    public double calc(double x, double eps) {
        if (Double.isNaN(x) || x <= 0 || Double.isInfinite(x)) {
            throw new ArithmeticException("Логарифм не определен");
        }
        double currVal = (x - 1) / (x + 1);
        double y = currVal;
        double result = 0;
        double previousResult = -1;
        int step = 1;

        while (Math.abs(result - previousResult) > eps) {
            previousResult = result;
            result += y / step;
            y = y * currVal * currVal;
            step += 2;
        }
        return 2 * result;

    }
}
