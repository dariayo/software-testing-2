package org.tpo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.function.BiFunction;

public class CsvExporter {
    private final FunctionSystem functionSystem;
    private final String delimiter;

    public CsvExporter(FunctionSystem functionSystem, String delimiter) {
        this.functionSystem = functionSystem;
        this.delimiter = delimiter;
    }

    public void export(String filePath, double start, double end, double step, double eps) {
        exportFunction(filePath, start, end, step, eps, functionSystem::calc, "FunctionSystem");
    }

    public void exportFunction(String filePath, double start, double end, double step, double eps,
                               BiFunction<Double, Double, Double> function, String functionName) {
        try (Writer writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(delimiter.charAt(0)))) {
            csvPrinter.printRecord("X", functionName + "(X)");

            for (double x = start; x <= end; x += step) {
                try {
                    double result = function.apply(x, eps);
                    csvPrinter.printRecord(x, result);
                } catch (ArithmeticException e) {
                    csvPrinter.printRecord(x, "NaN");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи CSV файла", e);
        }
    }
}

