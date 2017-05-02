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

import org.beigesoft.converter.IConverterByName;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Converter field from JDBC result-set to Double.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class CnvBnRsToDouble<RS> implements
  IConverterByName<IRecordSet<RS>, Double> {

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
  public final Double convert(final Map<String, Object> pAddParam,
    final IRecordSet<RS> pFrom, final String pName) throws Exception {
    return pFrom.getDouble(pName);
  }
}
