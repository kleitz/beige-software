package org.beigesoft.converter;

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
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IUtlReflection;

/**
 * <p>Generic converter any complex object (exclude Integer, String, Bigdecimal)
 * to string representation and vice versa. It uses reflection API.
 * Fields of simple type like String, Integer, BigDecimal
 * are converted by [field].toString().
 * Fields foreign entities that extends <b>IHasId</b>
 * are converted by reveal <b>itsId</b> - either simple type
 * or complex ID.
 * Primitives fields are ignored.
 * This is none-synchronized class.
 * It's used to convert composite ID to/from string.
 * </p>
 * Example of converting to string IdUserRoleTomcat:
 * <pre>
 * itsUser=admin,itsRole=admin
 * </pre>
 *
 * @param <T> type of object
 * @author Yury Demidenko
 */
public class CnvTfsObject<T> implements IConverterToFromString<T> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterToFromString<?>>
    fieldsConvertersFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Object class.</p>
   **/
  private Class<T> objectClass;

  /**
   * <p>Fields names.</p>
   **/
  private Set<String> fieldsNames;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> settersRapiHolder;

  /**
   * <p>Convert to string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pObject Complex Id
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String toString(final Map<String, Object> pAddParam,
    final T pObject) throws Exception {
    StringBuffer sb = new StringBuffer("");
    boolean isFirst = true;
    for (String fldNm : this.fieldsNames) {
      String cnvrtName = this.fieldConverterNamesHolder.getFor(this.objectClass,
        fldNm);
      if (cnvrtName != null) { // e.g. transient field or collections
        IConverterToFromString cnvrt = this.fieldsConvertersFatory
          .lazyGet(null, cnvrtName);
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        Method getter = this.gettersRapiHolder
          .getFor(this.objectClass, fldNm);
        Object fldVal = getter.invoke(pObject);
        sb.append(fldNm + "=" + cnvrt.toString(pAddParam, fldVal));
      }
    }
    return sb.toString();
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return M model
   * @throws Exception - an exception
   **/
  @Override
  public final T fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    T object = this.objectClass.newInstance();
    for (String fldValStr : pStrVal.split(",")) {
      int eqIdx = fldValStr.indexOf("=");
      String fldName = fldValStr.substring(0, eqIdx);
      String cnvrtName = this.fieldConverterNamesHolder.getFor(this.objectClass,
        fldName);
      if (cnvrtName != null) { // e.g. transient field
        IConverterToFromString cnvrt = this.fieldsConvertersFatory
          .lazyGet(null, cnvrtName);
        Method fldSetter = this.settersRapiHolder
          .getFor(this.objectClass, fldName);
        String strVal = fldValStr.substring(eqIdx + 1);
        Object fldVal = cnvrt.fromString(pAddParam, strVal);
        fldSetter.invoke(object, fldVal);
      }
    }
    return object;
  }

  /**
   * <p>Initialize converter for given object class.</p>
   * @param pClass object class
   * @throws Exception - an exception
   **/
  public final synchronized void init(final Class<T> pClass) throws Exception {
    this.objectClass = pClass;
    this.fieldsNames = new HashSet<String>();
    Field[] fields = getUtlReflection().retrieveFields(this.objectClass);
    for (Field field : fields) {
      this.fieldsNames.add(field.getName());
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

  /**
   * <p>Getter for fieldsConvertersFatory.</p>
   * @return IFactoryAppBeansByName<IConverterToFromString<?>>
   **/
  public final IFactoryAppBeansByName<IConverterToFromString<?>>
    getFieldsConvertersFatory() {
    return this.fieldsConvertersFatory;
  }

  /**
   * <p>Setter for fieldsConvertersFatory.</p>
   * @param pFieldsConvertersFatory reference
   **/
  public final void setFieldsConvertersFatory(
    final IFactoryAppBeansByName<IConverterToFromString<?>>
      pFieldsConvertersFatory) {
    this.fieldsConvertersFatory = pFieldsConvertersFatory;
  }

  /**
   * <p>Getter for fieldConverterNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldConverterNamesHolder() {
    return this.fieldConverterNamesHolder;
  }

  /**
   * <p>Setter for fieldConverterNamesHolder.</p>
   * @param pFieldConverterNamesHolder reference
   **/
  public final void setFieldConverterNamesHolder(
    final IHolderForClassByName<String> pFieldConverterNamesHolder) {
    this.fieldConverterNamesHolder = pFieldConverterNamesHolder;
  }

  /**
   * <p>Getter for objectClass.</p>
   * @return Class<T>
   **/
  public final Class<T> getObjectClass() {
    return this.objectClass;
  }

  /**
   * <p>Getter for fieldsNames.</p>
   * @return Set<String>
   **/
  public final Set<String> getFieldsNames() {
    return this.fieldsNames;
  }

  /**
   * <p>Getter for gettersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getGettersRapiHolder() {
    return this.gettersRapiHolder;
  }

  /**
   * <p>Setter for gettersRapiHolder.</p>
   * @param pGettersRapiHolder reference
   **/
  public final void setGettersRapiHolder(
    final IHolderForClassByName<Method> pGettersRapiHolder) {
    this.gettersRapiHolder = pGettersRapiHolder;
  }

  /**
   * <p>Getter for settersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getSettersRapiHolder() {
    return this.settersRapiHolder;
  }

  /**
   * <p>Setter for settersRapiHolder.</p>
   * @param pSettersRapiHolder reference
   **/
  public final void setSettersRapiHolder(
    final IHolderForClassByName<Method> pSettersRapiHolder) {
    this.settersRapiHolder = pSettersRapiHolder;
  }
}
