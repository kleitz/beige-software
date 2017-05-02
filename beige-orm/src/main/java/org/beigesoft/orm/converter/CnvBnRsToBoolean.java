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
 * <p>Converter field from JDBC result-set to Boolean.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class CnvBnRsToBoolean<RS> implements
  IConverterByName<IRecordSet<RS>, Boolean> {

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
  public final Boolean convert(final Map<String, Object> pAddParam,
    final IRecordSet<RS> pFrom, final String pName) throws Exception {
    //The simple, the better
    //Findbugs NP_BOOLEAN_RETURN_NULL prohibit Boolean to be null
    //So Boolean must be allways initialized to false/true
    //And database fields that hold Booleans must be not null
    //And any user interface also must treat Booleans as false/true, not null
    //For 3-states values use Enum "YES/NO" instead where null
    //equivalent "not answered yet"
    return pFrom.getInteger(pName) == 1;
  }
}
