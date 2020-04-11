package seedu.calendar;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

/**
 * Class to support date and calendar relation methods.
 */
public class CalendarParser {

    public static final int CALENDAR_OFFSET = 1;
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();

    /**
     * Default constructor.
     */
    public CalendarParser() {

    }

    /**
     * Constructor to accept user input for month.
     * @param month which month to check calendar
     */
    public CalendarParser(int month) {
        if (month < 0 || month > 12) {
            calendar.get(Calendar.MONTH); //set default to current month if any error
        } else {
            month -= CALENDAR_OFFSET;
            calendar.set(2020, month, 1);
        }
    }

    /**
     * Get the first day requested month.
     * @return 0 for Sunday, 6 for Saturday
     */
    public int getFirstDay() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.DAY_OF_WEEK) - CALENDAR_OFFSET; //Sunday is first day, 1
    }

    /**
     * Get total number of days for requested month.
     * @return max days in month
     */
    public int getTotalDays() {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH); //Sunday is first day, 1
    }

    /**
     * Total number of weeks in month.
     * @return number of weeks
     */
    public int getTotalWeeks() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Check which month is selected.
     * @return number of weeks
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Check which month is selected.
     * @return number of weeks
     */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public static LocalDate convertToDate(String userInput) throws DateTimeParseException {
        LocalDate localDate = LocalDate.parse(userInput, dateFormatter);
        return localDate;
    }

}
