package org.tpo;

import org.tpo.functions.Calculable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CsvWriter {
    public static void write(List<Calculable> functions, double step) {
        String filePath = "res.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Шаг,Значение,Функция");
            writer.newLine();

            for (Calculable function : functions) {
                double currentStep = step;
                for (int j = 0; j < 10; j++) {
                    String line = String.format(Locale.US,"%.5f,%.5f,%s",
                            currentStep,
                            function.calc(currentStep),
                            function.getClass().getSimpleName());
                    writer.write(line);
                    writer.newLine();

                    currentStep += step;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
