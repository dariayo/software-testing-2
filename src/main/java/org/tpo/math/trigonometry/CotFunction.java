package org.tpo.math.trigonometry;

import org.tpo.math.interfaces.TrigFunctions;

public class CotFunction implements TrigFunctions {
    private final CosFunction cos;
    private final SinFunction sin;

    public CotFunction(CosFunction cos, SinFunction sin) {
        this.cos = cos;
        this.sin = sin;
    }

    public CotFunction() {
        sin = new SinFunction();
        cos = new CosFunction(sin);
    }

    public double calc(double x, double eps) {
        double cosVal = cos.calc(x, eps);
        double sinVal = sin.calc(x, eps);

        if (Math.abs(sinVal) <= eps) {
            return cosVal > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return cosVal / sinVal;
    }
}
