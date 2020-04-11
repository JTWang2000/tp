package seedu.command;

import seedu.calendar.CalendarParser;
import seedu.common.Messages;
import seedu.exception.CommandExceptions.EmptyTaskListException;
import seedu.exception.CommandExceptions.TaskOutOfBoundsException;
import seedu.exception.ProjException;
import seedu.tasks.Task;
import seedu.ui.Ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;

import static seedu.common.Constants.TAB;

public class DeleteCommand extends Command {

    private String userInput;
    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_INFO = COMMAND_WORD + ": deletes tasks from the list"
            + " (e.g all tasks or by category)";
    public static final String COMMAND_USAGE = COMMAND_WORD + " [TASK_INDEX]" + System.lineSeparator() + TAB + TAB + TAB
            + COMMAND_WORD + " c/[CATEGORY]" + System.lineSeparator() + TAB + TAB + TAB
            + COMMAND_WORD + " d/[YYYY-MM-DD]" + System.lineSeparator() + TAB + TAB + TAB
            + COMMAND_WORD + " t/[HH:MM-HH:MM]" + System.lineSeparator() + TAB + TAB + TAB
            + COMMAND_WORD + " d/[YYYY-MM-DD] t/[HH:MM-HH:MM]";

    private int invalidNumber;
    private static final int INVALID_TIME_RANGE = 1;
    private static final int INVALID_DATE_RANGE = 2;
    private static final int INVALID_NUMBER = 0;

    private static final int DELETE_ERROR = 0;
    private static final int DELETE_TASK = 1;
    private static final int DELETE_BY_CATEGORY = 2;
    private static final int DELETE_BY_DATE_CATEGORY = 4;

