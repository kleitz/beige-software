insert into DEBTORCREDITORCATEGORY (ITSID, ITSVERSION, ITSNAME) values (1, 1462867334713,'Buyers STD');
insert into DEBTORCREDITORCATEGORY (ITSID, ITSVERSION, ITSNAME) values (2, 1462867334713,'Vendors STD');

insert into DEBTORCREDITOR (ITSID, ITSVERSION, ITSNAME, ITSCATEGORY, REGPHONE, REGADDRESS1) values (15626875423, 1462867334713, 'Carlo''s grocery', 2, '+1 562 687 54 23', 'Grove st.1');
insert into DEBTORCREDITOR (ITSID, ITSVERSION, ITSNAME, ITSCATEGORY, REGPHONE, REGADDRESS1) values (15626171411, 1462867334713, 'Mini-market', 1, '+1 562 617 14 11', 'Mice st.4');
insert into DEBTORCREDITOR (ITSID, ITSVERSION, ITSNAME, ITSCATEGORY, REGPHONE, REGADDRESS1) values (91191191101, 1462867334713, 'Funny coffee shop', 1, '?', '?');

insert into WAREHOUSE (ITSID, ITSNAME, ITSVERSION) values (1, 'Storeroom', 1462867931627);
insert into WAREHOUSE (ITSID, ITSNAME, ITSVERSION) values (2, 'Kitchen', 1462867931627);

insert into WAREHOUSESITE (ITSID, WAREHOUSE, ITSNAME, ITSVERSION) values (1, 1, 'Shelf#1', 1462867931627);
insert into WAREHOUSESITE (ITSID, WAREHOUSE, ITSNAME, ITSVERSION) values (2, 1, 'Shelf#2', 1462867931627);
insert into WAREHOUSESITE (ITSID, WAREHOUSE, ITSNAME, ITSVERSION) values (3, 2, 'Cabinet#1', 1462867931627);
insert into WAREHOUSESITE (ITSID, WAREHOUSE, ITSNAME, ITSVERSION) values (4, 2, 'Cabinet#2', 1462867931627);

insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('SST wh.', 3, 1, 6.2000, 1465288228645, 3, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('Medicare wh.', 4, 2, 1.4500, 1465288228645, 3, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('F. Income Tax wh.', 5, 3, 29.000, 1465288228645, 3, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('NY income tax wh.', 7, 4, 9.6200, 1465288228645, 3, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('FUTA wh.', 6, 5, 6.0000, 1465288228645, 4, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('Sales tax fake', 11, 6, 6.0000, 1465288228645, 2, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('NY FST hot food', 11, 7, 6.0000, 1465288228645, 2, 0, 0);
insert into TAX (ITSNAME, EXPENSE, ITSID, ITSPERCENTAGE, ITSVERSION, ITSTYPE, DUEMETHOD, PLUSAMOUNT) values ('F. FST hot food', 11, 8, 3.0000, 1465288228645, 2, 0, 0);

insert into INVITEMCATEGORY (ITSNAME, ITSID, ITSVERSION) values ('Pizza Ingredients', 1, 1462867931627);
insert into INVITEMCATEGORY (ITSNAME, ITSID, ITSVERSION) values ('Pizza cheese frozen', 2, 1462867931627);
insert into INVITEMCATEGORY (ITSNAME, ITSID, ITSVERSION) values ('Pizza bacon frozen', 3, 1462867931627);
insert into INVITEMCATEGORY (ITSNAME, ITSID, ITSVERSION) values ('Pizza in progress', 4, 1462867931627);

insert into INVITEMTAXCATEGORY (ITSNAME, ITSID, IDBIRTH, ITSVERSION, IDDATABASEBIRTH) values ('Pizza hot', 1, null, 1462867931627, 1);

insert into INVITEMTAXCATEGORYLINE (ITSOWNER, ITSID, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, TAX, ITSPERCENTAGE) values (1, 1, null, 1462867931627, 1, 7, 6.0);
insert into INVITEMTAXCATEGORYLINE (ITSOWNER, ITSID, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, TAX, ITSPERCENTAGE) values (1, 2, null, 1462867931627, 1, 8, 3.0);

insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (5, 'Flour', 1, 0.25, null, 1463737121668, 1, 2, 1, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Egg', 2, null, null, 1463737121668, 1, 2, 1, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza bacon frozen', 3, null, null, 1463737121668, 1, 4, 3, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza bacon in progress', 4, null, null, 1463737121668, 1, 3, 4, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (5, 'Pastry', 5, null, null, 1463737121668, 1, 2, 1, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (5, 'Cheese', 6, null, null, 1463737121668, 1, 2, 1, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (5, 'Bacon', 7, null, null, 1463737121668, 1, 2, 1, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (5, 'Salt', 8, null, null, 1463737121668, 1, 2, 1, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza cheese frozen', 9, null, null, 1463737121668, 1, 4, 2, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza cheese in progress', 10, null, null, 1463737121668, 1, 3, 4, null);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza cheese hot#1', 11, null, null, 1463737121668, 1, 4, 2, 1);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza cheese hot#2', 12, null, null, 1463737121668, 1, 4, 2, 1);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza cheese hot#3', 13, null, null, 1463737121668, 1, 4, 2, 1);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza bacon hot#1', 14, null, null, 1463737121668, 1, 4, 3, 1);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza bacon hot#2', 15, null, null, 1463737121668, 1, 4, 3, 1);
insert into INVITEM (DEFUNITOFMEASURE, ITSNAME, ITSID, KNOWNCOST, IDBIRTH, ITSVERSION, IDDATABASEBIRTH, ITSTYPE, ITSCATEGORY, TAXCATEGORY) values (1, 'Pizza bacon hot#3', 16, null, null, 1463737121668, 1, 4, 3, 1);

insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Direct Labor', 1, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Indirect Labor', 2, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Social Security withholding', 3, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Medicare withholding', 4, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Federal Income Tax withholding', 5, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Federal Unemployment Tax withholding', 6, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('State Income Tax withholding', 7, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('State Unemployment Tax withholding', 8, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Rent', 9, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Property Deprecation', 10, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Sales tax', 11, 1462867931627);
insert into EXPENSE (ITSNAME, ITSID, ITSVERSION) values ('Delivery', 12, 1462867931627);

insert into BANKACCOUNT (ITSNAME, ITSID, ITSVERSION) values ('#1236786817 in US bank', 1, 1462867931627);

insert into PROPERTY (ITSNAME, ITSID, ITSVERSION) values ('Ford TRANSIT CONNECT', 1, 1462867931627);

insert into EMPLOYEECATEGORY (ITSNAME, ITSID, ITSVERSION) values ('Cooks', 1, 1462867931627);
insert into EMPLOYEECATEGORY (ITSNAME, ITSID, ITSVERSION) values ('Pizza deliverers', 2, 1462867931627);

insert into EMPLOYEE (ITSNAME, ITSID, ITSCATEGORY, TAXIDENTIFICATIONNUMBER, ITSVERSION, DATEHIRE) values ('Rob Swallow', 123424599, 1, '123-42-4599', 1462867931627, 1470143258862);

insert into WAGETYPE (ITSNAME, ITSID, EXPENSE, ITSVERSION) values ('Cooking', 1, 1, 1462867931627);
insert into WAGETYPE (ITSNAME, ITSID, EXPENSE, ITSVERSION) values ('Pizza delivery', 2, 2, 1462867931627);

insert into WAGETAXTABLE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSNAME, TAX) values (1, 1463737121668, 1, '2016 IRS-ETG FIT Table 8 daily', 3);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (1, 1463737121668, 1, 1, 0, 0, 9999999.99, 8.7, 44.3, 10, 0);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (2, 1463737121668, 1, 1, 0, 0, 9999999.99, 44.3, 153.5, 15, 3.56);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (3, 1463737121668, 1, 1, 0, 0, 9999999.99, 153.5, 359.2, 25, 19.94);
insert into WAGETAXTABLEEMPLOYEE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, EMPLOYEE) values (1, 1463737121668, 1, 1, 31.2, 123424599);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (1, 1463737121668, 1, 1, 1);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (2, 1463737121668, 1, 1, 2);

insert into WAGETAXTABLE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSNAME, TAX) values (2, 1463737121668, 1, '2016 IRS-ETG MT all', 2);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (4, 1463737121668, 1, 2, 0, 0, 9999999.99, 0, 9999999.99, 1.45, 0);
insert into WAGETAXTABLEEMPLOYEE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, EMPLOYEE) values (2, 1463737121668, 1, 2, 0, 123424599);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (3, 1463737121668, 1, 2, 1);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (4, 1463737121668, 1, 2, 2);

insert into WAGETAXTABLE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSNAME, TAX) values (3, 1463737121668, 1, '2016 IRS-ETG SST all', 1);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (5, 1463737121668, 1, 3, 0, 0, 118500.00, 0, 118500.00, 6.2, 0);
insert into WAGETAXTABLEEMPLOYEE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, EMPLOYEE) values (3, 1463737121668, 1, 3, 0, 123424599);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (5, 1463737121668, 1, 3, 1);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (6, 1463737121668, 1, 3, 2);

insert into WAGETAXTABLE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSNAME, TAX) values (4, 1463737121668, 1, '2016 IRS-ETG FUTA all', 5);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (6, 1463737121668, 1, 4, 0, 0, 7000.00, 0, 7000.00, 6, 0);
insert into WAGETAXTABLEEMPLOYEE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, EMPLOYEE) values (4, 1463737121668, 1, 4, 0, 123424599);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (7, 1463737121668, 1, 4, 1);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (8, 1463737121668, 1, 4, 2);

