package org.tpo.trigonometry;

import org.tpo.interfaces.TrigFunctions;

import static java.lang.Math.PI;

public class SinFunction implements TrigFunctions {
    public double calc(double x, double eps) {
        x = x % (2 * PI);

        if (Double.compare(x, 0D) == 0) {
            return 0.0;
        }
        if (Double.compare(x, PI / 2) == 0) {
            return 1.0;
        }
        if (Double.compare(x, -PI / 2) == 0) {
            return -1.0;
        }

        int n = 0;
        double result = 0;
        double pow = x;
        double fact = 1;
        int sign = 1;
        double term = x;

        while (Math.abs(term) > eps) {
            result += term;

            sign = -sign;
            fact *= (2 * n + 2) * (2 * n + 3);
            pow *= x * x;
            n += 1;
            term = sign * pow / fact;
        }

        return result;

    }
}
