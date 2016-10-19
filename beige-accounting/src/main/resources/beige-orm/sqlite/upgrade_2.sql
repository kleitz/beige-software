alter table PURCHASEINVOICE add column PAYBYDATE integer;
alter table SALESINVOICE add column PAYBYDATE integer;
update DATABASEINFO set DATABASEVERSION=2, DESCRIPTION='BeigeAccounting version 2';
