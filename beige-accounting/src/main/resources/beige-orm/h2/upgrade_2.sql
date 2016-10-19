alter table PURCHASEINVOICE add column PAYBYDATE bigint;
alter table SALESINVOICE add column PAYBYDATE bigint;
update DATABASEINFO set DATABASEVERSION=2, DESCRIPTION='BeigeAccounting version 2';
