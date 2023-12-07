package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void convertDollarToEuro() {
        // 100$ =>93€
        final int dollars = 100;
        final int euros = Utils.convertDollarToEuro(dollars);
        assertEquals(euros, 93);
    }

    @Test
    public void convertEuroToDollar() {
        // 108€ => 100$
        final int euros = 100;
        final int dollars = Utils.convertEuroToDollar(euros);
        assertEquals(dollars, 108);
    }

    @Test
    public void getTodayDateTest() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String actualDate = Utils.getTodayDate();
        assertEquals(dateFormat.format(new Date()), actualDate);
    }
}