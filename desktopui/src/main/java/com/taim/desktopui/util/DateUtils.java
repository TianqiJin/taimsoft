package com.taim.desktopui.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.LocalDate;

/**
 * Created by jiawei.liu on 10/25/17.
 */
public class DateUtils {
    public static DateTime toDateTime(LocalDate localDate) {
        return new DateTime(DateTimeZone.UTC).withDate(
                localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()
        ).withTime(0, 0, 0, 0);
    }

    public static LocalDate toLocalDate(DateTime dateTime) {
        DateTime dateTimeUtc = dateTime.withZone(DateTimeZone.UTC);
        return LocalDate.of(dateTimeUtc.getYear(), dateTimeUtc.getMonthOfYear(), dateTimeUtc.getDayOfMonth());
    }
}
