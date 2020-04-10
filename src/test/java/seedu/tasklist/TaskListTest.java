package seedu.tasklist;

import org.junit.jupiter.api.Test;
import seedu.tasks.Class;
import seedu.tasks.TaskNonclass;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.common.Constants.TAB;

public class TaskListTest {

    private void addTestTask(TaskList tasks) {
        tasks.addTask(new TaskNonclass("testTitle", "", "2020-06-10", "", "NUS", "", "Test"));
        tasks.addTask(new Class("testClass", "", "1 3", "", "NTU NUS", "", "CLASS"));
    }

    @Test
    public void addAndDeleteTask() {
        TaskList tasks = new TaskList();
        addTestTask(tasks);
        String expectedTask = "[TEST] Title: testTitle | 2020-06-10 ( NUS )";
        String expectedClass = "[CLASS] Title: testClass | MONDAY ( NTU ) | WEDNESDAY ( NUS )";
        assertEquals(expectedTask,tasks.getTask(0).toString());
        assertEquals(expectedClass,tasks.getTask(1).toString());

        Integer output = tasks.getListSize();
        Integer expected = 2;
        assertEquals(output,expected);
        tasks.deleteTask(1);
        Integer outputAfterDelete = tasks.getListSize();
        Integer expectedAfterDelete = 1;
        assertEquals(outputAfterDelete,expectedAfterDelete);
    }

    @Test
    public void changeTask() {
        TaskList tasks = new TaskList();
        tasks.resetCategoryMap();

        addTestTask(tasks);

        String expectedLocation = "NUS COM2";
        tasks.changeLocation(0,expectedLocation);
        assertEquals(tasks.getTask(0).getLocation().get(0),"NUS");

        String expectedReminder = "testReminder";
        tasks.changeReminder(0,expectedReminder);
        assertEquals(tasks.getTask(0).getReminder(),expectedReminder);

        tasks.changeTime(0,"12:00-13:00");
        LocalTime expectedTime = LocalTime.parse("12:00");
        assertEquals(tasks.getTask(0).getTime().get(0),expectedTime);

        tasks.changeDate(0,"2021-04-20");
        LocalDate expectedDate = LocalDate.parse("2021-04-20");
        assertEquals(tasks.categoryCounter(expectedDate),1);

        String expectedDescription = "testDescription";
        tasks.changeDescription(0,expectedDescription);
        assertEquals(tasks.getTask(0).getDescription(),expectedDescription);

        String expectedCategory = "DEADLINE";
        tasks.changeCategory(0,expectedCategory);
        assert (tasks.containsCategory(expectedCategory));
        assert (!tasks.containsCategory("TEST"));

        int expectedLength = tasks.getAllCategory().length;
        assertEquals(expectedLength,2);

        tasks.changeDate(1,"1 4");
        assertEquals(tasks.getTask(1).getDate().get(0).getDayOfWeek().name(),"MONDAY");

    }

    @Test
    public void findTask() {
        TaskList tasks = new TaskList();
        addTestTask(tasks);
        Integer output = tasks.findTasks("TEST").size();
        assertEquals(output,2);
    }

    @Test
    public void setTime_TimeRangeIsWrong_exceptionThrown() {
        TaskList tasks = new TaskList();
        try {
            tasks.addTask(new TaskNonclass("testTitle", "", "", "11:00-10:00", "NUS", "", "Test"));
        } catch (NumberFormatException e) {
            String expected = TAB + "[Error][Add/Edit]: Please enter a valid time range: "
                    + "the end time should be after the start time";
            assertEquals(expected,e.getMessage());
        }
    }




}
