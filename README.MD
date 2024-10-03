# Tasker

Tasker is an automation tool designed to replicate any task assigned or configured in it. This project started in 2016 with the goal of creating a tool that can automate the workflow for application management.

## Table of Contents

- Introduction
- Email Configuration Reference
- Configuring Tasks
  - Non-Alert Tasks
  - Alert Tasks
- Task Configuration File Syntax
  - Non-Alert Task Syntax (Without Semicolon)
  - Non-Alert Task Syntax (With Semicolon)
- Running Tasker
- Keywords for Tasks
- DB Keywords for Tasks/Alerts

## Introduction

Tasker is an automation tool that can handle any task assigned or configured in it. The project was initiated in 2016, and all code is written by me, without including any proprietary software code from my employer. The project is available on GitHub.

## Email Configuration Reference

### TASKER_CONFIG Table

#### Configure New Non-Alert Task

1. Add an entry in the `tasker_request` table.
2. Supported frequencies: `HOURLY`, `EOD`, `SCHEDULED_HOUR`, `SCHEDULED_MIN`, `ONCE`.
3. `EMAIL_IF_PRE_COND_IS_TRUE`: `Y` - Will send email only when the pre-condition is true; for any other value, it will send email always.
4. `PATH`, `FILENAME`: Denotes the configuration file path for the task.

#### Configure New Alert Task

1. Add an entry in the `tasker_request` table with `NAME='ALERT'`.
2. `PATH`: Database in which the alert table exists.
3. `FILENAME`: Alert table name.

## Configuring Tasks

### Non-Alert Tasks

#### Non-Alert Task Configuration File Syntax (Without Semicolon Support)

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
