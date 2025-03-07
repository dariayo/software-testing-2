package org.tpo.trigonometry;

import org.tpo.functions.Calculable;

public class CotFunction implements Calculable {
    private final SinFunction sinFunction;
    private final CosFunction cosFunction;

    public CotFunction(double eps, SinFunction sinFunction) {
        if (Double.isNaN(eps) || eps <= 0) {
            throw new IllegalArgumentException("eps must be > 0");
        }
        this.sinFunction = sinFunction;
        cosFunction = new CosFunction(eps, sinFunction);
    }

    public double calc(double x) {
        if (x % Math.PI == 0) {
            return Double.NaN;
        }
        return cosFunction.calc(x) / sinFunction.calc(x);
    }
}
