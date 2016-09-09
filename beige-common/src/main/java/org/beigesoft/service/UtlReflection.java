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
public class UtlReflection {

  /**
   * <p>Retrieve all nonstatic private members from given class.</p>
   * @param clazz - class
   * @return Field[] fields.
   **/
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
   * <p>Retrieve member from given class.</p>
   * @param pClazz - class
   * @param pFieldName - field name
   * @return Field field.
   * @throws Exception if field not exist
   **/
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
}
