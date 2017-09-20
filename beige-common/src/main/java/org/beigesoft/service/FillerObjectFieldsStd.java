package org.beigesoft.service;

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

import org.beigesoft.holder.IHolderForClassByName;

/**
 * <p>Filler that fill object fields by reflection.
 * For performance reason use ObjectReaderWriter instead.
 * </p>
 *
 * @param <T> object (entity) type
 * @author Yury Demidenko
 */
public class FillerObjectFieldsStd<T> implements IFillerObjectFields<T> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Object class.</p>
   **/
  private Class<T> objectClass;

  /**
   * <p>Fields names.</p>
   **/
  private Set<String> fieldsNames;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> settersRapiHolder;

  /**
   * <p>Fill object's field.</p>
   * @param pAddParam additional param
   * @param pObject Object to fill
   * @param pFieldVal Field Value
   * @param pFieldName Field name
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pAddParam, final T pObject,
    final Object pFieldVal, final String pFieldName) throws Exception {
    Method fldSetter = this.settersRapiHolder
      .getFor(this.objectClass, pFieldName);
    fldSetter.invoke(pObject, pFieldVal);
  }

  /**
   * <p>Get object's fields names.</p>
   * @return Object's fields names
   **/
  @Override
  public final Set<String> getFieldsNames() {
    return this.fieldsNames;
  }

  /**
   * <p>Initialize filler for given object class.</p>
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
   * <p>Getter for objectClass.</p>
   * @return Class<T>
   **/
  public final Class<T> getObjectClass() {
    return this.objectClass;
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
