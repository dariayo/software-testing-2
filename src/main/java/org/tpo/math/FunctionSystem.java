package org.tpo.math;

import lombok.AllArgsConstructor;
import org.tpo.math.logarithms.LogPart;
import org.tpo.math.trigonometry.TrigPart;

@AllArgsConstructor
public class FunctionSystem {
    private final TrigPart trigPart;
    private final LogPart logPart;

    public double calc(double x, double eps) {
        if (x <= 0) {
            return trigPart.calc(x, eps);
        } else {
            return logPart.calc(x, eps);
        }
    }
}
