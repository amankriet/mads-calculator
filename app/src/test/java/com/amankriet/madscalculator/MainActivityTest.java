package com.amankriet.madscalculator;

import static com.amankriet.madscalculator.MainActivity.operations;
import static com.amankriet.madscalculator.MainActivity.result;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void calculationCheckEqual() {
        operations = "6×2×3÷2";
        MainActivity.performMADSCalculation();
        assertEquals("18", result);

        operations = "100÷2÷2÷5×2";
        MainActivity.performMADSCalculation();
        assertEquals("2", operations);

        operations = "6+3+1-2";
        MainActivity.performMADSCalculation();
        assertEquals("8", operations);

        operations = "15-3-2-2+7";
        MainActivity.performMADSCalculation();
        assertEquals("1", operations);

        operations = "6×2×3÷2+1-2";
        MainActivity.performMADSCalculation();
        assertEquals("10", operations);

        operations = "6×2×3÷2+1-2+";
        MainActivity.performMADSCalculation();
        assertEquals("10", result);
    }

}