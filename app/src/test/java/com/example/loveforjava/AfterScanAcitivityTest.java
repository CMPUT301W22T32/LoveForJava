package com.example.loveforjava;

import org.junit.Assert;
import org.junit.Test;

public class AfterScanAcitivityTest {
    AfterScanActivity afterScanActivity = new AfterScanActivity();

    @Test
    public void score_calc_test() {
        String hashedCode = "123";
        Assert.assertEquals(6, afterScanActivity.score_calc(hashedCode));
    }
}
