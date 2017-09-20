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

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.HashSet;

/**
 * <p>Simple reflection service.
 * It used by ORM and Prezentation libraries for
 * automatically generate SQL queries and ViewDescriptors.
 * </p>
 *
 * @author Yury Demidenko
 */
public class UtlReflection implements IUtlReflection {

  /**
   * <p>Retrieve all nonstatic private members from given class.</p>
   * @param clazz - class
   * @return Field[] fields.
   **/
  @Override
  public final Field[] retrieveFields(final Class<?> clazz) {
    Set<Field> fieldsSet = new HashSet<Field>();
    for (Field fld : clazz.getDeclaredFields()) {
      int modifiersMask = fld.getModifiers();
      if ((modifiersMask & Modifier.PRIVATE) > 0
            && (modifiersMask & Modifier.STATIC) == 0) {
        fieldsSet.add(fld);
      }
    }
    final Class<?> superClazz = clazz.getSuperclass();
    if (superClazz != null && superClazz != java.lang.Object.class) {
      for (Field fld : retrieveFields(superClazz)) {
        fieldsSet.add(fld);
      }
    }
    return fieldsSet.toArray(new Field[fieldsSet.size()]);
  }

  /**
   * <p>Retrieve all nonstatic non-private methods from given class.</p>
   * @param clazz - class
   * @return Method[] fields.
   **/
  @Override
  public final Method[] retrieveMethods(final Class<?> clazz) {
    Set<Method> fieldsSet = new HashSet<Method>();
    for (Method mfd : clazz.getDeclaredMethods()) {
      int modifiersMask = mfd.getModifiers();
      if ((modifiersMask & Modifier.PRIVATE) == 0
            && (modifiersMask & Modifier.STATIC) == 0) {
        fieldsSet.add(mfd);
      }
    }
    final Class<?> superClazz = clazz.getSuperclass();
    if (superClazz != null && superClazz != java.lang.Object.class) {
      for (Method mfd : retrieveMethods(superClazz)) {
        fieldsSet.add(mfd);
      }
    }
    return fieldsSet.toArray(new Method[fieldsSet.size()]);
  }

  /**
   * <p>Retrieve member from given class.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Field field.
   * @throws Exception if field not exist
   **/
  @Override
  public final Field retrieveField(final Class<?> pClazz,
    final String pFieldName) throws Exception {
    for (Field fld : pClazz.getDeclaredFields()) {
      if (fld.getName().equals(pFieldName)) {
        return fld;
      }
    }
    final Class<?> superClazz = pClazz.getSuperclass();
    Field field = null;
    if (superClazz != null && superClazz != java.lang.Object.class) {
      field = retrieveField(superClazz, pFieldName);
    }
    if (field == null) {
      throw new Exception("There is no field " + pFieldName + " in class "
        + pClazz); //TO-DO class must be original
    }
    return field;
  }

  /**
   * <p>Retrieve method from given class by name.</p>
   * @param pClazz - class
   * @param pMethodName - method name
   * @return Method method.
   * @throws Exception if method not exist
   **/
  @Override
  public final Method retrieveMethod(final Class<?> pClazz,
    final String pMethodName) throws Exception {
    for (Method mfd : pClazz.getDeclaredMethods()) {
      if (mfd.getName().equals(pMethodName)) {
        return mfd;
      }
    }
    final Class<?> superClazz = pClazz.getSuperclass();
    Method method = null;
    if (superClazz != null && superClazz != java.lang.Object.class) {
      method = retrieveMethod(superClazz, pMethodName);
    }
    if (method == null) {
      throw new Exception("There is no method " + pMethodName + " in class "
        + pClazz); //TO-DO class must be original
    }
    return method;
  }

  /**
   * <p>Retrieve getter from given class by field name.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Method getter.
   * @throws Exception if method not exist
   **/
  @Override
  public final Method retrieveGetterForField(final Class<?> pClazz,
    final String pFieldName) throws Exception {
    String getterName = "get" + pFieldName.substring(0, 1).toUpperCase()
      + pFieldName.substring(1);
    return retrieveMethod(pClazz, getterName);
  }

  /**
   * <p>Retrieve setter from given class by field name.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Method setter.
   * @throws Exception if method not exist
   **/
  @Override
  public final Method retrieveSetterForField(final Class<?> pClazz,
    final String pFieldName) throws Exception {
    String setterName = "set" + pFieldName.substring(0, 1).toUpperCase()
      + pFieldName.substring(1);
    return retrieveMethod(pClazz, setterName);
  }
}
