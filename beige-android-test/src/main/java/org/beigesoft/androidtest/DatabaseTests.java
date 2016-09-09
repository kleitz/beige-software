package org.beigesoft.androidtest;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static org.junit.Assert.assertEquals;

import org.beigesoft.android.log.Logger;
import org.beigesoft.android.sqlite.service.SrvDatabase;
import org.beigesoft.android.sqlite.service.CursorFactory;
import org.beigesoft.android.sqlite.service.SrvRecordRetriever;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.service.SrvOrmAndroid;
import org.beigesoft.orm.test.TestSimple;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.test.persistable.Department;

/**
 * <p>Tests of database service for Android.
 * </p>
 *
 * @author Yury Demidenko
 */
public class DatabaseTests extends android.test.AndroidTestCase {

  /**
   * <p>ID DB tests.</p>
   **/
  public static final Integer ID_DATABASE = 999;

  /**
   * <p>Database service.</p>
   **/
  private SrvDatabase srvDatabase;

  /**
   * <p>ORM service.</p>
   **/
  private SrvOrmAndroid<Cursor> srvOrm;

  /**
   * <p>Perform simple (non-concurrence) tests.</p>
   * @throws Exception an exception
   **/
  public void testAll() throws Exception {
    //BeanELResolver beanELResolver = new BeanELResolver();
    Context context = getContext();
    Logger log = new Logger();
    log.setIsShowDebugMessages(true);
    SQLiteDatabase db = context.openOrCreateDatabase ("dbtest.sqlite",
     Context.MODE_PRIVATE, new CursorFactory());
    srvDatabase = new SrvDatabase();
    srvDatabase.setSqliteDatabase(db);
    srvDatabase.setLogger(log);
    srvOrm = new SrvOrmAndroid<Cursor>();
    srvOrm.setSrvSqlEscape(new SrvSqlEscape());
    srvOrm.setIsNeedsToSqlEscape(false);
    SrvRecordRetriever srvRecordRetriever = new SrvRecordRetriever();
    srvOrm.setSrvRecordRetriever(srvRecordRetriever);
    srvOrm.setSrvDatabase(srvDatabase);
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(log);
    srvOrm.setLogger(log);
    srvOrm.setMngSettings(mngSettings);
    log.debug(DatabaseTests.class, 
      "loading configuration: beige-orm, persistence-sqlite.xml");
    srvOrm.loadConfiguration("beige-orm", "persistence-sqlite.xml");
    assertEquals("integer not null primary key autoincrement",
      srvOrm.getMngSettings().getFieldsSettings()
                .get(RoleJetty.class.getCanonicalName()).get("itsId")
                  .get("definition"));
    TestSimple<Cursor> testSimple = new TestSimple<Cursor>();
    testSimple.setSrvDatabase(srvDatabase);
    testSimple.setLogger(log);
    testSimple.setSrvOrm(srvOrm);
    testSimple.doTest1();
    srvDatabase.setIsAutocommit(true);
    String dep3insert = "insert into DEPARTMENT  (ITSID, ITSNAME, IDDATABASEBIRTH ) values (3, 'dp3', 999);";
    String dep4insert = "insert into DEPARTMENT  (ITSID, ITSNAME, IDDATABASEBIRTH ) values (4, 'dp4', 999);";
    Department dp3 = srvOrm.retrieveEntityById(Department.class, "3");
    if (dp3 == null) {
      srvDatabase.executeQuery(dep3insert);
      dp3 = srvOrm.retrieveEntityById(Department.class, "3");
      assertNotNull(dp3);
      srvOrm.deleteEntity(dp3);
      dp3 = srvOrm.retrieveEntityById(Department.class, "3");
      assertNull(dp3);
    }
    Department dp4 = srvOrm.retrieveEntityById(Department.class, "4");
    if (dp4 == null) {
      srvDatabase.executeQuery(dep4insert);
      dp4 = srvOrm.retrieveEntityById(Department.class, "4");
      assertNotNull(dp4);
      srvOrm.deleteEntity(dp4);
      dp4 = srvOrm.retrieveEntityById(Department.class, "4");
      assertNull(dp4);
    }
    boolean isFallGroupInsert = false;
    try {
      srvDatabase.executeQuery(dep3insert + "\n" + dep4insert);
    } catch (Exception e) {
      isFallGroupInsert = true;
    }
    assertTrue(!isFallGroupInsert);
    srvOrm.deleteEntity(Department.class, "3");
    srvOrm.deleteEntity(Department.class, "4");
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvDatabase.</p>
   * @return SrvDatabase
   **/
  public final SrvDatabase getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference/value
   **/
  public final void setSrvDatabase(final SrvDatabase pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return SrvOrmAndroid
   **/
  public final SrvOrmAndroid getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final SrvOrmAndroid pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
}
