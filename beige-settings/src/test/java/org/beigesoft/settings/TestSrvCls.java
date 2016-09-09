package org.beigesoft.settings;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import org.beigesoft.log.LoggerSimple;

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
    mngSettings.setLogger(logger);
    mngSettings.loadConfiguration("beige-settings","base.xml");
    assertEquals("select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME=':tableName';",
      mngSettings.getAppSettings().get("checkTableExist"));
    assertEquals(6, mngSettings.getClasses().size());
    assertTrue(mngSettings.getClasses().contains(org.beigesoft.test.persistable.PersistableHead.class));
    assertNull(mngSettings.getClassesSettings().get("org.beigesoft.test.persistable.GoodVersionTime").get("constraintAdd"));
    assertNotNull(mngSettings.getClassesSettings().get("org.beigesoft.persistable.UserRoleTomcat").get("constraintAdd"));
    assertEquals("itsId", mngSettings.getClassesSettings().get("org.beigesoft.test.persistable.GoodVersionTime").get("idName"));
    assertEquals("itsUser", mngSettings.getClassesSettings().get("org.beigesoft.persistable.UserRoleTomcat").get("idName"));
    assertEquals("lstActStd", mngSettings.getClassesSettings().get("org.beigesoft.test.persistable.GoodVersionTime").get("wdgListActions"));
    assertEquals("lstActDepartm", mngSettings.getClassesSettings().get("org.beigesoft.test.persistable.Department").get("wdgListActions"));
    assertEquals("lstActDoc", mngSettings.getClassesSettings().get("org.beigesoft.test.persistable.PersistableHead").get("wdgListActions"));
    System.out.println("org.beigesoft.test.persistable.PersistableHead - wdgListActions: " 
      + mngSettings.getClassesSettings().get("org.beigesoft.test.persistable.PersistableHead").get("wdgListActions"));
    assertNull(mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("isNew"));
    assertEquals("identity not null primary key", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("itsId")
      .get("sqlDef"));
    assertEquals("integer not null primary key", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Warehouse").get("itsId")
      .get("sqlDef"));
    assertEquals("varchar(255)", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("itsName")
      .get("sqlDef"));
    assertEquals(null, mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("itsId")
      .get("wdgNewForm"));
    assertEquals("0", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("itsId")
      .get("orderShowList"));
    assertEquals("-1", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("idDatabaseBirth")
      .get("orderShowList"));
    assertEquals("65", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableHead").get("itsTotal")
      .get("orderShowList"));
    assertEquals("inputNumberReadOnly", mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").get("itsId")
      .get("wdgEditForm"));
    String wdgToStringDepartment = mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableHead").get("itsDepartment")
      .get("wdgToString");
    assertEquals("toStrHasName", wdgToStringDepartment);
    System.out.println("org.beigesoft.test.persistable.PersistableHead - department - wdgToString: " + wdgToStringDepartment);
    String wdgToStringId = mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableHead").get("itsId")
      .get("wdgToString");
    assertEquals("toStrSimple", wdgToStringId);
    System.out.println("org.beigesoft.test.persistable.PersistableHead - id - wdgToString: " + wdgToStringId);
    String wdgToStringDate = mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableHead").get("itsDate")
      .get("wdgToString");
    assertEquals("toStrDate", wdgToStringDate);
    System.out.println("org.beigesoft.test.persistable.PersistableHead - date - wdgToString: " + wdgToStringDate);
    assertEquals(4, mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.Department").entrySet().size());
    List<Map.Entry<String, Map<String, String>>> lstSortedCleaned =
      mngSettings.makeFldPropLst("org.beigesoft.test.persistable.Department", "orderShowList");
    assertEquals(2, lstSortedCleaned.size());
    assertEquals("itsId", lstSortedCleaned.get(0).getKey());
    assertEquals("itsName", lstSortedCleaned.get(1).getKey());
    String wdgNewFormIT = mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableLine").get("itsTotal")
      .get("wdgNewForm");
    String wdgNewFormIP = mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableLine").get("itsPrice")
      .get("wdgNewForm");
    String wdgNewFormIQ = mngSettings.getFieldsSettings().get("org.beigesoft.test.persistable.PersistableLine").get("itsQuantity")
      .get("wdgNewForm");
    assertNull(wdgNewFormIQ);
    assertNull(wdgNewFormIT);
    assertEquals("inputPriceQuantityTotal", wdgNewFormIP);
  }
}
