package org.tpo;

import org.tpo.functions.Calculable;
import org.tpo.functions.Function;
import org.tpo.logarithms.LnFunction;
import org.tpo.logarithms.LogFunction;
import org.tpo.trigonometry.CosFunction;
import org.tpo.trigonometry.CotFunction;
import org.tpo.trigonometry.SinFunction;
import org.tpo.trigonometry.TanFunction;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static double eps = 0.01;

    public static void main(String[] args) {
        SinFunction sinFunction = new SinFunction(eps);
        CosFunction cosFunction = new CosFunction(eps, sinFunction);
        CotFunction cotFunction = new CotFunction(eps, sinFunction);
        TanFunction tanFunction = new TanFunction(eps, sinFunction);
        LnFunction lnFunction = new LnFunction(eps);
        LogFunction logFunction2 = new LogFunction(eps, 2, lnFunction);
        LogFunction logFunction3 = new LogFunction(eps, 3, lnFunction);
        LogFunction logFunction10 = new LogFunction(eps, 10, lnFunction);
        Function function = new Function(cosFunction, cotFunction, tanFunction, logFunction2, logFunction3, logFunction10);
        List<Calculable> list = new ArrayList<>();
        list.add(function);
        list.add(logFunction2);
        list.add(logFunction3);
        list.add(logFunction10);
        list.add(cosFunction);
        list.add(sinFunction);
        list.add(cotFunction);
        list.add(tanFunction);
        list.add(lnFunction);
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.write(list, 0.01);
    }
}
