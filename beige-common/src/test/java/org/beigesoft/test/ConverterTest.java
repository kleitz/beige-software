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
import org.beigesoft.holder.HolderRapiSetters;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.test.persistable.UserRoleTomcatLine;
import org.beigesoft.persistable.IdUserRoleTomcat;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.converter.CnvTfsObject;
import org.beigesoft.converter.CnvTfsHasId;
import org.beigesoft.converter.CnvTfsString;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.converter.CnvTfsDateTime;
import org.beigesoft.converter.CnvTfsEnum;

import org.junit.Test;

/**
 * <p>Converters Tests.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterTest {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection = new UtlReflection();

  @Test
  public void test1() throws Exception {
    FctConvertersToFromString fcf = new FctConvertersToFromString();
    fcf.setUtlReflection(getUtlReflection());
    fcf.setFieldConverterNamesHolder(new ConvertersNamesHolder());
    fcf.getHasStringIdMap().put(UserTomcat.class, "itsUser");
    fcf.getHasCompositeIdMap().put(UserRoleTomcat.class, "itsId");
    fcf.getCompositeClasses().add(IdUserRoleTomcat.class);
    fcf.getEnumsClasses().add(EPeriod.class);
    HolderRapiGetters hrg = new HolderRapiGetters();
    hrg.setUtlReflection(getUtlReflection());
    fcf.setGettersRapiHolder(hrg);
    HolderRapiSetters hrs = new HolderRapiSetters();
    hrs.setUtlReflection(getUtlReflection());
    fcf.setSettersRapiHolder(hrs);
    HolderRapiFields hrf = new HolderRapiFields();
    hrf.setUtlReflection(getUtlReflection());
    fcf.setFieldsRapiHolder(hrf);
    UserTomcat ut = new UserTomcat();
    ut.setItsUser("admin");
    UserRoleTomcat urt = new UserRoleTomcat();
    urt.setItsUser(ut);
    urt.setItsRole("role");
    @SuppressWarnings("unchecked")
    CnvTfsObject<IdUserRoleTomcat> cnvrt =
      (CnvTfsObject<IdUserRoleTomcat>) fcf
        .lazyGet(null, CnvTfsObject.class.getSimpleName()
          + IdUserRoleTomcat.class.getSimpleName());
    String strVal = cnvrt.toString(null, urt.getItsId());
    System.out.println(strVal);
    assertTrue(strVal.contains("itsUser=admin"));
    assertTrue(strVal.contains("itsRole=role"));
    IdUserRoleTomcat idUrt = cnvrt.fromString(null, strVal);
    assertEquals("admin", idUrt.getItsUser().getItsUser());
    assertEquals("role", idUrt.getItsRole());
    CnvTfsDateTime dateTimeCnvrt = (CnvTfsDateTime)
      fcf.lazyGet(null, CnvTfsDateTime.class.getSimpleName());
    Date date = dateTimeCnvrt.fromString(null, "2001-07-04T12:08");
    assertEquals("2001-07-04T12:08", dateTimeCnvrt.toString(null, date));
    CnvTfsEnum<EPeriod> eperiodCnvrt = (CnvTfsEnum<EPeriod>)
      fcf.lazyGet(null, CnvTfsEnum.class.getSimpleName()
        + EPeriod.class.getSimpleName());
    assertEquals("", eperiodCnvrt.toString(null, null));
    EPeriod ep = eperiodCnvrt.fromString(null, EPeriod.DAILY.name());
    assertEquals(EPeriod.DAILY, ep);
    UserRoleTomcatLine urtl = new UserRoleTomcatLine();
    urtl.setItsOwner(urt);
    CnvTfsHasId convOwner =
      (CnvTfsHasId) fcf
        .lazyGet(null, CnvTfsHasId.class.getSimpleName() + "Composite"
          + UserRoleTomcat.class.getSimpleName());
    strVal = convOwner.toString(null, urtl.getItsOwner());
    System.out.println(strVal);
    assertTrue(strVal.contains("itsUser=admin"));
    assertTrue(strVal.contains("itsRole=role"));
  }

  protected class ConvertersNamesHolder implements IHolderForClassByName<String> {

    /**
     * <p>Get thing for given class and field name.</p>
     * @param pClass a Class
     * @param pFieldNamep Field Name
     * @return a thing
     **/
    @Override
    public final String getFor(final Class<?> pClass, final String pFieldName) {
      if (pFieldName.equals("itsRole")) {
        return CnvTfsString.class.getSimpleName();
      } else if (pFieldName.equals("itsUser")) {
        return CnvTfsHasId.class.getSimpleName() + "String"
          + UserTomcat.class.getSimpleName();
      } else if (pFieldName.equals("itsOwner")) {
        return CnvTfsHasId.class.getSimpleName() + "Composite"
          + UserRoleTomcat.class.getSimpleName();
      }
      return "";
    }

    /**
     * <p>Set thing for given class and field name.</p>
     * @param pThing Thing
     * @param pClass Class
     * @param pFieldNamep Field Name
     **/
    @Override
    public final void setFor(final String pThing,
      final Class<?> pClass, final String pFieldName) {
    }
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
