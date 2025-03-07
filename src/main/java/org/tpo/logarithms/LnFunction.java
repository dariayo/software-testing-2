package org.tpo.logarithms;

import org.tpo.functions.Calculable;

public class LnFunction implements Calculable {
    private final double eps;

    public LnFunction(double eps) {
        if (Double.isNaN(eps) || eps <= 0) {
            throw new IllegalArgumentException("eps must be > 0");
        }
        this.eps = eps;
    }

    @Override
    public double calc(double x) {
        if (x <= 0 || Double.isNaN(x)) {
            throw new IllegalArgumentException("x must be a number > 0");
        }
        if (x == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (x > 2) {
            return calc(x / 2.0) + calc(2.0);
        }
        double nextVal = x - 1;
        double currVal = Double.MAX_VALUE;
        int n = 1;

        while (Math.abs(currVal - nextVal) > eps) {
            currVal = nextVal;
            nextVal += Math.pow(-1, n) * (Math.pow(x - 1, n + 1) / (n + 1));
            n++;
        }

        return currVal;
    }

}
