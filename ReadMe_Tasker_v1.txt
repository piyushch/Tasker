******************************************************************************** ReadMe Tasker  - version 1 ************************************************************************************************************************



-----------------------------------------------------------
Email configuration refrrence table - TASKER_CONFIG
---------------------------------------------------------------

**********Configure new non ALert Task 
1) Add entry in tasker_request table 
2) Supported frequencies - HOURLY,EOD,SCHEDULED_HOUR,SCHEDULED_MIN,ONCE
3) EMAIL_IF_PRE_COND_IS_TRUE - Y - Will send email only when Pre-Condition is true, for any other value will send email always 
4) PATH ,FILENAME - denotes configuration file path for task

---------------------------------------------------------------

**********Configure new ALERT Task 
1) Add entry in tasker_request table with NAME='ALERT'2
2) PATH- database in which alert table exists 
3) FILENAME- ALERT table name 

---------------------------------------------------------------
--------------------------------------------------------------

Note - 
	if precondition line is Optional
	[-><dbkeyword>] is optional


**********Non ALERT TASK CONFIG file syntax -- Without semicolon support

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

Note - 
	for <keyword>  - check "Keywords for Tasks" list below 
	<dbkeyword>    - check "DB Keywords for Tasks/ALERT" list below 


---------------------------------------------------------------
**********Non ALERT TASK CONFIG file syntax -- With semicolon support

[if <cnt_variable_name> [>,<,=,!=] <value>]
Run <keyword> <variable_name> and <keyword> <variable_name> and <keyword> <variable_name> ........... ;
Declare_v1
<cnt_variable_name>-> Query/command[-><dbkeyword>]->END;
<variable_name> -> Query/command ->Y->END;
<variable_name> -> Query/command ->Y->END;
.
.
.


---------------------------------------------------------------
--------------------------------------------------------------

**********Configure ALERT

Sample Insert - Insert into TASKER_ALERT_REQUEST values (1,'BCC','TRB PUB LOG','select count(1) from trb1_pub_log','Y','GREATER_THAN',-1,200,500,'QUERY',null,'abpapp',1,null,null);
Notes -

ALERT_GROUP - TO group alerts in saperate Emails , we can run one GRoup at a time or all together 
THRESHOLD_CONDITION- LESS_THAN / GREATER_THAN of the values given in successding colunms
NOTIFY_THRESHOLD - put "-1" if alert need to shown always in report else Alert will not be shown in Email if NOTIFY_THRESHOLD is not satisfied
ALERT_TYPE- QUERY/COMMAND
ALERT_USER_HOST, APPLICATION_DB- can put comma saperated multiple user@hosts or database keywords ,each db/host will be a new rows in Email
PRIORITY, ATTR_VALUE1, ATTR_VALUE2 - NOT in USE 




---------------------------------------------------------------
--------------------------------------------------------------
How to RUN 

To Run as Daemon 
Set runinterval in table tasker_config , After how many minutes report should be genetared ,

To Run as One time script keep runinterval  -1 

 
Run any report BY Request 
 command - "Tasker <REPORT_NAME>"

Generate ALERT by request 
 command - "Tasker ALERT <ALERT_GROUP>" - to run single Group
 command - "Tasker ALERT '*'" - to run all Group


---------------------------------------------------------------
--------------------------------------------------------------

*************** Keywords for Tasks
---FOR SQL queries 
select 
update
delete
grant
insert
backup  --- Create backup table from select query 
drop
create
export  -- Attach select query out in Excel sheet 

----To Fetch File from location
attach

--- To execute unix command
execute

---------------------------------------------------------------
--------------------------------------------------------------

*************** DB Keywords for Tasks/ALERT
abpapp
omsapp
crmapp
abpopr
epcapp
abpusg1
abpusg2
abpusg3
abpusg4
abpusg5
anmapp
falapp
mcoapp
asomapp
mcssapp
mcssebl









 
