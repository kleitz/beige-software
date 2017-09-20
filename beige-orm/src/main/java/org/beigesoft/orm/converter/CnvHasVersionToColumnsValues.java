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

import org.beigesoft.model.IHasVersion;
import org.beigesoft.converter.IConverter;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Generic converter of entity with version into ColumnValues
 * and set its new version.
 * </p>
 *
 * @param <T> type of object
 * @author Yury Demidenko
 */
public class CnvHasVersionToColumnsValues<T extends IHasVersion>
  implements IConverter<T, ColumnsValues> {

  /**
   * <p>Standard delegator.</p>
   **/
  private IConverter<T, ColumnsValues> cnvObjectToColumnsValues;

  /**
   * <p>Convert to column values.</p>
   * @param pAddParam additional params, expected "isOnlyId"-not null for
   * converting only ID field.
   * @param pObject entity
   * @return ColumnsValues Columns Values
   * @throws Exception - an exception
   **/
  @Override
  public final ColumnsValues convert(final Map<String, Object> pAddParam,
    final T pObject) throws Exception {
    ColumnsValues result = this.cnvObjectToColumnsValues
      .convert(pAddParam, pObject);
    pObject.setItsVersion(result.getLong(ISrvOrm.VERSION_NAME));
    return result;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cnvObjectToColumnsValues.</p>
   * @return IConverter<T, ColumnsValues>
   **/
  public final IConverter<T, ColumnsValues> getCnvObjectToColumnsValues() {
    return this.cnvObjectToColumnsValues;
  }

  /**
   * <p>Setter for cnvObjectToColumnsValues.</p>
   * @param pCnvObjectToColumnsValues reference
   **/
  public final void setCnvObjectToColumnsValues(
    final IConverter<T, ColumnsValues> pCnvObjectToColumnsValues) {
    this.cnvObjectToColumnsValues = pCnvObjectToColumnsValues;
  }
}
