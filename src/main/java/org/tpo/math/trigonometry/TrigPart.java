package org.tpo.math.trigonometry;

import lombok.AllArgsConstructor;
import org.tpo.math.interfaces.TrigFunctions;

@AllArgsConstructor
public class TrigPart implements TrigFunctions {
    private final CosFunction cos;
    private final TanFunction tan;
    private final CotFunction cot;

    public TrigPart() {
        this.cos = new CosFunction();
        this.tan = new TanFunction();
        this.cot = new CotFunction();
    }

    public double calc(double x, double eps) {
        double cosVal = cos.calc(x, eps);
        double tanVal = tan.calc(x, eps);
        double cotVal = cot.calc(x, eps);
        if (tanVal == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return ((cosVal - tanVal - cotVal) / tanVal) - tanVal + cotVal * cotVal;
    }
}
