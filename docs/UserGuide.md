# CAFS - User Guide

By: `AY1920S2-CS2113-T14-3`


1. [Introduction](#1-introduction)
1. [Quick Start](#2-quick-start)
1. [Features](#3-features) <br/>
  3.1 [Viewing help: `help`](#31-viewing-help-help) <br/>
  3.2 [Adding tasks: `add`](#32-adding-tasks-add) <br/>
  3.3 [Editing tasks: `edit`](#33-editing-tasks-edit) <br/>
  3.4 [Listing tasks: `list`](#34-listing-tasks-list) <br/>
  3.5 [Deleting tasks: `delete`](#35-deleting-tasks-delete) <br/>
  3.6 [Marking tasks as done: `done`](#36-marking-tasks-as-done-done) <br/>
  3.7 [Finding tasks: `find`](#37-finding-tasks-find) <br/>
  3.8 [Viewing calendar: `calendar`](#38-viewing-calendar-calendar) <br/>
  3.9 [Clearing output: `clc`](#39-clearing-output-clc) <br/>
  3.10 [Exiting program: `bye`](#310-exiting-program-bye)
1. [FAQ](#4-faq)
1. [Command Summary](#5-command-summary)


## 1. Introduction

CAFS is a CLI calender-like task scheduler that supports task and 
class schedule adding. It is simple to use, and comes with an auto save function to 
remember your tasks.

<br/>

---

## 2. Quick Start

1. Ensure you have Java 11 or above installed in your computer.

1. Download the latest cafs.jar [here](https://github.com/AY1920S2-CS2113-T14-3/tp/releases).

1. Copy the file to the folder you want to use as the home folder.

1. Run the jar file using `java - jar caf.jar` from your terminal window.

<br/>

---
## 3. Features

**Command Format:**
* The <UPPER_CASE> format indicates user input. However, when inputting, there is no need to input `<>`symbol. 
The `< >` symbol just for readability. 
* Command keyword (e.g. `add`) is case insensitive. However, the delimiters (e.g. `n/`) are case sensitive.
* It is okay to switch the sequence when inputting the delimiters:
    * `add n/<NAME> l/<LOCATION>` has same effects as `add l/<LOCATION> n/<NAME> `
* When inputting a time, the format is always:`hh:mm-hh:mm`. No space is allowed around `-`. 
  Also, input like `1:00-2:00` will not be allowed. 
* When inputting a date, the format is always: `yyyy-mm-dd`.
* Some recognized date/time error will be automatically parsed to accepted format.
    * `24:00` will be parsed to `23:59` since 24:00 is next day. 
    * `2021-02-30` will be parsed to `2021-02-28`

---
### 3.1. Viewing help: `help`
Displays the set of supported commands <br/>
**Usage**: `help`

### 3.2. Adding tasks: `add`
Users add tasks using this command. <br/>
**Usage**: `add  n/<NAME> t/<TIME> l/<LOCATION> d/<DATE> i/<INFORMATION> r/<REMINDER> c/<CATEGORY>` <br/>
  * Only name `<NAME>` is compulsory to include. However, if user only inputs time, 
    then date of current day will be added automatically. 
  * Space is okay in `<NAME>`,`<REMINDER>` and `<INFORMATION>`.  e.g.: `n/2113 deadline` is accepted.                                                       
  * The `<TIME>` should be in time duration format: `hh:mm-hh:mm` (e.g. `14:00-16:00`).
      There should be no space between this duration. 
  * Since a task/class may have multiple time zones in a week, students can just add list of time zones.
      However, the number of `<TIME>`should match with the number of `<DATE>`. It is suggested that 
      the number of `<LOCATION>` also match with the number of `<TIME>`. Use space to separate the time 
      zone/date/location. 
     * Example
       * `add n/CS2113 t/12:00-13:00 15:00-16:00 d/2020-07-01 2020-09-01 l/NUS NTU`  
       Adds a task with two time zones, dates, and locations.
  * If no `<DATE>` is inputted by `<TIME>` is inputted, the system will automatically add today's date
      to that task. 
  * The default category is TODO. To add a class, just indicate the category is `CLASS`. The category is
      case-insensitive.  
  * Task's notification are `[N]` (not completed, after just add) and `[Y]` (completed, after done) Class's notification is always `[W]` (weekly).

#### 3.2.1 Adding Task:
* `<Date>` should be in format:`yyyy-mm-dd`

Examples: <br/>
* `add n/Project Meeting t/12:00-13:00 15:00-16:00 d/2020-07-01 2020-09-01 l/NUS NTU c/meeting`<br/>
Adds a meeting: Project Meeting on two time slots: 12:00-13:00 on 2020-07-01(NUS) and 15:00-16:00 on 2020-09-01(NTU)
* `add n/2113 v2.1 t/23:00-24:00 d/2020-05-16 c/deadline`<br/>
Adds a deadline: 2113 v2.1 on a specific date with a time range for the deadline. 
* `add n/Project Meeting t/12:00-13:00 15:00-16:00 l/NUS NTU i/important`<br/>
Adds a task  with two time slots on two locations with a description important.

#### 3.2.2 Adding Class:
* `<DATE>` should be the day of the week, represented by an integer (e.g. `1 3` means Mon Wed). 

Examples:<br/>
* `add t/11:00-12:00 01:00-03:00 n/2113 d/3 4 c/CLASS l/COM2 COM1` <br/>
Adds a class with two time frames, two days (Wednesday and Thursday), and two locations


### 3.3. Editing tasks: `edit`
Edit the inputted task/class. <br/>
**Usage**:`edit TASK_INDEX t/<TIME> l/<LOCATION> d/<DATE> i/<INFORMATION> r/<REMINDER> c/<CATEGORY>`

* It is not allowed to edit the `<NAME>`.
* It is okay to edit the `<CATEGORY>`. However, it is not allowed to change from class category to other 
  categories. It is also not allowed to change from other categories to class category. 
* When edit `<TIME>` and `<DATE>`, please be reminded that the number of `<TIME>` should match with the 
  number of `<DATE>`. It is suggested that number of location `<LOCATION>` also matches. 
    * If you only have added one of the `<TIME>` and `<DATE>` for that task, no need to follow the 
      _number matching of TIME and DATE_ since there is only field in that task. 
    * If you have added both `<TIME>` and `<DATE>` for a task _1_:<br/>
      For example:  Previously the task _1_ is:`[TODO] Title: Project Meeting | 2020-09-10 : 11:00 - 12:00`<br/>
      When editing, can either input `<TIME>` with same number of `<DATE>` in current task _1_ : 
                <br/>e.g. `edit 1 t/01:00-02:00`<br/>
                or input `<DATE>` with same number of `<TIME>` in current task _1_ : 
                <br/>e.g. `edit 1 d/2020-11-12`<br/>
                or input both `<TIME>` and `<DATE>` with same number : 
                <br/>e.g. `edit 1 d/2020-09-10 2020-09-11 t/11:00-12:00 13:00-14:00`
  

Examples: <br/>
* `edit 2 l/NUSCOM2`<br/>
Edit the location of task 2 to be NUSCOM2
* `edit 2 c/todo`  <br/>
Edit the category of task 2 to be TODO

  ​      
### 3.4. Listing tasks: `list`
#### 3.4.1. List
Displays all the current tasks in the list. <br/> 
**Usage**: `list`
* Note that other list sub-commands (see below) will default to just `list` when input is wrongly formatted

#### 3.4.2. List by Category
List tasks that belong to a specific category. <br/>
**Usage**: `list c/<CATEGORY>`
* Wrong command: `list TODO` will have the same effects as just `list`
* The `<CATEGOTY>` is case insensitive. That is, `list c/TODO` and `list c/todo` have same effect.

Examples: <br/>
* `list c/TODO`
* `list c/DEADLINE`

#### 3.4.3. List by Time
Lists tasks and classes by specific time range. <br/>
**Usage**: `list t/<TIME>`
* `task` and `class` with a specific time range can be listed by `list t/hh:mm-hh:mm`.
List the `events` which are exactly at that time range or have a overlap at that time range.

Example: <br/>
* `list t/15:00-16:00`<br/>
 Lists the `tasks` that fall within the given time frame 

#### 3.4.4. List by Date
List tasks by specific date. <br/>
**Usage** : `list d/<DATE>`

* `task` with a specific date can be listed by `list d/yyyy-mm-dd`. Or `tasks` have date/dates in the specific dates can be listed by  `list d/yyyy-mm-dd yyyy-mm-dd ...` *(more dates)*.
* `class` cannot be listed by date since class only adopts schedule.

Examples: <br/>
* `list d/2020-07-01` <br/>
Lists the `tasks` that fall on the date `2020-07-01` 
* `list d/2020-07-01 d/2020-09-01` <br/>
Lists the `tasks` that fall on either `2020-07-01` or `2020-09-01`
  
#### 3.4.5. List by Date & Time        
Lists tasks by specific date and time. <br/>
**Usage** : `list d/<DATE> t/<TIME>`

* `task` with a specific date and time range can be listed by `list d/yyyy-mm-dd t/hh:mm-hh:mm`
* `class` cannot be listed by date and time since class only adopts schedule.   
* List the `tasks` which are exactly at that time range or have a overlap at that time range.
  

Example:
* `list d/2020-07-01 t/12:00-13:00` <br/>
Lists the `tasks` that fall on the given date (`2020-07-01`) and within the given time frame (`12:00-13:00`)
      
#### 3.4.6. List by Category, Date & Time

List tasks by category and date and time. 
**Usage**: `list c/<CATEGORY> d/<DATE> t/<TIME>`
* `tasks` in a specific date and specific time range in a  specific category. However, the `class` category cannot be shown, since these don't have date values.
* `tasks`  just in a specific date/dates in a specific category. However, the `class` category cannot be shown, since these don't have date values.
* `tasks` and `classes`  just in a specific time range in a specific category. All `categories` can be shown.

Examples: <br/>
* `list d/2020-07-01 t/12:00-13:00 c/meeting` <br/>
Lists the `tasks` whose start time before `2020-07-01 13:00` and end time after `2020-07-01 12:00` and  category is `meeting`
* `list d/2020-05-16 c/deadline` <br/>
Lists the `tasks` which have date on `2020-05-16` and whose `category` is `deadline`
* `list t/23:00-23:30 c/deadline`<br/>
Lists the `tasks` whose start time before `23:00` and end time after `23:30`and `category` is `deadline`

### 3.5. Deleting tasks: `delete`
#### 3.5.1. Delete a task/class
Deletes a task from the list <br/>
**Usage**: `delete <TASK_INDEX>`
* The index refers to the index number shown in the displayed task list.
* Use `list` to derive task index
* Index has to be an _integer_

Example:
* `delete 2` <br/>
Deletes the second task from the list.

#### 3.5.2 - Delete by Time
Delete tasks and classes by specific time range. <br/>
**Usage**: `delete t/<TIME>`
* `task` and `class` with a specific time range can be deleted by `delete t/hh:mm-hh:mm`.
* Delete the `tasks` and `classes`  which are exactly at that time range or have a overlap at that time range. 
  

Example: 
* `delete t/15:00-16:00` <br/>
Deletes the `tasks` that fall within the given time frame `15:00-16:00`

#### 3.5.3 - Delete by Date
Delete tasks by specific date. <br/>
**Usage** : `delete d/<DATE>`
* `task` with a specific date can be listed by `delete d/yyyy-mm-dd`. 
* `class` cannot be deleted by date since classes adopt to a schedule (Monday to Sunday)

Examples:
   * `delete d/2020-07-01` <br/>
      Deletes the `tasks` that fall on `2020-07-01`
   * `delete d/2020-07-01 d/2020-09-01` <br/>
      Deletes the `tasks` that fall on either `2020-07-01` or `2020-09-01`

#### 3.5.4 - Delete by Date & Time        
Delete tasks by specific date and time. <br/>
**Usage** : `delete d/<DATE> t/<TIME>`
* `task` with a specific date and time can be listed by `delete d/yyyy-mm-dd t/hh:mm-hh:mm`.
*  `class` cannot be listed by date and time since class only adopts schedule. 
* Delete the `tasks` which are exactly at that time range or have a overlap at that time range.
  

 Example: <br>
   * `delete d/2020-07-01 t/12:00-13:00` <br/>
      Deletes the `tasks` that fall on the given date and within the given time frame.

### 3.6. Marking tasks as done: `done`
Marks a task as complete, changing the notification from '`N`' (not completed) to '`Y`' (completed). <br>
**Usage**: `done <TASK_INDEX>` 

* Obtain task index from `list` command. (without any filters)
* task cannot be a from the category class. Class's notification is always '`W`' (Weekly).
* Index is a valid positive number.

Example:
* `done 4` <br/>
Marks the fourth task as completed. 

### 3.7. Finding tasks: `find`
Searches all task titles, descriptions, and locations for supplied pattern <br/>
**Usage**: `find <PATTERN>`
* Keyword is case _insensitive_
* Can search the pattern in title, description and location. 

Examples:
* `find project meeting` <br/>
Lists the tasks with the given pattern (`project meeting`) if found in their title, description, or location

### 3.8. Viewing calendar: `calendar`
Prints a monthly representation of a calendar. Shows the number of tasks a user has on a particular day.
Users are able to specify which month to look up. Default is the current month. 

Will still display tasks from past months as long as not marked as complete. <br/>

**Usage**: `calendar <optional month>` 
* Month is in integer representation
* Where 1 is January and 12 is December
* Default set to current month
* If number supplied is not within the month range, it will be set to current month

### 3.9. Clearing output: `clc`
Clears the output window. <br/>
**Usage**:`clc`

### 3.10. Exiting program: `bye`
Exits the program. <br/>
**Usage**: `bye`

<br/>  

---
## 4. FAQ
 * **Q:** How do I save my tasks? <br/>
 **A:** Tasks are saved automatically and loaded upon start up of application
    * If unable to load, check the directory and file name:
        * Default folder from application folder: `/Save/`
        * Default file name: `data.txt`

<br/>

---
## 5. Command Summary
 * **Add**: `add n/<NAME> t/<TIME> l/<LOCATION> d/<DATE> i/<INFORMATION> r/<REMINDER> c/<CATEGORY>`
 * **Edit**: `edit TASK_INDEX t/<TIME> l/<LOCATION> d/<DATE> i/<INFORMATION> r/<REMINDER> c/<CATEGORY>`
 * **List**: 
    * `list`
    * `list c/<CATEGORY>` 
    * `list t/<TIME>`
    * `list d/<DATE>`
    * `list d/<DATE> t/<TIME>`
    * `list c/<CATEGORY> d/<DATE> t/<TIME>`
 * **Delete**:
    * `delete <TASK_INDEX>` 
    * `delete t/<TIME>` 
    * `delete d/<DATE>`
    * `delete d/<DATE> t/<TIME>`
 * **Done**: `done <TASK_INDEX>` 
 * **Find**: `find <PATTERN>` 
 * **Help**: `help`
 * **Calendar**: `calendar` or `calendar <MONTH>`
 * **Clear**:`clc`
 * **Exit**: `bye`