insert into WAGETAXTABLE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSNAME, TAX) values (5, 1463737121668, 1, '2016 NYS NYIT daily', 4);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (7, 1463737121668, 1, 5, 0, 0, 9999999.99, 0, 33, 4, 0);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (8, 1463737121668, 1, 5, 33, 0, 9999999.99, 33, 45, 4.5, 1.3);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (9, 1463737121668, 1, 5, 45, 0, 9999999.99, 45, 53, 5.25, 1.85);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (10, 1463737121668, 1, 5, 53, 0, 9999999.99, 53, 82, 5.9, 2.3);
insert into WAGETAXTABLELINE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, YEARWAGEFROM, YEARWAGETO, WAGEFROM, WAGETO, ITSPERCENTAGE, PLUSAMOUNT) values (11, 1463737121668, 1, 5, 82, 0, 9999999.99, 82, 308, 6.45, 3.99);
insert into WAGETAXTABLEEMPLOYEE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, ALLOWANCE, EMPLOYEE) values (5, 1463737121668, 1, 5, 36.15, 123424599);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (9, 1463737121668, 1, 5, 1);
insert into WAGETAXTABLETYPE (ITSID, ITSVERSION, IDDATABASEBIRTH, ITSOWNER, WAGETYPE) values (10, 1463737121668, 1, 5, 2);

insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (1, 'Pizza', 'all pizza', 1491312192581, 1, 1, 1);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (11, 'Pizza frozen', 'all frozen pizza', 1491309079794, 1, 1, 11);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (12, 'Pizza hot', 'all hot pizza', 1491309644397, 1, 1, 12);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (2, 'On start', 'on start pizza', 1491312192581, 0, 0, 0);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (3, 'Promouted', null, 1491312192581, 0, 1, 3);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (111, 'Vegetarian frozen', 'frozen', 1491309803376, 0, 1, 111);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (112, 'With meat frozen', 'frozen', 1491310024920, 0, 1, 112);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (121, 'Vegetarian hot', 'hot', 1491309795302, 0, 1, 121);
insert into CATALOGGS (ITSID, ITSNAME, DESCRIPTION, ITSVERSION, HASSUBCATALOGS, ISINMENU, ITSINDEX) values (122, 'With meat hot', 'hot', 1491309881104, 0, 1, 122);

insert into SUBCATALOGSCATALOGSGS (ITSCATALOG, SUBCATALOG, ITSVERSION) values(1, 11, 1491309803376);
insert into SUBCATALOGSCATALOGSGS (ITSCATALOG, SUBCATALOG, ITSVERSION) values(1, 12, 1491309803376);
insert into SUBCATALOGSCATALOGSGS (ITSCATALOG, SUBCATALOG, ITSVERSION) values(11, 111, 1491309803376);
insert into SUBCATALOGSCATALOGSGS (ITSCATALOG, SUBCATALOG, ITSVERSION) values(11, 112, 1491309803376);
insert into SUBCATALOGSCATALOGSGS (ITSCATALOG, SUBCATALOG, ITSVERSION) values(12, 121, 1491309803376);
insert into SUBCATALOGSCATALOGSGS (ITSCATALOG, SUBCATALOG, ITSVERSION) values(12, 122, 1491309803376);
