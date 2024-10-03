# Tasker
The idea behind Tasker originated from the desire to create a tool that could replicate my workflow for application management, automating every task I perform with simple, intuitive instructions.
Tasker is an automation tool designed to replicate any task assigned or configured in it. This project started in 2016 with the goal of creating a tool that can automate the workflow for application management.

## Table of Contents

-- Content
- Email Configuration Reference
- Configuring Tasks
  - Non-Alert Tasks
  - Alert Tasks
- Task Configuration File Syntax
  - Non-Alert Task Syntax (Without Semicolon)
  - Non-Alert Task Syntax (With Semicolon)
- Running Tasker
- Keywords for Tasks


## Email Configuration Reference

### TASKER_CONFIG Table

#### Configure New Non-Alert Task

1. Add an entry in the `tasker_request` table.
2. Supported frequencies: `HOURLY`, `EOD`, `SCHEDULED_HOUR`, `SCHEDULED_MIN`, `ONCE`,`REPEAT_MIN` , `BYREQ'
3. `EMAIL_IF_PRE_COND_IS_TRUE`: `Y` - Will send email only when the pre-condition is true; for any other value, it will send email always.
4. `PATH`, `FILENAME`: Denotes the configuration file path for the task.

#### Configure New Alert Task

1. Add an entry in the `tasker_request` table with `NAME='ALERT'`.
2. `PATH`: Database in which the alert table exists.
3. `FILENAME`: Alert table name.

## Configuring Tasks

### Non-Alert Tasks

#### Non-Alert Task Configuration File Syntax 

```plaintext
[if <cnt_variable_name> [>,<,=,!=] <value>]
Run <keyword> <variable_name> and <keyword> <variable_name> and <keyword> <variable_name> ........... ;
Declare
<cnt_variable_name>-> Query/command[-><dbkeyword>];
<variable_name> -> Query/command ->Y[-><dbkeyword>];
<variable_name> -> Query/command ->Y;
<variable_name> -> Query/command ->Y;
<variable_name> -> Query/command ->Y;
.
.
.
```


---

## Configure Alerts

### Sample Insert
```sql
Insert into TASKER_ALERT_REQUEST 
values (1,'BCC','TRB PUB LOG','select count(1) from trb1_pub_log','Y','GREATER_THAN',-1,200,500,'QUERY',null,'abpapp',1,null,null);
```
## Notes:
- ALERT_GROUP: Groups alerts into separate emails; you can run one group at a time or all together.
- THRESHOLD_CONDITION: LESS_THAN / GREATER_THAN values.
- NOTIFY_THRESHOLD: Use -1 to always show the alert in the report, otherwise alert will not be shown if the threshold is not met.
- ALERT_TYPE: Can be QUERY or COMMAND.
- ALERT_USER_HOST, APPLICATION_DB: Use comma-separated multiple user@hosts or database keywords; each db/host will appear as a new row in the email.
- PRIORITY, ATTR_VALUE1, ATTR_VALUE2: Not in use.


# How to Run Tasker
- Run as Daemon: Set runinterval in the tasker_config table. After how many minutes should the report be generated?
- Run as One-Time Script: Set runinterval = -1

- Run Any Report by Request
```plaintext
Tasker <REPORT_NAME>
```

- Generate Alert by Request
```plaintext
Tasker ALERT <ALERT_GROUP>    # Run a single group
Tasker ALERT '*'              # Run all groups
```

## Keywords for Tasks

### For SQL Queries
- `select`
- `update`
- `delete`
- `grant`
- `insert`
- `backup` (Creates backup table from a select query)
- `drop`
- `create`
- `export` (Attaches select query output to an Excel sheet)

### To Fetch Files from Location
- `attach`

### To Execute Unix Commands
- `execute`
