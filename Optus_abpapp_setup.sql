
 CREATE TABLE TASKER_REQUEST
   (	"NAME" VARCHAR2(200 BYTE), 
	"DESCRIPTION" VARCHAR2(2000 BYTE), 
	"REQUESTOR" VARCHAR2(200 BYTE), 
	"ENABLED" VARCHAR2(2 BYTE), 
	"FREQUENCY" VARCHAR2(200 BYTE), 
	"TIME" VARCHAR2(4000 BYTE), 
	"PATH" VARCHAR2(4000 BYTE), 
	"FILENAME" VARCHAR2(4000 BYTE), 
	"EMAIL_SUBJECT" VARCHAR2(4000 BYTE), 
	"EMAIL_IF_PRE_COND_IS_TRUE" CHAR(1 BYTE), 
	"EMAIL_LIST" VARCHAR2(4000 BYTE), 
	"EMAIL_HEAD" VARCHAR2(4000 BYTE), 
	"LASTEXECUTED" DATE);
	
/*	
Insert into TASKER_REQUEST (NAME,DESCRIPTION,REQUESTOR,ENABLED,FREQUENCY,TIME,PATH,FILENAME,EMAIL_SUBJECT,EMAIL_IF_PRE_COND_IS_TRUE,EMAIL_LIST,EMAIL_HEAD,LASTEXECUTED) values ('TEST','Test','Piyush','Y','HOURLY',null,'/users/gen/abpwrk1/','test.txt','Test',null,'piyushch@amdocs.com',null,null);
Insert into TASKER_REQUEST (NAME,DESCRIPTION,REQUESTOR,ENABLED,FREQUENCY,TIME,PATH,FILENAME,EMAIL_SUBJECT,EMAIL_IF_PRE_COND_IS_TRUE,EMAIL_LIST,EMAIL_HEAD,LASTEXECUTED) 
values ('TEST_V1','Test','Piyush','Y','HOURLY',null,'/users/gen/abpwrk1/','testv1.txt','Test Version 1',null,'piyushch@amdocs.com',null,null);
*/
Insert into TASKER_REQUEST (NAME,DESCRIPTION,REQUESTOR,ENABLED,FREQUENCY,TIME,PATH,FILENAME,EMAIL_SUBJECT,EMAIL_IF_PRE_COND_IS_TRUE,EMAIL_LIST,EMAIL_HEAD,LASTEXECUTED) 
values ('ALERT','Application alerts','BCC','Y','BYREQ',null,'abpapp','TASKER_ALERT_REQUEST',null,null,'piyushch@amdocs.com','Application Alerts Report',null);

commit;

CREATE TABLE TASKER_CONFIG
   (	"NAME" VARCHAR2(200 BYTE) unique, "VALUE" VARCHAR2(2000 BYTE),"DESCRIPTION"  VARCHAR2(1000 BYTE));
   Insert into TASKER_CONFIG (NAME,VALUE,DESCRIPTION) values ('table.head.color','#1865a0','Report Table head color');
 insert into TASKER_CONFIG values ('report.font','Trebuchet MS','Report font');
insert into TASKER_CONFIG values ('alert.font','Trebuchet MS','Alert font');
insert into TASKER_CONFIG values ('table.alert.head.notifcolor','#dedee0','Alert Notif color');
      insert into TASKER_CONFIG values ('table.head.color','#1865a0','Report Table head color');
   insert into TASKER_CONFIG values ('table.row.color','#d9edfc','Report Table row color');

   insert into TASKER_CONFIG values ('table.row.fontsize','12','Report table Font size');
   insert into TASKER_CONFIG values ('table.text.color','#333333','Report Table text color');
   insert into TASKER_CONFIG values ('runinterval','-1','After how many minutes report should be genetared ,keep -1 if BYREQ');
   insert into TASKER_CONFIG values ('mail.from','TASKER@amdocs.com','From email address of SMTP server');
   insert into TASKER_CONFIG values ('mail.subject','','Prefix for each mail send from Tasker if needed');
   insert into TASKER_CONFIG values ('printlog','TRUE','');
   insert into TASKER_CONFIG values ('table.alert.head.color','#515152','Alert Table head color');
   insert into TASKER_CONFIG values ('table.alert.row.color','#dedee0','Alert Table row color');
     insert into TASKER_CONFIG values ('head.alert.row.fontsize','20','Alert Head Font size');
   insert into TASKER_CONFIG values ('table.alert.row.fontsize','14','Alert table Font size');
   insert into TASKER_CONFIG values ('table.alert.text.color','#333333','Alert Table text color');
 insert into TASKER_CONFIG values ('table.alert.border.color','#ffffff','Alert Table text color');
  insert into TASKER_CONFIG values ('table.alert.border.size','2','Alert Table text color');
   insert into TASKER_CONFIG values ('table.border.color','#ffffff','Alert Table text color');
  insert into TASKER_CONFIG values ('table.border.size','1','Alert Table text color');
      insert into TASKER_CONFIG values ('table.alert.head.textcolor','#ffffff','Alert Table text color');
  insert into TASKER_CONFIG values ('table.head.textcolor','#ffffff','Report Table text color');
  insert into TASKER_CONFIG values ('table.alert.head.criticalcolor','#EC0000','Alert Table critical color');
 insert into TASKER_CONFIG values ('table.alert.head.warningcolor','#FFE666','Alert Table warning color');
 insert into TASKER_CONFIG values ('home.env','amc','Tasker Host machine');
