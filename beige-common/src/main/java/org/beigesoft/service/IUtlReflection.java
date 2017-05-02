package org.beigesoft.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * <p>Abstraction reflection service.</p>
 *
 * @author Yury Demidenko
 */
public interface IUtlReflection {

  /**
   * <p>Retrieve all nonstatic private members from given class.</p>
   * @param clazz - class
   * @return Field[] fields.
   **/
  Field[] retrieveFields(Class<?> clazz);

  /**
   * <p>Retrieve all nonstatic non-private methods from given class.</p>
   * @param clazz - class
   * @return Method[] fields.
   **/
  Method[] retrieveMethods(Class<?> clazz);

  /**
   * <p>Retrieve member from given class.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Field field.
   * @throws Exception if field not exist
   **/
  Field retrieveField(Class<?> pClazz,
    String pFieldName) throws Exception;

  /**
   * <p>Retrieve method from given class by name.</p>
   * @param pClazz - class
   * @param pMethodName - method name
   * @return Method method.
   * @throws Exception if method not exist
   **/
  Method retrieveMethod(Class<?> pClazz,
    String pMethodName) throws Exception;

  /**
   * <p>Retrieve getter from given class by field name.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Method getter.
   * @throws Exception if method not exist
   **/
  Method retrieveGetterForField(Class<?> pClazz,
    String pFieldName) throws Exception;

  /**
   * <p>Retrieve setter from given class by field name.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Method setter.
   * @throws Exception if method not exist
   **/
  Method retrieveSetterForField(Class<?> pClazz,
    String pFieldName) throws Exception;
}
