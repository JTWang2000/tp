package seedu.calendar;

import seedu.tasklist.TaskList;
import java.time.LocalDate;
import java.util.Collections;

/**
 * Generates a formatted view of a monthy calendar with notifications if a particular day has a task.
 *
 */
public class GenerateCalendar {
    public static final String VERTICAL_MARK = "|";
    public static final String JOIN_MARK = "+";
    public static final int SYMBOL_SIZE = 2;
    public static final int DAYS_IN_WEEK = 7;
    public static final String PADDING = " ";


    private static final String[] monthName = {"January", "February",
        "March", "April", "May", "June", "July",
        "August", "September", "October", "November",
        "December"};
    private static final String[] HEADING = {"Sunday", "Monday", "Tuesday",
        "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final int COLUMN_SIZE = 7;
    private static final String HORIZONTAL_SEP = "-";
    private static final String TASK_IN_DAY = "Items due: ";
    private static final int DEFAULT_WIDTH = 15;
    private static final String NEW_LINE = System.lineSeparator();
    private static final String NO_ENTRY = String.format("%s%-" + DEFAULT_WIDTH + "s", VERTICAL_MARK, PADDING);
    public static final int CALENDAR_OFFSET = 1;

    private int startingDay;
    private int totalDays;
    private int totalWeeks;
    private int dateCounter = 1;
    private int currentMonth;
    private int currentYear;
    private LocalDate date;
    private String[] tasksInWeek = new String[7];

    /**
     * Constructor to set up internal variables for generating calendar.
     * @param startingDay first day the first date of month falls on
     * @param totalDays how many days in month
     * @param totalWeeks how many weeks in month
     * @param currentMonth month selected by user
     * @param currentYear current year
     */
    public GenerateCalendar(int startingDay, int totalDays, int totalWeeks, int currentMonth, int currentYear) {
        this.startingDay = startingDay;
        this.totalDays = totalDays;
        this.totalWeeks = totalWeeks;
        this.currentMonth = currentMonth + CALENDAR_OFFSET;
        this.currentYear = currentYear;
    }

    /**
     * Method to print the calendar and format the frame.
     * Returns a string containing the lines of the calendar
     */
    public String print() {
        // printing header and box around it
        String feedback = "";

        feedback += String.format("Month is: " + monthName[currentMonth - CALENDAR_OFFSET] + NEW_LINE);
        feedback += printLine();
        feedback += printHeader();
        feedback += printLine();

        // first week
        feedback += printFirstRow();
        feedback += printLine();

        while (dateCounter <= totalDays) {
            feedback += printRow();
            feedback += printLine();
        }
        return feedback;
    }

    private String printLine() {
        String currentLine = "";
        for (int i = 0; i < COLUMN_SIZE; i++) {
            currentLine += JOIN_MARK;
            currentLine += String.join("", Collections.nCopies(DEFAULT_WIDTH, HORIZONTAL_SEP));
        }
        currentLine += NEW_LINE;

        //the last plus to end the table
        return currentLine;
    }



    private String printFirstRow() {
        // padding for first week
        String currentWeek = "";
        for (int i = 0; i < startingDay; i++) {
            currentWeek += NO_ENTRY;
        }

        // remainder of first week
        for (int i = startingDay; i < DAYS_IN_WEEK; i++) {
            currentWeek += String.format("%s%-" + DEFAULT_WIDTH + "d", VERTICAL_MARK, dateCounter);

            date = LocalDate.of(currentYear,currentMonth, dateCounter);
            int taskCounter = TaskList.categoryCounter(date);
            if (taskCounter == 0) {
                tasksInWeek[i] = null;
            } else {
                tasksInWeek[i] = String.valueOf(taskCounter);
            }
            dateCounter++;
        }
        currentWeek += VERTICAL_MARK;
        currentWeek += NEW_LINE;
        currentWeek = getDailyTasks(currentWeek);
        currentWeek += VERTICAL_MARK;
        currentWeek += NEW_LINE;
        return currentWeek;
    }

    private String printRow() {
        String currentWeek = "";
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            if (dateCounter <= totalDays) {
                currentWeek += String.format("%s%-" + DEFAULT_WIDTH + "d", VERTICAL_MARK, dateCounter);
                date = LocalDate.of(currentYear,currentMonth, dateCounter);
                int taskCounter = TaskList.categoryCounter(date);
                if (taskCounter == 0) {
                    tasksInWeek[i] = null;
                } else {
                    tasksInWeek[i] = String.valueOf(taskCounter);
                }
            } else {
                currentWeek += NO_ENTRY;
            }
            dateCounter++;
        }
        currentWeek += VERTICAL_MARK;
        currentWeek += NEW_LINE;
        currentWeek = getDailyTasks(currentWeek);
        currentWeek += VERTICAL_MARK;
        currentWeek += NEW_LINE;
        return currentWeek;
    }

    private String getDailyTasks(String currentWeek) {
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            if (tasksInWeek[i] == null) {
                currentWeek += NO_ENTRY;
            } else {
                String calendarInput = TASK_IN_DAY + tasksInWeek[i];
                currentWeek += String.format("%s%-" + DEFAULT_WIDTH + "s", VERTICAL_MARK, calendarInput);
            }
        }
        return currentWeek;
    }

    private String printHeader() {
        String currentLine = "";
        for (String day : HEADING) {
            currentLine += String.format("%s%-" + DEFAULT_WIDTH + "s", VERTICAL_MARK, day);
        }
        currentLine += VERTICAL_MARK;
        currentLine += NEW_LINE;
        return currentLine;
    }
}
