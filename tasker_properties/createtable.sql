create table tasker_Request
(
   Name varchar(200),
   Description  varchar(2000),
   Requestor varchar(200),
   Enabled varchar(2),
   Frequency varchar(200),
   Time varchar(4000),
   Path varchar(4000),
   Filename  varchar(4000),
   Email_Subject varchar(4000),
   Email_IF_PRE_COND_IS_TRUE char(1),
   Email_list varchar(4000),   
   Email_head varchar(4000),  
   LastExecuted date
);
drop table tasker_Request;
grant all on tasker_Request to public ;

update al_Request set LastExecuted=sysdate where rowid='AAA6IrAAFAAGQ7wAAA';
UPDATE TASKER_REQUEST SET LASTEXECUTED=SYSDATE WHERE ROWID='AAA6ISAAFAAGQ7WAAA';
UPDATE TASKER_REQUEST SET Email_list='shikha.shekhar@amdocs.com' WHERE ROWID='AAA6IsAAFAAGQ7wAAA';

select ROWID,tasker_Request.* from tasker_Request where Enabled='Y';

insert into tasker_Request values ('testreport','testing alfred','Piyush','Y','HOURLY',null,'/users/gen/abpwrk1/var/bss/projs/cm/','alfredtest.txt','Test Email from Tasker','Y','piyushch@amdocs.com','',null);
commit;

delete tasker_Request where EMAIL_LIST like '%shikha%';
commit;

update tasker_Request set path='/users/gen/abpwrk1/';

select * from tasker_Request;

create table tasker_test
(
   Name varchar(200)
);

insert into tasker_test values ('abc1');
insert into tasker_test values ('abc2');
insert into tasker_test values ('abc3');
insert into tasker_test values ('abc4');
commit;

select * from tasker_test;
 DELETE from tasker_test where Name='abc4' ;
 
 select ROWID,tasker_Request.* from tasker_Request where Enabled='Y';
 create table table1 as (select * from tasker_test);
 
 select * from table1;
 drop table table1;

