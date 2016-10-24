alter table PURCHASEINVOICE add column PAYBYDATE bigint;
alter table SALESINVOICE add column PAYBYDATE bigint;
alter table SALESINVOICELINE add column WAREHOUSESITEFO bigint unsigned;
alter table GOODSLOSSLINE add column WAREHOUSESITEFO bigint unsigned;
alter table MANUFACTURE add column WAREHOUSESITEFO bigint unsigned;
alter table USEDMATERIALLINE add column WAREHOUSESITEFO bigint unsigned;
alter table PURCHASERETURNLINE add column WAREHOUSESITEFO bigint unsigned;
insert into ACCENTRIESSOURCESLINE (ITSID, ITSOWNER, FILENAME, ITSVERSION, SOURCETYPE, SETCODE, ISUSED, ENTRIESSOURCETYPE, ENTRIESACCOUNTINGTYPE, SOURCEIDNAME, DESCRIPTION) values (22, 1, 'BeginInvItemCatDbt', 1462867931627, 15, 'InvItemCategory', 1, 0, 1, 'BEGINNINGINVENTORY.ITSID', 'BeginningInventory , Debit Inventory per InvItemCategory.');
insert into DRAWMATERIALSOURCESLINE (ITSID, ITSOWNER, FILENAME, ITSVERSION, SOURCETYPE, USEINMETHODS, ISUSED, DESCRIPTION) values (4, 1, 'drawBeginningInventoryLineM1', 1462867931627, 1009, '1, 2', 1, 'Beginning Inventory Lines where rest > 0');
insert into COGSITEMSOURCESLINE (ITSID, ITSOWNER, FILENAME, ITSVERSION, SOURCETYPE, USEINMETHODS, ISUSED, DESCRIPTION) values (4, 1, 'drawBeginningInventoryLineM1', 1462867931627, 1009, '1, 2', 1, 'Beginning Inventory Lines where rest > 0');
update DRAWMATERIALSOURCESLINE set SOURCETYPE=1006 where ITSID=3;
update DATABASEINFO set DATABASEVERSION=2, DESCRIPTION='BeigeAccounting version 2';
