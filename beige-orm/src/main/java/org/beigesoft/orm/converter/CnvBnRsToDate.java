package org.beigesoft.orm.converter;

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
import java.util.Date;

import org.beigesoft.converter.IConverterByName;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Converter field from JDBC result-set to Date.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class CnvBnRsToDate<RS> implements
  IConverterByName<IRecordSet<RS>, Date> {

  /**
   * <p>Convert parameter with using name.</p>
   * @param pAddParam additional params, e.g. entity class UserRoleTomcat
   * to reveal derived columns for its composite ID, or field Enum class
   * to reveal Enum value by index.
   * @param pFrom from a bean
   * @param pName by a name
   * @return pTo to a bean
   * @throws Exception - an exception
   **/
  @Override
  public final Date convert(final Map<String, Object> pAddParam,
    final IRecordSet<RS> pFrom, final String pName) throws Exception {
    Long longVal = pFrom.getLong(pName);
    if (longVal != null) {
      return new Date(longVal);
    }
    return null;
  }
}
