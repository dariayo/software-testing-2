package org.tpo.trigonometry;

import org.tpo.functions.Calculable;

public class CosFunction implements Calculable {
    private final SinFunction sinFunction;

    public CosFunction(double eps, SinFunction sinFunction) {
        if (Double.isNaN(eps) || eps <= 0) {
            throw new IllegalArgumentException("eps must be > 0");
        }
        this.sinFunction = sinFunction;
    }

    @Override
    public double calc(double x) {
        return sinFunction.calc(Math.PI/2 + x);
    }
}
