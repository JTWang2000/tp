package seedu.tasklist;

import seedu.tasks.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static seedu.common.Constants.CLASS_CATEGORY;


public class TaskList {

    private static ArrayList<Task> taskList;
    private static HashMap<String, ArrayList<Integer>> categoryMap = new HashMap<>();

    public TaskList() {
        taskList = new ArrayList<>();
    }

    //Accessors:
    public int getListSize() {
        return taskList.size();
    }

    public Task getTask(int index) {
        return taskList.get(index);
    }

    // TODO Fix so that method returns a deep copy of the list
    public ArrayList<Task> getList() {
        return taskList;
    }

    //Mutator methods:
    public void changeDescription(int taskIndex, String newDescription) {
        taskList.get(taskIndex).setDescription(newDescription);
    }

    public void changeDate(int taskIndex, String newDate) {
        taskList.get(taskIndex).setDate(newDate);
    }

    public void changeTime(int taskIndex, String newTime) {
        taskList.get(taskIndex).setTime(newTime);
    }

    public void changeLocation(int taskIndex, String newLocation) {
        taskList.get(taskIndex).setLocation(newLocation);
    }

    public void changeReminder(int taskIndex, String newReminder) {
        taskList.get(taskIndex).setReminder(newReminder);
    }
  
    //Other methods:
    /**
     * Checks if the given task contains the given pattern.
     *
     * @param task    task to inspect
     * @param pattern pattern to look for in task
     * @return true if task contains pattern
     */
    private boolean hasPattern(Task task, String pattern) {
        pattern = pattern.toLowerCase();
        Boolean hasKeywordInStringField = task.getTitle().toLowerCase().contains(pattern)
                || task.getDescription().toLowerCase().contains(pattern);
        for (String location : task.getLocation()) {
            hasKeywordInStringField = hasKeywordInStringField || location.toLowerCase().contains(pattern);
        }
        return hasKeywordInStringField;
    }

    /**
     * Change category of a task and change the category mapping.
     *
     * @param taskIndex   Index of task that needs to be changed.
     * @param newCategory Newly category.
     */
    public void changeCategory(int taskIndex, String newCategory) {
        String oldCategory = taskList.get(taskIndex).getCategory();
        this.categoryMap.get(oldCategory).remove(Integer.valueOf(taskIndex));
        if (this.categoryMap.get(oldCategory).size() == 0) {
            this.categoryMap.remove(oldCategory);
        }
        taskList.get(taskIndex).setCategory(newCategory);
        updateCategoryMap(newCategory, taskIndex);
    }

    /**
     * Add new category mapping.
     * @param category input category.
     * @param index input task.
     */
    private void updateCategoryMap(String category, Integer index) {
        if (category.length() != 0) {
            //add new category
            if (!categoryMap.containsKey(category)) {
                this.categoryMap.put(category, new ArrayList<>());
            }
            this.categoryMap.get(category).add(index);
        }
    }

    /**
     * Add tasks and update the category mapping.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        taskList.add(task);
        updateCategoryMap(task.getCategory(), taskList.size() - 1);
    }


    /**
     * Removes a task and return a reference to that object.
     *
     * @param index Index of the task to be deleted.
     * @return The task that is deleted.
     */
    public Task deleteTask(int index) {

        String category = taskList.get(index).getCategory();
        int j = 0;
        for (int i : categoryMap.get(category)) {
            if (i == index) {
                categoryMap.remove(j);
                break;
            }
            j++;
        }

        if (categoryMap.get(category).size() == 0) {
            categoryMap.remove(category);
        }
        Task toRemove = taskList.get(index);
        taskList.remove(index);
        return toRemove;
    }

    /**
     * Finds the tasks that contain the given pattern in their
     * title, description or location.
     *
     * @param pattern pattern to look for in the tasks.
     * @return tasks that match the pattern.
     */
    public ArrayList<Task> findTasks(String pattern) {

        ArrayList<Task> foundTasks = new ArrayList<>();

        for (Task task : taskList) {
            if (hasPattern(task, pattern)) {
                foundTasks.add(task);
            }
        }

        return foundTasks;
    }

    public Boolean containsCategory(String category) {
        return this.categoryMap.containsKey(category);
    }

    public ArrayList<Integer> getCategoryTask(String category) {
        return this.categoryMap.get(category);
    }

    public String[] getAllCategory() {
        return this.categoryMap.keySet().toArray(new String[this.categoryMap.size()]);
    }

    public void resetCategoryMap() {
        categoryMap.clear();
    }

    /**
     * Checks list of tasks with supplied date to see how many tasks for that date.
     *
     * @param checkDate date used to check against list.
     * @return number of tasks for that day.
     */
    public static int categoryCounter(LocalDate checkDate) {
        int totalTasksDay = 0;
        // searching every task to see if date matches
        for (Task task : taskList) {
            int matchedTask;
            if (task.getCategory() == CLASS_CATEGORY) {
                continue;
            }
            ArrayList<LocalDate> taskDates = task.getDate();
            for (LocalDate taskDate : taskDates) {
                matchedTask = checkDate.compareTo(taskDate);
                if (matchedTask == 0) {
                    totalTasksDay++;
                }
            }
        }
        return totalTasksDay;
    }

}