    public DeleteCommand(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public CommandResult execute() throws ProjException {

        String feedback = "";
        String[] commandSections = userInput.split(" ");

        try {
            int len = commandSections.length;
            String category = getCategory(userInput).trim().toUpperCase();
            String date = getDate(userInput).trim();
            String time = getTime(userInput).trim();

            checkDateTimeFormat(date, time);

            int listCmdSubtype = getCmdSubtype(category, date, time, len);
            //initial the invalid number
            invalidNumber = 0;
            checkForEmptyList();
            String strIndex = commandSections[1].trim();

            switch (listCmdSubtype) {
            case DELETE_TASK:
                feedback = deleteTask(strIndex);
                break;

            case DELETE_BY_CATEGORY:
                feedback = deleteByCategory(category);
                break;

            case DELETE_BY_DATE_CATEGORY:
                feedback = deleteByDateCategory(date, time, category);
                break;

            default:
                feedback = "[Error][List] No such option to filter";
                break;
            }

        } catch (TaskOutOfBoundsException e) {
            feedback = TAB + String.format(Messages.MESSAGE_OUT_OF_BOUNDS, COMMAND_WORD, commandSections[1].trim(),
                    taskList.getListSize());
        } catch (IndexOutOfBoundsException e) {
            feedback = TAB + Messages.MESSAGE_MISSING_NUMBER;
        } catch (NumberFormatException e) {

            if (invalidNumber == INVALID_NUMBER) {
                feedback = TAB + String.format(Messages.MESSAGE_INVALID_INDEX, COMMAND_WORD, commandSections[1]);
            } else if (invalidNumber == INVALID_TIME_RANGE) {
                feedback = TAB + "[Error][Add/Edit]: Please enter a valid time range: "
                        + "the end time should be after the start time";
            } else {
                feedback = TAB + Messages.MESSAGE_PRESENT_OR_FUTURE_DATE;

            }

        } catch (EmptyTaskListException e) {
            feedback = TAB + String.format(Messages.MESSAGE_LIST_IS_EMPTY, COMMAND_WORD, COMMAND_WORD);

        } finally {
            feedback = checkForEmptyFeedback(feedback);
            return new CommandResult(feedback);
        }
    }

    private String checkForEmptyFeedback(String feedback) {

        if (feedback.isEmpty()) {
            return TAB + Messages.MESSAGE_DELETE_NO_TASK_FOUND;
        }

        return feedback;
    }

    private void checkForEmptyList() throws EmptyTaskListException {
        if (taskList.getListSize() == 0) {
            throw new EmptyTaskListException();
        }
    }

    private void checkForValidIndex(int index) throws TaskOutOfBoundsException {

        if (index < 0 || index >= taskList.getListSize()) {
            throw new TaskOutOfBoundsException();
        }
    }

    private String formatSuccessFeedback(Task removed) {

        String feedback = "";

        String description = TAB + TAB + removed.toString() + System.lineSeparator();
        description += String.format(TAB + Messages.MESSAGE_REMAINING_TASKS, taskList.getListSize());
        description += System.lineSeparator();
        feedback = String.format(TAB + Messages.MESSAGE_DELETE_SUCCESS, description);

        return feedback;
    }


    /**
     * delete the tasks by date and/or time
     * in the specific category.
     *
     * @param date     :
     * @param time     :
     * @param category :
     * @return : java.lang.String
     * @author : yuchenlichuck
     * @date : 11/4/20 7:06 PM
     */
    private String deleteByDateCategory(String date, String time, String category)
            throws DateTimeParseException {

        //only task can do it just get by date.
        String feedback = "";
        if (time == null || time.isEmpty()) {
            String[] dates = date.split("\\s+");

            //dates input dates
            HashSet<LocalDate> inputDates = new HashSet<>();
            for (String d : dates) {
                LocalDate addedDate = CalendarParser.convertToDate(d);

                if (addedDate.compareTo(LocalDate.now()) < 0) {
                    invalidNumber = INVALID_DATE_RANGE;
                    throw new NumberFormatException(TAB + Messages.MESSAGE_PRESENT_OR_FUTURE_DATE);
                }

                inputDates.add(addedDate);
            }

            for (int m = 0; m < taskList.getListSize(); m++) {
                Task task = taskList.getTask(m);
                if (!category.isEmpty() && !task.getCategory().equals(category)) {
                    continue;
                }
                if (task.getCategory().equals("CLASS")) {
                    continue;
                }

                ArrayList<LocalDate> localDates = task.getDate();
                for (LocalDate d : localDates) {
                    if (inputDates.contains(d)) {

                        Task removedTask = taskList.deleteTask(m);
                        storage.overwriteFile(taskList.getList());
                        assert removedTask != null : "Removed-task is null";
                        feedback += formatSuccessFeedback(removedTask) + "\n";
                        m--;
                        break;
                    }
                }
            }
        }

        // just get by time
        if (date.isEmpty()) {
            String[] times = time.split("\\s+");
            ArrayList<LocalTime> startTimes = new ArrayList<>();
            ArrayList<LocalTime> endTimes = new ArrayList<>();
            for (String atime : times) {

                String[] timeRange = atime.split("-");

                if (timeRange[1].equals("24:00")) {
                    timeRange[1] = "23:59";
                }

                LocalTime startTime = LocalTime.parse(timeRange[0], DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime endTime = LocalTime.parse(timeRange[1], DateTimeFormatter.ofPattern("HH:mm"));


                if (startTime.isAfter(endTime)) {
                    invalidNumber = INVALID_TIME_RANGE;
                    throw new NumberFormatException(TAB + "[Error][Add/Edit]: Please enter a valid time range: "
                            + "the end time should be after the start time");
                }

                startTimes.add(startTime);
                endTimes.add(endTime);
            }

            int size = startTimes.size();
            for (int i = 0; i < taskList.getListSize(); i++) {
                Task task = taskList.getTask(i);
                if (!category.isEmpty() && !task.getCategory().equals(category)) {
                    continue;
                }
                ArrayList<LocalTime> localTimes = task.getTime();

                label1:
                for (int j = 0; j < localTimes.size() / 2; j++) {
                    for (int k = 0; k < size; k++) {

                        if (localTimes.get(2 * j).isBefore(endTimes.get(k))
                                && localTimes.get(2 * j + 1).isAfter(startTimes.get(k))) {
                            Task removedTask = taskList.deleteTask(i);
                            storage.overwriteFile(taskList.getList());
                            assert removedTask != null : "Removed-task is null";
                            feedback += formatSuccessFeedback(removedTask) + "\n";
                            i--;
                            break label1;
                        }
                    }
                }
            }
        }

        //date and time
        if (!date.isEmpty() && !time.isEmpty()) {
            String[] times = time.split("\\s+");
            String[] dates = date.split("\\s+");

            ArrayList<LocalTime> startTimes = new ArrayList<>();
            ArrayList<LocalTime> endTimes = new ArrayList<>();
            ArrayList<LocalDate> dateList = new ArrayList<>();

            for (String atime : times) {
                String[] timeRange = atime.split("-");

                if (timeRange[1].equals("24:00")) {
                    timeRange[1] = "23:59";
                }

                LocalTime startTime = LocalTime.parse(timeRange[0], DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime endTime = LocalTime.parse(timeRange[1], DateTimeFormatter.ofPattern("HH:mm"));


                if (startTime.isAfter(endTime)) {
                    invalidNumber = INVALID_TIME_RANGE;
                    throw new NumberFormatException(TAB + "[Error][Add/Edit]: Please enter a valid time range: "
                            + "the end time should be after the start time");
                }
                startTimes.add(startTime);
                endTimes.add(endTime);
            }

            for (String adate : dates) {
                dateList.add(LocalDate.parse(adate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            }

            for (int i = 0; i < taskList.getListSize(); i++) {
                Task task = taskList.getTask(i);
                if (!category.isEmpty() && !task.getCategory().equals(category)) {
                    continue;
                }

                if (task.getCategory().equals("CLASS")) {
                    continue;
                }

                ArrayList<LocalTime> localTimes = task.getTime();
                ArrayList<LocalDate> localDates = task.getDate();
                la:
                for (int j = 0; j < localDates.size(); j++) {
                    for (int k = 0; k < dateList.size(); k++) {
                        if (localTimes.get(2 * j).isBefore(endTimes.get(k))
                                && localTimes.get(2 * j + 1).isAfter(startTimes.get(k))
                                && localDates.get(j).equals(dateList.get(k))) {
                            Task removedTask = taskList.deleteTask(i);
                            storage.overwriteFile(taskList.getList());
                            assert removedTask != null : "Removed-task is null";
                            feedback += formatSuccessFeedback(removedTask) + "\n";
                            i--;
                            //break the double loop
                            break la;
                        }
                    }
                }
            }
        }
        return feedback;
    }

    /**
     *delete the tasks by category
     *with the specific category.
     *
     * @return : java.lang.String
     * @author : yuchenlichuck
     * @date : 11/4/20 7:06 PM
     */
    private String deleteByCategory(String category) throws ProjException {

        if (!taskList.containsCategory(category)) {
            ui.showAllCategory(taskList.getAllCategory());
            throw new ProjException(TAB + "There is no " + category + " in current category.\n"
                    + Ui.DIVIDER);
        }

        String feedback = "";

        for (int i = 0; i < taskList.getListSize(); i++) {
            if (taskList.getTask(i).getCategory().equals(category)) {
                Task removedTask = taskList.deleteTask(i);
                storage.overwriteFile(taskList.getList());
                i--;
                assert removedTask != null : "Removed-task is null";
                feedback += formatSuccessFeedback(removedTask) + "\n";
            }
        }
        return feedback;
    }

    /**
     * Delete task by the strIndex.
     *
     * @param strIndex :
     * @return : java.lang.String
     * @author : yuchenlichuck
     * @date : 11/4/20 7:07 PM
     */
    private String deleteTask(String strIndex) throws TaskOutOfBoundsException {

        int index = Integer.parseInt(strIndex) - 1;
        checkForValidIndex(index);
        assert index < taskList.getListSize() : "index > the size of taskList";
        Task removedTask = taskList.deleteTask(index);
        storage.overwriteFile(taskList.getList());
        return formatSuccessFeedback(removedTask);

    }

    /**
     * get cmd sub type.
     *
     *
     * @param category :
     * @param date     :
     * @param time     :
     * @param len      :
     * @return : int
     * @author : yuchenlichuck
     * @date : 11/4/20 7:04 PM
     */
    private int getCmdSubtype(String category, String date, String time, int len) {

        if (date.isEmpty() && time.isEmpty() && !category.isEmpty()) {
            return DELETE_BY_CATEGORY;
        }

        if (!(date.isEmpty() && time.isEmpty())) {
            return DELETE_BY_DATE_CATEGORY;
        }

        if (len == 2 && (date.isEmpty() && time.isEmpty() && category.isEmpty())) {
            return DELETE_TASK;
        }

        return DELETE_ERROR;
    }

}