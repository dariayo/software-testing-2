package org.tpo.functions;

import org.tpo.logarithms.LogFunction;
import org.tpo.trigonometry.CosFunction;
import org.tpo.trigonometry.CotFunction;
import org.tpo.trigonometry.TanFunction;

public class Function implements Calculable {
    private final CosFunction cosFunction;
    private final CotFunction cotFunction;
    private final TanFunction tanFunction;
    private final LogFunction logFunction2;
    private final LogFunction logFunction3;
    private final LogFunction logFunction10;

    public Function(CosFunction cosFunction, CotFunction cotFunction, TanFunction tanFunction, LogFunction logFunction2, LogFunction logFunction3, LogFunction logFunction10) {
        this.cosFunction = cosFunction;
        this.cotFunction = cotFunction;
        this.tanFunction = tanFunction;
        this.logFunction2 = logFunction2;
        this.logFunction3 = logFunction3;
        this.logFunction10 = logFunction10;
    }

    @Override
    public double calc(double x) {
        double result;
        if (x <= 0) {
            result = ((cosFunction.calc(x) - tanFunction.calc(x) - cotFunction.calc(x)) / tanFunction.calc(x) - tanFunction.calc(x)) + cotFunction.calc(x) * cotFunction.calc(x);
        } else {
            result = (Math.pow(Math.pow(logFunction2.calc(x), 3) / logFunction10.calc(10), 3) / (logFunction3.calc(x) / logFunction3.calc(x) / logFunction3.calc(x))) - Math.pow(logFunction3.calc(x), 2);
        }
        return result;
    }
}
