package org.tpo.math;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tpo.math.trigonometry.CosFunction;
import org.tpo.math.trigonometry.SinFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CosFunctionTest {
    public static SinFunction sin = mock(SinFunction.class);
    public static SinFunction sinReal = new SinFunction();

    private static final double eps = 1e-9;
    private final double accuracy = 0.001;

    @BeforeAll
    public static void setUpMocks() {
        String fileName = "src/test/resources/org/tpo/math/trigFuncCsv/Sin.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = line.split(", ");

                double x = Double.parseDouble(a[0]);
                double y = Double.parseDouble(a[1]);

                when(sin.calc(x, eps)).thenReturn(y);
            }
        } catch (IOException ignored) {

        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/trigFuncCsv/Cos.csv")
    void testMock(Double x, Double expected) {
        CosFunction cos = new CosFunction(sin);
        double res = cos.calc(x, eps);
        assertEquals(expected, res, accuracy);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "../math/trigFuncCsv/Cos.csv")
    void testRealValues(Double x, Double expected) {
        CosFunction cos = new CosFunction(sinReal);
        double result = cos.calc(x, eps);
        assertEquals(expected, result, accuracy);
    }

}
