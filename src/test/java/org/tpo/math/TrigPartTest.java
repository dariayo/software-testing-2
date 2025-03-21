package org.tpo.math;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tpo.math.trigonometry.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrigPartTest {
    public static SinFunction sin = mock(SinFunction.class);
    public static CosFunction cos = mock(CosFunction.class);
    public static CotFunction cot = mock(CotFunction.class);
    public static TanFunction tan = mock(TanFunction.class);
    public static CosFunction cosReal = new CosFunction(new SinFunction());
    public static CotFunction cotReal = new CotFunction(new CosFunction(new SinFunction()), new SinFunction());
    public static TanFunction tanReal = new TanFunction(new CosFunction(new SinFunction()), new SinFunction());

    private static final double eps = 1e-9;
    private final double percentage = 0.01;

    @BeforeAll
    public static void setUpMocks() {
        String[] fileNames = {
                "src/test/resources/org/tpo/math/trigFuncCsv/Sin.csv",
                "src/test/resources/org/tpo/math/trigFuncCsv/Cos.csv",
                "src/test/resources/org/tpo/math/trigFuncCsv/Cot.csv",
                "src/test/resources/org/tpo/math/trigFuncCsv/Tan.csv"
        };
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = line.split(", ");

                    double x = Double.parseDouble(a[0]);
                    double y = Double.parseDouble(a[1]);

                    if (fileName.contains("Cos")) {
                        when(cos.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Tan")) {
                        when(tan.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Sin")) {
                        when(sin.calc(x, eps)).thenReturn(y);
                    } else if (fileName.contains("Cot")) {
                        when(cot.calc(x, eps)).thenReturn(y);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/TrigPart.csv")
    void trigTestMock(Double x, Double expected) {
        TrigPart tfc = new TrigPart(cos, tan, cot);
        double result = tfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/TrigPart.csv")
    void trigTestCosReal(Double x, Double expected) {
        TrigPart tfc = new TrigPart(cosReal, tan, cot);
        double result = tfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/TrigPart.csv")
    void trigTestTanReal(Double x, Double expected) {
        TrigPart tfc = new TrigPart(cos, tanReal, cot);
        double result = tfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/TrigPart.csv")
    void trigTestCotReal(Double x, Double expected) {
        TrigPart tfc = new TrigPart(cos, tan, cotReal);
        double result = tfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/tpo/math/trigFuncCsv/TrigPart.csv")
    void trigTestAllReal(Double x, Double expected) {
        TrigPart tfc = new TrigPart(cosReal, tanReal, cotReal);
        double result = tfc.calc(x, eps);
        assertEquals(expected, result, Math.abs(result * percentage));

    }
}
