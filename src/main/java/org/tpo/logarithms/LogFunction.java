package org.tpo.logarithms;

import org.tpo.functions.Calculable;

public class LogFunction implements Calculable {
    private final double arg;
    private final LnFunction lnFunction;

    public LogFunction(double eps, double arg, LnFunction lnFunction) {
        if (Double.isNaN(eps) || eps <= 0) {
            throw new IllegalArgumentException("eps must be > 0");
        }
        if (Double.isNaN(arg) || arg <= 0 || arg == 1 || Double.isInfinite(arg)) {
            throw new IllegalArgumentException("base of logarithm must be number");
        }
        this.arg = arg;
        this.lnFunction = lnFunction;
    }

    @Override
    public double calc(double x) {
        if (x <= 0 || Double.isNaN(x)) {
            throw new IllegalArgumentException("x must be a number > 0");
        }
        if (x == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        return lnFunction.calc(x) / lnFunction.calc(this.arg);
    }
}
