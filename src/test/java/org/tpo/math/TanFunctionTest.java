package org.tpo.math;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tpo.math.trigonometry.CosFunction;
import org.tpo.math.trigonometry.SinFunction;
import org.tpo.math.trigonometry.TanFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TanFunctionTest {
    public static CosFunction cos = mock(CosFunction.class);
    public static CosFunction cosReal = new CosFunction();
    public static SinFunction sin = mock(SinFunction.class);
    public static SinFunction sinReal = new SinFunction();

    private static final double eps = 1e-9;
    private final double accuracy = 0.001;

    @BeforeAll
    public static void setUpMocks() {
        String[] fileNames = {
                "src/test/resources/org/tpo/math/trigFuncCsv/Sin.csv",
                "src/test/resources/org/tpo/math/trigFuncCsv/Cos.csv"
        };
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = line.split(", ");

                    double x = Double.parseDouble(a[0]);
                    double y = Double.parseDouble(a[1]);

                    if (fileName.contains("Sin")) {
                        when(sin.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Cos")) {
                        when(cos.calc(x, eps)).thenReturn(y);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/Tan.csv")
    void testWithMock(Double x, Double expected) {
        TanFunction tan = new TanFunction(cos, sin);
        double result = tan.calc(x, eps);
        assertEquals(expected, result, accuracy);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/Tan.csv")
    void testWithReal(Double x, Double expected) {
        TanFunction tan = new TanFunction(cosReal, sinReal);
        double result = tan.calc(x, eps);
        assertEquals(expected, result, accuracy);
    }
}
