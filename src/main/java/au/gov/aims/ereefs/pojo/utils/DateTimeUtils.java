package au.gov.aims.ereefs.pojo.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for handling differences between times. This class works with {@code LocalDateTime}
 * objects instead of {@code ZonedDateTime} objects because of discrepancies introduced converting
 * between the instant time-lines of the different time zones (UTC and Aus/Brisbane) introduced due
 * to the epoch of NetCDF files being 1990-01-01 00:00:00. At this time, due to daylight saving
 * being in effect in Qld at that time, the time zone adjustment is +11 instead of the normal +10.
 * This causes problems converting dates in relation to this epoch, so a simplified approach was
 * taken where conversions between time zones are not performed.
 *
 * @author Aaron Smith
 */
public class DateTimeUtils {

    /**
     * General purpose date/time formatters.
     */
    static final public DateTimeFormatter dateFormatter =
        new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();
    static final public DateTimeFormatter isoDateTimeFormatter =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.
            withZone(DateTimeConstants.DEFAULT_TIME_ZONE_ID);
    static final public DateTimeFormatter instantDateTimeFormatter =
        DateTimeFormatter.ISO_INSTANT
            .withZone(DateTimeConstants.DEFAULT_TIME_ZONE_ID);

    /**
     * The {@code ZoneId} of the timezone specified in constructor. At present this is ignored.
     */
    protected ZoneId timeZoneId;

    /**
     * Simple constructor to specify {@link DateTimeConstants#DEFAULT_TIME_ZONE_ID} for
     * {@link #timeZoneId}.
     */
    public DateTimeUtils() {
        this(DateTimeConstants.DEFAULT_TIME_ZONE_ID);
    }

    /**
     * Constructor to capture the specified {@link #timeZoneId}.
     */
    public DateTimeUtils(String timeZone) {
        this(ZoneId.of(timeZone));
    }

    /**
     * Constructor to capture the specified {@link #timeZoneId}.
     */
    public DateTimeUtils(ZoneId timeZone) {
        super();
        this.timeZoneId = timeZone;
    }

    /**
     * Convert the {@code daysSinceEpoch} to a {@code LocalDateTime} by adding the value to the
     * {@link DateTimeConstants#EPOCH}.
     */
    static public LocalDateTime toDateTime(double daysSinceEpoch) {

        // Convert days to seconds.
        double secondsSinceEpoch = daysSinceEpoch * DateTimeConstants.SECONDS_IN_24_HOURS;

        // Add the days to epoch.
        return DateTimeConstants.EPOCH.plusSeconds((long) secondsSinceEpoch);

    }

    /**
     * Calculate the number of days (decimal) between two (2) dates.
     */
    static public double differenceInDays(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.until(endDateTime, ChronoUnit.SECONDS) / DateTimeConstants.SECONDS_IN_24_HOURS;
    }

    /**
     * Calculate the number of days (decimal) between the specified date and the
     * {@link DateTimeConstants#EPOCH}.
     */
    static public double sinceEpochInDays(LocalDateTime dateTime) {
        return DateTimeUtils.differenceInDays(DateTimeConstants.EPOCH, dateTime);
    }

    static public LocalDateTime parseAsDate(String dateTime) {
        return DateTimeUtils.dateFormatter.parse(dateTime, LocalDateTime::from);
    }
}
