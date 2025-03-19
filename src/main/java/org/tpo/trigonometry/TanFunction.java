package org.tpo.trigonometry;

import org.tpo.interfaces.TrigFunctions;

public class TanFunction implements TrigFunctions {
    private final CosFunction cos;
    private final SinFunction sin;

    public TanFunction(CosFunction cos, SinFunction sin) {
        this.cos = cos;
        this.sin = sin;
    }

    public TanFunction() {
        sin = new SinFunction();
        cos = new CosFunction(sin);
    }

    public double calc(double x, double eps) {
        double cosVal = cos.calc(x, eps);
        double sinVal = sin.calc(x, eps);

        if (Math.abs(cosVal) <= eps) {
            return sinVal > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return sinVal / cosVal;
    }
}
