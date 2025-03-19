package org.tpo.math;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tpo.FunctionSystem;
import org.tpo.logarithms.LnFunction;
import org.tpo.logarithms.LogFunction;
import org.tpo.logarithms.LogPart;
import org.tpo.trigonometry.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FunctionSystemTest {
    private final double percentage = 0.001;
    private static final double eps = 1e-9;
    public static TrigPart trigPart = mock(TrigPart.class);
    public static LogPart logPart = mock(LogPart.class);

    public TrigPart trigPartReal = new TrigPart(
            new CosFunction(new SinFunction()), new TanFunction(new CosFunction(new SinFunction()), new SinFunction()),
            new CotFunction(new CosFunction(new SinFunction()), new SinFunction())
    );
    public LogPart logPartReal = new LogPart(
            new LnFunction(), new LogFunction(new LnFunction(), 2), new LogFunction(new LnFunction(), 3), new LogFunction(new LnFunction(), 10)
    );

    @BeforeAll
    public static void setUpMocks() {
        String[] fileNames = {
                "src/test/resources/org/tpo/math/trigFuncCsv/TrigPart.csv",
                "src/test/resources/org/tpo/math/logFuncCsv/LogPart.csv"
        };
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = line.split(",");

                    double x = Double.parseDouble(a[0]);
                    double y = Double.parseDouble(a[1]);

                    if (fileName.contains("LogPart")) {
                        when(logPart.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("TrigPart")) {
                        when(trigPart.calc(x, eps)).thenReturn(y);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/System.csv")
    void allMock(Double x, Double trueResult) {

        FunctionSystem calculator = new FunctionSystem(trigPart, logPart);
        double result = calculator.calc(x, eps);
        assertEquals(trueResult, result, Math.abs(result * percentage));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/System.csv")
    void testTrigReal(Double x, Double trueResult) {

        FunctionSystem calculator = new FunctionSystem(trigPartReal, logPart);
        double result = calculator.calc(x, eps);
        assertEquals(trueResult, result, Math.abs(result * percentage));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/System.csv")
    void testLogReal(Double x, Double trueResult) {

        FunctionSystem calculator = new FunctionSystem(trigPart, logPartReal);
        double result = calculator.calc(x, eps);
        assertEquals(trueResult, result, Math.abs(result * percentage));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/System.csv")
    void allReal(Double x, Double trueResult) {

        FunctionSystem calculator = new FunctionSystem(trigPartReal, logPartReal);
        double result = calculator.calc(x, eps);
        assertEquals(trueResult, result, Math.abs(result * percentage));
    }

    public double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public double log3(double x) {
        return Math.log(x) / Math.log(3);
    }

    public double func(double x) {
        if (x <= 0) {
            return (Math.cos(x) - Math.tan(x) - 1 / Math.tan(x)) / Math.tan(x) - Math.tan(x) + 1 / Math.tan(x) * 1 / Math.tan(x);
        }
        double result = Math.pow(Math.pow(log2(x), 3) / Math.log10(x), 3) / (log3(x) / (log3(x) / log3(x))) - Math.pow(log3(x), 2);
        if (Double.isNaN(result)){
            return Double.POSITIVE_INFINITY;
        }
        return result;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/System.csv")
    void testWithCorrectFunc(Double x) {

        FunctionSystem calculator = new FunctionSystem(trigPartReal, logPartReal);
        double result = calculator.calc(x, eps);
        double expected = func(x);
        assertEquals(expected, result, Math.abs(result * percentage));
    }
}
