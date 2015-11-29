
package com.stas.mobile.testing.framework.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.stas.mobile.testing.framework.util.logger.LogController;

public class TestHelper
{
    private static LogController logger = new LogController(TestHelper.class);

    public static long getCurrentGMT6Time()
    {
        return getDateInTimeZone(new Date(), "America/Los_Angeles").getTime();
    }

    public static Date getDateInTimeZone(Date currentDate, String timeZoneId)
    {
        Calendar mbCal = new GregorianCalendar(TimeZone.getTimeZone(timeZoneId));
        mbCal.setTimeInMillis(currentDate.getTime());

        Calendar cal = Calendar.getInstance();
        cal.set(1, mbCal.get(1));
        cal.set(2, mbCal.get(2));
        cal.set(5, mbCal.get(5));
        cal.set(11, mbCal.get(11));
        cal.set(12, mbCal.get(12));
        cal.set(13, mbCal.get(13));
        cal.set(14, mbCal.get(14));
        return cal.getTime();
    }

    public static String removeHtmlTags(String html)
    {
        try
        {
            return html.replaceAll("\\<.*?\\>", "");
        }
        catch (Exception e)
        {
        }
        return null;
    }
}
