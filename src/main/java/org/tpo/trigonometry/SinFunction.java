package org.tpo.trigonometry;

import org.tpo.functions.Calculable;

public class SinFunction implements Calculable {
    private final double eps;

    public SinFunction(double eps) {
        if (Double.isNaN(eps) || eps <= 0) {
            throw new IllegalArgumentException("eps must be > 0");
        }
        this.eps = eps;
    }

    @Override
    public double calc(double x) {
        double val = Double.MAX_VALUE;
        double nextVal = 0.0;
        int n = 1;
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x must be a number");
        }
        if (x > Math.PI || x < -Math.PI) {
            double newX = x % (2 * Math.PI);
            if (newX < -Math.PI) {
                return newX + 2 * Math.PI;
            }
            if (newX > Math.PI) {
                return newX - 2 * Math.PI;
            }
            x = newX;
        }
        while (Math.abs(val - nextVal) >= eps) {
            val = nextVal;
            nextVal += Math.pow(-1, n - 1) * Math.pow(x, 2 * n - 1) / factorial(2 * n - 1);
            n++;
        }
        return nextVal;
    }

    private static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
