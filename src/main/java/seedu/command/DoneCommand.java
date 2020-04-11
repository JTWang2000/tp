package seedu.command;

import seedu.exception.ProjException;
import seedu.parser.Parser;
import seedu.storage.Storage;
import seedu.tasks.Task;
import seedu.tasks.Class;
import seedu.tasks.TaskNonclass;
import static seedu.common.Constants.TAB;
import static seedu.common.Constants.NEW_LINE;
import static seedu.common.Constants.TASKLIST_OFFSET;

/**
 * Allows users to mark a task as complete and displays back to them the change.
 *
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";
    public static final String COMMAND_INFO = COMMAND_WORD + ": changes the status of task to completed";
    public static final String COMMAND_USAGE = COMMAND_WORD + " [TASK_INDEX]";


    public static final String ARGUMENT_COUNT = "1";
    private String feedback = "";

    private String indexCompleteTask;

    public DoneCommand(String indexCompleteTask) {
        this.indexCompleteTask = indexCompleteTask;
    }

    @Override
    public CommandResult execute() throws ProjException {

        try {
            Boolean checkValidNumber = Parser.isInteger(indexCompleteTask);

            if (!checkValidNumber) {
                feedback += TAB + "[Error][Done] Please insert a valid positive number\n";
                return new CommandResult(feedback);
            }

            int indexComplete = Integer.parseInt(indexCompleteTask);

            if (indexComplete == 0) {
                feedback += TAB + "[Error][Done] List numbering starts from 1\n";
                return new CommandResult(feedback);
            }

            assert indexComplete > 0 : "[Error][Done] user input is less than 0, not valid";

            if (taskList.getListSize() == 0) {
                feedback += TAB + "[Alert][Done]: There are no tasks to mark completed!\n";
                return new CommandResult(feedback);
            }

            Task task = taskList.getTask(indexComplete - TASKLIST_OFFSET);

            if (task instanceof Class) {
                feedback += TAB + "[Alert][Done]: Done command does not operate on [CLASS] category.";
            }

            // only allow users to complete a non class task object
            if (task instanceof TaskNonclass) {
                TaskNonclass taskNonClass = (TaskNonclass) task;
                if (!taskNonClass.getDoneStatus()) {
                    markAsDone(taskNonClass);
                } else {
                    feedback += TAB + "[Alert][Done]: Task is already done\n";
                }
            }

        } catch (IndexOutOfBoundsException e) {
            feedback = TAB + "[Error][Done]: Please input a task within the range of: 1 - "
                    + taskList.getList().size() + System.lineSeparator();
            return  new CommandResult(feedback);
        }

        return new CommandResult(feedback);
    }


    private void markAsDone(TaskNonclass task) {
        task.markAsDone();
        feedback += TAB + "Task marked as done: " + NEW_LINE + TAB + TAB;
        feedback += "[" + ((TaskNonclass) task).getStatusIcon() + "] " + task + "\n";
        Storage.overwriteFile(taskList.getList());
    }
}
