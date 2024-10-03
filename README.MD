****************************** ReadMe Tasker  ***************************************************
The Project started with an Idea to create a replica of Me.
Whatever task I do in my work flow for application management Tasker should be able to replicate automatically  with easy instructions .

Start this project in 2016 , as each line of code is written by me and it does not include and Proprietary software code from my employer ,
I was able to upload it to GitHub.

Tasker
Summary
Tasker is an automation tool designed to replicate workflows for application management. It can automatically perform any configured tasks, offering support for both non-alert and alert tasks. Tasker supports different scheduling frequencies and provides configurable alerts with SQL queries, commands, and various database keywords. Developed independently without any proprietary software, Tasker is suitable for GitHub and can be run as either a daemon or a one-time script.

Email Configuration Reference Table: TASKER_CONFIG
Configure New Non-Alert Task
Add an entry in the tasker_request table.
Supported frequencies:
HOURLY
EOD
SCHEDULED_HOUR
SCHEDULED_MIN
ONCE
EMAIL_IF_PRE_COND_IS_TRUE:
Y: Will send an email only when the pre-condition is true.
Any other value: Will send an email always.
PATH, FILENAME: Denote configuration file path for the task.
Configure New Alert Task
Add an entry in the tasker_request table with NAME='ALERT'.
PATH: Database in which the alert table exists.
FILENAME: ALERT table name.
