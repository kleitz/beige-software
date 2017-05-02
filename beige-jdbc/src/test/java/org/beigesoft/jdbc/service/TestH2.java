package org.beigesoft.jdbc.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;
import java.util.LinkedHashMap;
import java.io.File;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import org.beigesoft.orm.service.SrvOrmH2;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.GoodVersionTime;
import org.beigesoft.test.persistable.GoodsRating;
import org.beigesoft.factory.FctConvertersToFromString;
import org.beigesoft.factory.FctFillersObjectFields;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.holder.HolderRapiSetters;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.persistable.IdUserRoleTomcat;
import org.beigesoft.test.persistable.UserRoleTomcatPriority;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.orm.factory.FctBcFctSimpleEntities;
import org.beigesoft.orm.factory.FctBnCnvBnFromRs;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.orm.holder.HldCnvFromRsNames;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.orm.service.FillerEntitiesFromRs;
import org.beigesoft.orm.test.TestSimple;
import org.beigesoft.log.LoggerSimple;

/**
 * <p>Test of ORM, Entity and Database services.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestH2 {

  SrvOrmH2<ResultSet> srvOrm;

  SrvDatabase srvDatabase;
  
  LoggerSimple logger = new LoggerSimple();

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection = new UtlReflection();

  public TestH2() throws Exception {
    logger.setIsShowDebugMessages(true);
    srvOrm = new SrvOrmH2<ResultSet>();
    srvDatabase = new SrvDatabase();
    srvDatabase.setLogger(logger);
    srvOrm.setSrvDatabase(srvDatabase);
    srvOrm.setLogger(logger);
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvOrm.setUtlReflection(getUtlReflection());
    srvDatabase.setHlpInsertUpdate(srvOrm.getHlpInsertUpdate());
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(logger);
    srvOrm.setMngSettings(mngSettings);
    srvOrm.loadConfiguration("beige-orm", "persistence-h2.xml");
    Properties props = new Properties();
    props.setProperty("dataSourceClassName", srvOrm.getPropertiesBase().getDataSourceClassName());
    props.setProperty("dataSource.user", srvOrm.getPropertiesBase().getUserName());
    props.setProperty("dataSource.password", srvOrm.getPropertiesBase().getUserPassword());
    props.setProperty("dataSource.Url", srvOrm.getPropertiesBase().getDatabaseUrl());
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
    FctBcCnvEntityToColumnsValues fcetcv = new FctBcCnvEntityToColumnsValues();
    HldCnvToColumnsValuesNames hldConvFld = new HldCnvToColumnsValuesNames();
    hldConvFld.setFieldsRapiHolder(hrf);
    fcetcv.setLogger(logger);
    fcetcv.setTablesMap(srvOrm.getTablesMap());
    fcetcv.setFieldsConvertersNamesHolder(hldConvFld);
    fcetcv.setGettersRapiHolder(hrg);
    fcetcv.setFieldsRapiHolder(hrf);
    fcetcv.setFieldsConvertersFatory(facConvFields);
    srvOrm.setFactoryCnvEntityToColumnsValues(fcetcv);
    FillerEntitiesFromRs<ResultSet> fillerEntitiesFromRs = new FillerEntitiesFromRs<ResultSet>();
    fillerEntitiesFromRs.setTablesMap(srvOrm.getTablesMap());
    fillerEntitiesFromRs.setLogger(logger);
    fillerEntitiesFromRs.setFieldsRapiHolder(hrf);
    FctFillersObjectFields fctFillersObjectFields = new FctFillersObjectFields();
    fctFillersObjectFields.setUtlReflection(getUtlReflection());
    HolderRapiSetters hrs = new HolderRapiSetters();
    hrs.setUtlReflection(getUtlReflection());
    fctFillersObjectFields.setSettersRapiHolder(hrs);
    fillerEntitiesFromRs.setFillersFieldsFactory(fctFillersObjectFields);
    srvOrm.setFctFillersObjectFields(fctFillersObjectFields);
    FctBnCnvBnFromRs<ResultSet> fctBnCnvBnFromRs = new FctBnCnvBnFromRs<ResultSet>();
    FctBcFctSimpleEntities fctBcFctSimpleEntities = new FctBcFctSimpleEntities();
    fctBcFctSimpleEntities.setSrvDatabase(srvDatabase);
    srvOrm.setEntitiesFactoriesFatory(fctBcFctSimpleEntities);
    fctBnCnvBnFromRs.setEntitiesFactoriesFatory(fctBcFctSimpleEntities);
    fctBnCnvBnFromRs.setFillersFieldsFactory(fctFillersObjectFields);
    fctBnCnvBnFromRs.setTablesMap(srvOrm.getTablesMap());
    fctBnCnvBnFromRs.setFieldsRapiHolder(hrf);
    fctBnCnvBnFromRs.setFillerObjectsFromRs(fillerEntitiesFromRs);
    fillerEntitiesFromRs.setConvertersFieldsFatory(fctBnCnvBnFromRs);
    HldCnvFromRsNames hldCnvFromRsNames = new HldCnvFromRsNames();
    hldCnvFromRsNames.setFieldsRapiHolder(hrf);
    fillerEntitiesFromRs.setFieldConverterNamesHolder(hldCnvFromRsNames);
    srvOrm.setFillerEntitiesFromRs(fillerEntitiesFromRs);
    HikariConfig config = new HikariConfig(props);
    HikariDataSource ds = new HikariDataSource(config);
    srvDatabase.setDataSource(ds);
  }

  @Test
  public void testAll() throws Exception {
    String currDir = System.getProperty("user.dir");
    System.out.println("Start test JDBC  H2");
    System.out.println("Current dir using System:" + currDir);
    TestSimple<ResultSet> testSimple = new TestSimple<ResultSet>();
    testSimple.setSrvDatabase(srvDatabase);
    testSimple.setSrvOrm(srvOrm);
    testSimple.setLogger(logger);
    testSimple.doTest1();
    TestConcurrence testConcurrence = new TestConcurrence();
    testConcurrence.setSrvDatabase(srvDatabase);
    testConcurrence.setLogger(logger);
    testConcurrence.setSrvOrm(srvOrm);
    testConcurrence.tstThreads();
    System.out.println("End test JDBC  H2");
  }

  //Simple getters and setters:
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
