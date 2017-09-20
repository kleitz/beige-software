package org.beigesoft.orm.converter;

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
