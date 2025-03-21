package org.tpo.math;

import org.tpo.math.logarithms.LogPart;
import org.tpo.math.trigonometry.TrigPart;

public class Main {
    public static void main(String[] args) {
        FunctionSystem system = new FunctionSystem(new TrigPart(), new LogPart());
        CsvExporter exporter = new CsvExporter(system, ";");

        double start = -10;
        double end = 10;
        double step = 0.5;
        double eps = 0.001;

        exporter.export("src/main/resources/function_system.csv", start, end, step, eps);
    }
}
