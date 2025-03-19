package org.tpo.math;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tpo.logarithms.LnFunction;
import org.tpo.logarithms.LogFunction;
import org.tpo.logarithms.LogPart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogFunctionsTest {
    public static LnFunction ln = mock(LnFunction.class);
    public static LogFunction log2 = mock(LogFunction.class);
    public static LogFunction log3 = mock(LogFunction.class);
    public static LogFunction log10 = mock(LogFunction.class);

    public static LnFunction lnReal = new LnFunction();
    public static LogFunction log2Real = new LogFunction(new LnFunction(), 2);
    public static LogFunction log3Real = new LogFunction(new LnFunction(), 3);
    public static LogFunction log10Real = new LogFunction(new LnFunction(), 10);

    private static final double eps = 1e-9;
    private final double percentage = 0.01;

    @BeforeAll
    public static void setUpMocks() {
        String[] fileNames = {
                "src/test/resources/org/tpo/math/logFuncCsv/Ln.csv",
                "src/test/resources/org/tpo/math/logFuncCsv/Log2.csv",
                "src/test/resources/org/tpo/math/logFuncCsv/Log3.csv",
                "src/test/resources/org/tpo/math/logFuncCsv/Log10.csv"
        };
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = line.split(", ");

                    double x = Double.parseDouble(a[0]);
                    double y = Double.parseDouble(a[1]);

                    if (fileName.contains("Ln")) {
                        when(ln.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Log2")) {
                        when(log2.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Log3")) {
                        when(log3.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Log10")) {
                        when(log10.calc(x, eps)).thenReturn(y);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/logFuncCsv/LogPart.csv")
    void LogMocks(Double x, Double expected) {
        LogPart lfc = new LogPart(ln, log2, log3, log10);
        double result = lfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/logFuncCsv/LogPart.csv")
    void LogRealLn(Double x, Double expected) {
        LogPart lfc = new LogPart(lnReal, log2, log3, log10);
        double result = lfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/logFuncCsv/LogPart.csv")
    void LogRealLog2(Double x, Double expected) {
        LogPart lfc = new LogPart(ln, log2Real, log3, log10);
        double result = lfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/logFuncCsv/LogPart.csv")
    void LogRealLog3(Double x, Double expected) {
        LogPart lfc = new LogPart(ln, log2, log3Real, log10);
        double result = lfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/logFuncCsv/LogPart.csv")
    void LogRealAll(Double x, Double expected) {
        LogPart lfc = new LogPart(lnReal, log2Real, log3Real, log10Real);
        double result = lfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));
    }

}
