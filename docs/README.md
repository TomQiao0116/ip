# Tom User Guide

## Introduction

Tom is a simple command-line chatbot that helps users manage their tasks.
It allows users to add, view, update, and organize tasks such as todos,
deadlines, and events using easy-to-remember commands.

## Adding deadlines

Adds a task that must be completed by a specific date.  
After the command is executed, the deadline task is added to the task list
and displayed to the user.

Example: `deadline DESCRIPTION /by YYYY-MM-DD`

Example usage:
deadline submit assignment /by 2026-03-01


Expected outcome:
Got it. I've added this task:
[D][ ] submit assignment (by: Mar 1 2026)
Now you have X tasks in the list.

## Adding todos

Adds a task without any date or time.

Example: `todo DESCRIPTION`

Example usage:
todo read book

Expected outcome:
Got it. I've added this task:
[T][ ] read book
Now you have X tasks in the list.

## Adding events

Adds an event with a start and end time.

Example: `event DESCRIPTION /from START /to END`

Example usage:
event team meeting /from 2pm /to 4pm

Expected outcome:
Got it. I've added this task:
[D][ ] submit assignment (by: Mar 1 2026)
Now you have X tasks in the list.

## Adding todos

Adds a task without any date or time.

Example: `todo DESCRIPTION`

Example usage:
todo read book

Expected outcome:
Got it. I've added this task:
[T][ ] read book
Now you have X tasks in the list.

## Adding events

Adds an event with a start and end time.

Example: `event DESCRIPTION /from START /to END`

Example usage:
event team meeting /from 2pm /to 4pm

Expected outcome:
Got it. I've added this task:
[E][ ] team meeting (from: 2pm to: 4pm)
Now you have X tasks in the list.

## Viewing all tasks

Displays all tasks currently stored in Tom.

Example: `list`

Expected outcome:
Here are the tasks in your list:
[T][ ] read book
[D][ ] submit assignment (by: Mar 1 2026)

## Marking tasks as done

Marks a task as completed using its index in the list.

Example: `mark INDEX`

Example usage:
mark 2

Expected outcome:
Nice! I've marked this task as done:
[D][X] submit assignment (by: Mar 1 2026)

## Unmarking tasks

Marks a completed task as not done.

Example: `unmark INDEX`

Expected outcome:
OK, I've marked this task as not done yet:
[D][ ] submit assignment (by: Mar 1 2026)

## Deleting tasks

Deletes a task from the list using its index.

Example: `delete INDEX`

Expected outcome:
Noted. I've removed this task:
[T][ ] read book
Now you have X tasks in the list.

## Finding tasks

Finds tasks whose descriptions contain a given keyword.

Example: `find KEYWORD`

Example usage:
find book

Expected outcome:
Here are the matching tasks in your list:
[T][ ] read book

## Sorting tasks

Sorts tasks by type and time-related fields.

Example: `sort`

Expected outcome:
Sorted your tasks.
Here are the tasks in your list:
[D][ ] submit assignment (by: Mar 1 2026)
[E][ ] team meeting (from: 2pm to: 4pm)
[T][ ] read book

## Exiting the program

Exits the chatbot.

Example: `bye`

Expected outcome:
Bye. Hope to see you again soon!



