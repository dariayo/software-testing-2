package org.tpo.math.logarithms;

import org.tpo.math.interfaces.LgFunctions;

public class LogPart implements LgFunctions {
    private final LnFunction ln;
    private final LogFunction log2;
    private final LogFunction log3;
    private final LogFunction log10;

    public LogPart(LnFunction ln, LogFunction log2, LogFunction log3, LogFunction log10) {
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log10 = log10;
    }

    public LogPart() {
        this.ln = new LnFunction();
        this.log2 = new LogFunction(ln, 2);
        this.log3 = new LogFunction(ln, 3);
        this.log10 = new LogFunction(ln, 10);
    }

    public double calc(double x, double eps) {
        if (x <= 0 || x == 1) {
            return Double.POSITIVE_INFINITY;
        }

        double log2Val = log2.calc(x, eps);
        double log3Val = log3.calc(x, eps);
        double log10Val = log10.calc(x, eps);

        if (Double.isNaN(log2Val) || Double.isNaN(log3Val) || Double.isNaN(log10Val)) {
            return Double.NaN;
        }
        if (log10Val == 0 || log3Val == 0) {
            return Double.NaN;
        }

        return (Math.pow(Math.pow(log2Val, 3) / log10Val, 3) / (log3Val / (log3Val / log3Val))) - Math.pow(log3Val, 2);
    }

}
