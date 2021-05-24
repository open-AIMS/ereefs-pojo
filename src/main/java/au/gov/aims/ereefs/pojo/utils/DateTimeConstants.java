package au.gov.aims.ereefs.pojo.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Collection of constants relating to {@code Date} and {@code Time}.
 *
 * @author Aaron Smith
 */
public class DateTimeConstants {

    /**
     * The default time zone to be used within the system.
     */
    static final public String DEFAULT_TIME_ZONE = "Australia/Brisbane";

    /**
     * Encoded equivalent of {@link #DEFAULT_TIME_ZONE}.
     */
    static final public ZoneId DEFAULT_TIME_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE);

    /**
     * The number of seconds in a 24 hour period.
     */
    static final public double SECONDS_IN_24_HOURS = 24.0 * 60 * 60;

    /**
     * Application-specific Epoch of "1990-01-01 00:00:00" using the specified timezone.
     */
    static final public LocalDateTime EPOCH = LocalDateTime.of(
        1990,
        1,
        1,
        0,
        0,
        0
    );

}
