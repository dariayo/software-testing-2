package org.tpo;

import lombok.AllArgsConstructor;
import org.tpo.logarithms.LogPart;
import org.tpo.trigonometry.*;

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
