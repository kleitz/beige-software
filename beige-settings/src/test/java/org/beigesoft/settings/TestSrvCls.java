package org.beigesoft.settings;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import org.beigesoft.log.LoggerSimple;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.UtlReflection;

/**
 * <p>Test of CLS service.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestSrvCls {
  
  LoggerSimple logger = new LoggerSimple();

  @Test
  public void tests() throws Exception {
    MngSettings mngSettings = new MngSettings();
    mngSettings.setUtlProperties(new UtlProperties());
    mngSettings.setUtlReflection(new UtlReflection());
    mngSettings.setLogger(logger);
    mngSettings.loadConfiguration("beige-settings","base.xml");
    assertEquals("select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME=':tableName';",
      mngSettings.getAppSettings().get("checkTableExist"));
    assertEquals(7, mngSettings.getClasses().size());
    assertTrue(mngSettings.getClasses().contains(org.beigesoft.test.persistable.PersistableHead.class));
    assertNull(mngSettings.getClassesSettings().get(org.beigesoft.test.persistable.GoodVersionTime.class).get("constraintAdd"));
    assertNotNull(mngSettings.getClassesSettings().get(org.beigesoft.persistable.UserRoleTomcat.class).get("constraintAdd"));
    assertEquals("itsId", mngSettings.getClassesSettings().get(org.beigesoft.test.persistable.GoodVersionTime.class).get("idName"));
    assertEquals("itsUser", mngSettings.getClassesSettings().get(org.beigesoft.persistable.UserRoleTomcat.class).get("idName"));
    assertEquals("lstActStd", mngSettings.getClassesSettings().get(org.beigesoft.test.persistable.GoodVersionTime.class).get("wdgListActions"));
    assertEquals("lstActDepartm", mngSettings.getClassesSettings().get(org.beigesoft.test.persistable.Department.class).get("wdgListActions"));
    assertEquals("lstActDoc", mngSettings.getClassesSettings().get(org.beigesoft.test.persistable.PersistableHead.class).get("wdgListActions"));
    System.out.println("org.beigesoft.test.persistable.PersistableHead - wdgListActions: " 
      + mngSettings.getClassesSettings().get(org.beigesoft.test.persistable.PersistableHead.class).get("wdgListActions"));
    assertNull(mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("isNew"));
    assertEquals("identity not null primary key", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("itsId")
      .get("sqlDef"));
    assertEquals("integer not null primary key", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Warehouse.class).get("itsId")
      .get("sqlDef"));
    assertEquals("varchar(255)", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("itsName")
      .get("sqlDef"));
    assertEquals(null, mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("itsId")
      .get("wdgNewForm"));
    assertEquals("0", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("itsId")
      .get("orderShowList"));
    assertEquals("-1", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("idDatabaseBirth")
      .get("orderShowList"));
    assertEquals("65", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableHead.class).get("itsTotal")
      .get("orderShowList"));
    assertEquals("inputNumberReadOnly", mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).get("itsId")
      .get("wdgEditForm"));
    String wdgToStringDepartment = mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableHead.class).get("itsDepartment")
      .get("wdgToString");
    assertEquals("toStrHasName", wdgToStringDepartment);
    System.out.println("org.beigesoft.test.persistable.PersistableHead - department - wdgToString: " + wdgToStringDepartment);
    String wdgToStringId = mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableHead.class).get("itsId")
      .get("wdgToString");
    assertEquals("toStrSimple", wdgToStringId);
    System.out.println("org.beigesoft.test.persistable.PersistableHead - id - wdgToString: " + wdgToStringId);
    String wdgToStringDate = mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableHead.class).get("itsDate")
      .get("wdgToString");
    assertEquals("toStrDate", wdgToStringDate);
    System.out.println("org.beigesoft.test.persistable.PersistableHead - date - wdgToString: " + wdgToStringDate);
    assertEquals(4, mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.Department.class).entrySet().size());
    List<Map.Entry<String, Map<String, String>>> lstSortedCleaned =
      mngSettings.makeFldPropLst(org.beigesoft.test.persistable.Department.class, "orderShowList");
    assertEquals(2, lstSortedCleaned.size());
    assertEquals("itsId", lstSortedCleaned.get(0).getKey());
    assertEquals("itsName", lstSortedCleaned.get(1).getKey());
    String wdgNewFormIT = mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableLine.class).get("itsTotal")
      .get("wdgNewForm");
    String wdgNewFormIP = mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableLine.class).get("itsPrice")
      .get("wdgNewForm");
    String wdgNewFormIQ = mngSettings.getFieldsSettings().get(org.beigesoft.test.persistable.PersistableLine.class).get("itsQuantity")
      .get("wdgNewForm");
    assertNull(wdgNewFormIQ);
    assertNull(wdgNewFormIT);
    assertEquals("inputPriceQuantityTotal", wdgNewFormIP);
  }
}
