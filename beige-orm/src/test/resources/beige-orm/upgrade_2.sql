insert into USERTOMCAT values ('admin_upgrade', 'admin_upgrade');
insert into USERROLETOMCAT  values ('admin_upgrade', 'admin_upgrade');
update DATABASEINFO set DATABASEVERSION=2, DESCRIPTION='Test database upgraded' where DATABASEID=999;
