package org.beigesoft.test;

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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.beigesoft.model.IHasId;
import org.beigesoft.model.EPeriod;
import org.beigesoft.factory.FctConvertersToFromString;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.persistable.IdUserRoleTomcat;
import org.beigesoft.test.persistable.UserRoleTomcatPriority;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.orm.service.SrvOrmSqlite;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.orm.converter.CnvObjectToColumnsValues;
import org.beigesoft.converter.CnvTfsHasId;
import org.beigesoft.converter.CnvTfsString;
import org.beigesoft.converter.IConverter;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.converter.CnvTfsDateTime;
import org.beigesoft.converter.CnvTfsEnum;
import org.beigesoft.log.LoggerSimple;

import org.junit.Test;

/**
 * <p>CnvObjectToColumnsValues Tests.</p>
 *
 * @author Yury Demidenko
 */
public class CnvEntitiesToColumnsValuesByNameTest {

  LoggerSimple logger = new LoggerSimple();

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection = new UtlReflection();

  @Test
  public void test1() throws Exception {
    Map<String, Object> appParam = new HashMap<String, Object>();
    SrvOrmSqlite<String> srvOrm = new SrvOrmSqlite<String>();
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvOrm.setLogger(logger);
    srvOrm.setUtlReflection(getUtlReflection());
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(logger);
    mngSettings.setUtlProperties(new UtlProperties());
    mngSettings.setUtlReflection(getUtlReflection());
    srvOrm.setMngSettings(mngSettings);
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
    UserTomcat userTomcat = new UserTomcat();
    userTomcat.setItsId("admin");
    UserRoleTomcat urt = new UserRoleTomcat();
    urt.setItsUser(userTomcat);
    urt.setItsRole("role");
    UserRoleTomcatPriority urtp = new UserRoleTomcatPriority();
    urtp.setUserRoleTomcat(urt);
    urtp.setPriority(4);
    IConverter<UserRoleTomcatPriority, ColumnsValues> cetcv = (IConverter<UserRoleTomcatPriority, ColumnsValues>)
      fcetcv.lazyGet(null, UserRoleTomcatPriority.class);
    ColumnsValues cvUrtp = cetcv.convert(appParam, urtp);
    assertEquals("admin", cvUrtp.getString("itsUser"));
    assertEquals("role", cvUrtp.getString("itsRole"));
    assertEquals(Integer.valueOf(4), cvUrtp.getInteger("priority"));
    assertTrue(!cvUrtp.ifContains("itsNew"));
    System.out.println(cvUrtp);
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
