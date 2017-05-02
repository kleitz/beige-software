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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.converter.IConverterIntoByName;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Converter version to ColumnsValues according version algorithm.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnVersionToCv
  implements IConverterIntoByName<Long, ColumnsValues> {

  /**
   * <p>Put version current and old to ColumnsValues
   * according version algorithm.</p>
   * @param pAddParam expected version algorithm with name "versionAlgorithm".
   * @param pFrom from a Long object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final Long pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
      Integer versionAlgorithm = (Integer)
        pAddParam.get("versionAlgorithm");
      if (versionAlgorithm == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Missed parameter versionAlgorithm!");
      }
      Long valueLngNew = null;
      if (versionAlgorithm == 1) {
        valueLngNew = new Date().getTime();
      } else {
        if (pFrom == null) {
          valueLngNew = 1L;
        } else {
          valueLngNew = pFrom + 1;
        }
      }
      pTo.put(pName, valueLngNew);
      pTo.put(ISrvOrm.VERSIONOLD_NAME, pFrom);
  }
}
