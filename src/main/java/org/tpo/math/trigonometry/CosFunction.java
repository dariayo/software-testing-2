package org.tpo.math.trigonometry;

import org.tpo.math.interfaces.TrigFunctions;

import static java.lang.Math.PI;

public class CosFunction implements TrigFunctions {
    private final SinFunction sin;

    public CosFunction(SinFunction sin) {
        this.sin = sin;
    }

    public CosFunction() {
        sin = new SinFunction();
    }

    public double calc(double x, double eps) {
        x = x % (2 * PI);
        return sin.calc(x + PI / 2, eps);

    }
}