insert into TASKER_CONFIG values ('home.db','abpapp','Tasker default DB');
insert into TASKER_CONFIG values ('head.row.fontsize','14','Report Head font size');
insert into TASKER_CONFIG values ('alert.row.paint','full','Fill style Alert color in row ');
commit;


CREATE TABLE TASKER_ALERT_REQUEST  (
ALERT_ID NUMBER(8) NOT NULL UNIQUE,
ALERT_GROUP         VARCHAR2(60)  NOT NULL ,
ALERT_NAME       VARCHAR2(2000) NOT NULL  ,
ALERT_SCRIPT       CLOB  ,
IS_VALID                 VARCHAR2(2)   ,
THRESHOLD_condition    VARCHAR2(60) NOT NULL,
NOTIFY_THRESHOLD    NUMBER(8) ,
WARNING_THRESHOLD   NUMBER(8)  NOT NULL, 
CRITICAL_THRESHOLD  NUMBER(8) NOT NULL,      

ALERT_TYPE          VARCHAR2(20) ,
ALERT_USER_HOST                 VARCHAR2(4000) , 
APPLICATION_DB VARCHAR2(60) ,
PRIORITY        NUMBER(7) NOT NULL,
ATTR_VALUE1  VARCHAR2(2000),
ATTR_VALUE2  VARCHAR2(2000));
/*
Insert into TASKER_ALERT_REQUEST values (1,'BCC','TRB PUB LOG','select count(1) from trb1_pub_log','Y','GREATER_THAN',-1,200,500,'QUERY',null,'abpapp',1,null,null);
Insert into TASKER_ALERT_REQUEST values (2,'BCC','TRB SUB LOG','select count(1) from trb1_sub_log','Y','GREATER_THAN',-1,200,500,'QUERY',null,'abpapp',2,null,null);
Insert into TASKER_ALERT_REQUEST values (3,'BCC','TRB SUB ERR','select count(1) from trb1_sub_errs','Y','LESS_THAN',-1,100,200,'QUERY',null,'abpapp',3,null,null);
Insert into TASKER_ALERT_REQUEST  values (4,'BCC','Pbin File count','ls -ltr /users/gen/abpwrk1/pbin/app | wc -l','Y','GREATER_THAN',-1,30,40,'COMMAND',null,null,4,null,null);
Insert into TASKER_ALERT_REQUEST  values (5,'BCC','Pbin File count Remote','ls -ltr /users/gen/abpwrk1/pbin/app | wc -l','Y','LESS_THAN',-1,40,800,'COMMAND','abpwrk1@inlnqw3240',null,3,null,null);
Insert into TASKER_ALERT_REQUEST values (6,'BCC','Failed tbauto_request in OMS','select count(1) from tbauto_request where REQUEST_STATUS=''FA''','Y','GREATER_THAN',-1,200,500,'QUERY',null,'omsapp',1,null,null);
Insert into TASKER_ALERT_REQUEST values (7,'OMS','Failed tbauto_request in OMS','select count(1) from tbauto_request where REQUEST_STATUS=''FA''','Y','GREATER_THAN',-1,200,500,'QUERY',null,'omsapp',1,null,null);

Insert into TASKER_ALERT_REQUEST values (8,'BCC','Stuck Orders tbmessage in OMS','select count(1) from tbmessage','Y','GREATER_THAN',-1,200,500,'QUERY',null,'omsapp',1,null,null);

Insert into TASKER_ALERT_REQUEST values (9,'BCC','CRM BAP errors table_bg_action','select count(1) from table_bg_action','Y','GREATER_THAN',-1,200,500,'QUERY',null,'crmapp',1,null,null);
Insert into TASKER_ALERT_REQUEST values (10,'CRM','CRM BAP errors table_bg_action','select count(1) from table_bg_action','Y','GREATER_THAN',-1,200,500,'QUERY',null,'crmapp',1,null,null);
commit;

update tasker_request set PATH='abpapp', FILENAME='TASKER_ALERT_REQUEST',DESCRIPTION='Application alerts ' where NAME='ALERT';
commit;
*/