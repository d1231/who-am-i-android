package com.danny.whomi.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    @Test
    public void testGetCalendarDate() throws Exception {

        Calendar expected = Calendar.getInstance();
        expected.set(2015, 10, 3, 0, 0, 0);

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 3, 3, 15, 4);

        final Date actual = DateUtils.getCalendarDate(cal.getTimeInMillis());

        assertFalse(actual.after(expected.getTime()));
//        assertFalse(actual.before(expected.getTime()));


    }

    @Test
    public void testGetCalendarDate2() throws Exception {

        Calendar expected = Calendar.getInstance();
        expected.set(2015, 10, 3, 0, 0, 0);

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 4, 3, 15, 4);

        final Date actual = DateUtils.getCalendarDate(cal.getTimeInMillis());

        assertTrue(actual.after(expected.getTime()));
        assertFalse(actual.before(expected.getTime()));


    }
}