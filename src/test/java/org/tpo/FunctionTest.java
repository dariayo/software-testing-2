package org.tpo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.tpo.functions.Function;
import org.tpo.logarithms.LnFunction;
import org.tpo.logarithms.LogFunction;
import org.tpo.trigonometry.CosFunction;
import org.tpo.trigonometry.CotFunction;
import org.tpo.trigonometry.SinFunction;
import org.tpo.trigonometry.TanFunction;

import static org.mockito.Mockito.mock;

public class FunctionTest {
    private Function function;
    private SinFunction sinFunction;
    private CosFunction cosFunction;
    private CotFunction cotFunction;
    private TanFunction tanFunction;
    private LnFunction lnFunction;
    private LogFunction logFunction2;
    private LogFunction logFunction3;
    private LogFunction logFunction10;
    private final static double eps = 0.01;

    @BeforeAll
    public void setUp(){
        sinFunction = mock(SinFunction.class);
        cosFunction = mock(CosFunction.class);
        cotFunction = mock(CotFunction.class);
        tanFunction = mock(TanFunction.class);
        lnFunction = mock(LnFunction.class);
        logFunction2 = mock(LogFunction.class);
        logFunction3 = mock(LogFunction.class);
        logFunction10 = mock(LogFunction.class);
        function = mock(Function.class);

        try{

        }

    }

    @ParameterizedTest
    @CsvSource(value = {

    })
}
