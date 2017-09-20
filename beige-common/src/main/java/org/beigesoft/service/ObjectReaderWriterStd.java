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
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * <p>Service that read/write object fields implemented with RAPI.
 * Encapsulation may give performance advantage.</p>
 *
 * @param <T> object (entity) type
 * @author Yury Demidenko
 */
public class ObjectReaderWriterStd<T> implements IObjectReaderWriter<T> {

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
  private Set<String> fieldsNames = new HashSet<String>();

  /**
   * <p>Fields setters map.</p>
   **/
  private Map<String, Method> settersMap = new Hashtable<String, Method>();

  /**
   * <p>Fields getters map.</p>
   **/
  private Map<String, Method> gettersMap = new Hashtable<String, Method>();

  /**
   * <p>Fill object's field.</p>
   * @param pAddParam additional param
   * @param pObject Object to fill
   * @param pFieldVal Field Value
   * @param pFieldName Field name
   * @throws Exception - an exception
   **/
  @Override
  public final void write(final Map<String, Object> pAddParam, final T pObject,
    final Object pFieldVal, final String pFieldName) throws Exception {
    Method fldSetter = this.settersMap.get(pFieldName);
    fldSetter.invoke(pObject, pFieldVal);
  }

  /**
   * <p>Read object's field.</p>
   * @param pAddParam additional param
   * @param pObject Object to fill
   * @param pFieldName Field name
   * @return object's field value
   * @throws Exception - an exception
   **/
  @Override
  public final Object read(final Map<String, Object> pAddParam,
    final T pObject, final String pFieldName) throws Exception {
    Method fldGetter = this.gettersMap.get(pFieldName);
    return fldGetter.invoke(pObject);
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
    this.fieldsNames.clear();
    this.gettersMap.clear();
    this.settersMap.clear();
    Field[] fields = getUtlReflection().retrieveFields(this.objectClass);
    for (Field field : fields) {
      Method getter = getUtlReflection()
        .retrieveGetterForField(pClass, field.getName());
      Method setter = getUtlReflection()
        .retrieveSetterForField(pClass, field.getName());
      this.gettersMap.put(field.getName(), getter);
      this.settersMap.put(field.getName(), setter);
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
}
