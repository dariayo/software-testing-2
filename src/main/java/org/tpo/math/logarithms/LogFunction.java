package org.tpo.math.logarithms;

import org.tpo.math.interfaces.LgFunctions;

public class LogFunction implements LgFunctions {
    private final LnFunction ln;
    private final Integer base;

    public LogFunction(LnFunction ln, Integer base) {
        this.ln = ln;
        this.base = base;
    }

    public double calc(double x, double eps) {
        return ln.calc(x, eps) / ln.calc(base, eps);
    }
}
