package org.beigesoft.androidtest;

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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static org.junit.Assert.assertEquals;

import org.beigesoft.android.log.Logger;
import org.beigesoft.android.sqlite.service.SrvDatabase;
import org.beigesoft.android.sqlite.service.CursorFactory;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.service.SrvOrmAndroid;
import org.beigesoft.orm.test.TestSimple;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.test.persistable.Department;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.orm.service.HlpInsertUpdate;

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
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection = new UtlReflection();

  /**
   * <p>Perform simple (non-concurrence) tests.</p>
   * @throws Exception an exception
   **/
  public void testAll() {
    try {
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
      srvOrm.setSrvDatabase(srvDatabase);
      srvOrm.setNewDatabaseId(999);
      srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
      srvOrm.setLogger(log);
      srvOrm.setUtlReflection(getUtlReflection());
      MngSettings mngSettings = new MngSettings();
      mngSettings.setLogger(log);
      mngSettings.setUtlProperties(new UtlProperties());
      mngSettings.setUtlReflection(getUtlReflection());
      srvOrm.setLogger(log);
      srvOrm.setMngSettings(mngSettings);
      log.debug(null, DatabaseTests.class, 
        "loading configuration: beige-orm, persistence-sqlite.xml");
      srvOrm.loadConfiguration("beige-orm", "persistence-sqlite.xml");
      FctBnCnvIbnToColumnValues facConvFields = new FctBnCnvIbnToColumnValues();
      facConvFields.setUtlReflection(getUtlReflection());
      facConvFields.setTablesMap(srvOrm.getTablesMap());
      HolderRapiGetters hrg = new HolderRapiGetters();
      hrg.setUtlReflection(getUtlReflection());
      facConvFields.setGettersRapiHolder(hrg);
      HolderRapiFields hrf = new HolderRapiFields();
      hrf.setUtlReflection(getUtlReflection());
      facConvFields.setFieldsRapiHolder(hrf);
      facConvFields.setSrvSqlEscape(new SrvSqlEscape());
      facConvFields.setIsNeedsToSqlEscape(false);
      FctBcCnvEntityToColumnsValues fcetcv = new FctBcCnvEntityToColumnsValues();
      HldCnvToColumnsValuesNames hldConvFld = new HldCnvToColumnsValuesNames();
      hldConvFld.setFieldsRapiHolder(hrf);
      fcetcv.setLogger(log);
      fcetcv.setTablesMap(srvOrm.getTablesMap());
      fcetcv.setFieldsConvertersNamesHolder(hldConvFld);
      fcetcv.setGettersRapiHolder(hrg);
      fcetcv.setFieldsRapiHolder(hrf);
      fcetcv.setFieldsConvertersFatory(facConvFields);
      srvOrm.setFactoryCnvEntityToColumnsValues(fcetcv);
      assertEquals("integer not null primary key autoincrement",
        srvOrm.getMngSettings().getFieldsSettings()
                  .get(RoleJetty.class).get("itsId")
                    .get("definition"));
      TestSimple<Cursor> testSimple = new TestSimple<Cursor>();
      testSimple.setSrvDatabase(srvDatabase);
      testSimple.setLogger(log);
      testSimple.setSrvOrm(srvOrm);
      testSimple.doTest1();
      srvDatabase.setIsAutocommit(true);
      String dep3insert = "insert into DEPARTMENT  (ITSID, ITSNAME, IDDATABASEBIRTH ) values (3, 'dp3', 999);";
      String dep4insert = "insert into DEPARTMENT  (ITSID, ITSNAME, IDDATABASEBIRTH ) values (4, 'dp4', 999);";
      Department dp3k = new Department();
      dp3k.setItsId(3L);
      Department dp3 = srvOrm.retrieveEntity(null, dp3k);
      if (dp3 == null) {
        srvDatabase.executeQuery(dep3insert);
        dp3 = srvOrm.retrieveEntity(null, dp3k);
        assertNotNull(dp3);
        srvOrm.deleteEntity(null, dp3);
        dp3 = srvOrm.retrieveEntity(null, dp3k);
        assertNull(dp3);
      }
      Department dp4k = new Department();
      dp4k.setItsId(4L);
      Department dp4 = srvOrm.retrieveEntity(null, dp4k);
      if (dp4 == null) {
        srvDatabase.executeQuery(dep4insert);
        dp4 = srvOrm.retrieveEntity(null, dp4k);
        assertNotNull(dp4);
        srvOrm.deleteEntity(null, dp4);
        dp4 = srvOrm.retrieveEntity(null, dp4k);
        assertNull(dp4);
      }
      boolean isFallGroupInsert = false;
      try {
        srvDatabase.executeQuery(dep3insert + "\n" + dep4insert);
      } catch (Exception e) {
        isFallGroupInsert = true;
      }
      assertTrue(!isFallGroupInsert);
      srvOrm.deleteEntity(null, dp3k);
      srvOrm.deleteEntity(null, dp4k);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
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
  public final SrvOrmAndroid<Cursor> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final SrvOrmAndroid<Cursor> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
  /**
   * <p>Getter for utlReflection.</p>
   * @return IUtlReflection
   **/
  public final IUtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final IUtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }
}
