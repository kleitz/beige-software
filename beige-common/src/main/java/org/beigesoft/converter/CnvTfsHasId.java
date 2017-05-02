package org.beigesoft.converter;

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
import java.lang.reflect.Method;

import org.beigesoft.model.IHasId;
import org.beigesoft.service.IUtlReflection;

/**
 * <p>Converter of IHasId to/from string representation of its ID,
 * null represents as "".
 * It's used to convert fields - foreign entities.</p>
 *
 * @author Yury Demidenko
 * @param <T> type of IHasId model.
 * @param <ID> type of ID.
 */
public class CnvTfsHasId<T extends IHasId<ID>, ID>
  implements IConverterToFromString<T> {

  /**
   * <p>ID converter.</p>
   **/
  private IConverterToFromString<ID> idConverter;

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Entity class.</p>
   **/
  private Class<T> entityClass;

  /**
   * <p>Reflection ID getter.</p>
   **/
  private Method getterId;

  /**
   * <p>Reflection ID setter.</p>
   **/
  private Method setterId;

  /**
   * <p>Convert IHasId to string representation of its ID,
   * null represents as "".</p>
   * @param pModel a bean
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String toString(final Map<String, Object> pAddParam,
    final T pModel) throws Exception {
    if (pModel == null) {
      return "";
    }
    @SuppressWarnings("unchecked")
    ID itsId = (ID) this.getterId.invoke(pModel);
    if (itsId == null) {
      return "";
    }
    return this.idConverter.toString(pAddParam, itsId);
  }

  /**
   * <p>Convert from string.</p>
   * @param pStrVal string representation
   * @return T value
   * @throws Exception - an exception
   **/
  @Override
  public final T fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    T entity = null;
    if (pStrVal != null && !"".equals(pStrVal)) {
      entity = this.entityClass.newInstance();
      ID idVal = this.idConverter.fromString(pAddParam, pStrVal);
      this.setterId.invoke(entity, idVal);
    }
    return entity;
  }

  /**
   * <p>It must be invoked at once by Factory to improve performance
   * by not using locking during usage this converter.</p>
   * @param pClass entity class
   * @param pIdName entity ID name
   * @throws Exception - an exception
   **/
  public final void init(final Class<T> pClass,
    final String pIdName) throws Exception {
    this.entityClass = pClass;
    this.getterId = getUtlReflection()
      .retrieveGetterForField(this.entityClass, pIdName);
    this.setterId = getUtlReflection()
      .retrieveSetterForField(this.entityClass, pIdName);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for idConverter.</p>
   * @return IConverterToFromString<ID>
   **/
  public final IConverterToFromString<ID> getIdConverter() {
    return this.idConverter;
  }

  /**
   * <p>Setter for idConverter.</p>
   * @param pIdConverter reference
   **/
  public final void setIdConverter(
    final IConverterToFromString<ID> pIdConverter) {
    this.idConverter = pIdConverter;
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

  /**
   * <p>Getter for entityClass.</p>
   * @return Class<T>
   **/
  public final Class<T> getEntityClass() {
    return this.entityClass;
  }

  /**
   * <p>Getter for getterId.</p>
   * @return Method
   **/
  public final Method getGetterId() {
    return this.getterId;
  }

  /**
   * <p>Getter for setterId.</p>
   * @return Method
   **/
  public final Method getSetterId() {
    return this.setterId;
  }
}
