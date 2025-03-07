package org.tpo.trigonometry;

import org.tpo.functions.Calculable;

public class TanFunction implements Calculable {
    private final double eps;
    private final SinFunction sinFunction;
    private final CosFunction cosFunction;

    public TanFunction(double eps, SinFunction sinFunction) {
        this.eps = eps;
        this.sinFunction = sinFunction;
        this.cosFunction = new CosFunction(eps, sinFunction);
    }

    @Override
    public double calc(double x) {
        double cosX = cosFunction.calc(x);
        if (Math.abs(cosX) < eps) {
            return Double.NaN;
        }
        return sinFunction.calc(x) / cosX;
    }
}
