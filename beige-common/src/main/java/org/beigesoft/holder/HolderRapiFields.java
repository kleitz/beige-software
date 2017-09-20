package org.beigesoft.holder;

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
import java.lang.reflect.Field;

import org.beigesoft.service.IUtlReflection;

/**
 * <p>Holder of RAPI Fields for any requested class.</p>
 *
 * @author Yury Demidenko
 */
public class HolderRapiFields implements IHolderForClassByName<Field> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Map of classes and their fields names - RAPI fields.</p>
   **/
  private final Map<Class<?>, Map<String, Field>> rapiFieldsMap =
    new Hashtable<Class<?>, Map<String, Field>>();

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pClass a Class
   * @param pFieldName Thing Name
   * @return a thing
   **/
  @Override
  public final Field getFor(final Class<?> pClass, final String pFieldName) {
    Map<String, Field> fldMap = this.rapiFieldsMap.get(pClass);
    if (fldMap == null) {
      // There is no way to get from Map partially initialized bean
      // in this double-checked locking implementation
      // cause putting to the Map fully initialized bean
      synchronized (this.rapiFieldsMap) {
        fldMap = this.rapiFieldsMap.get(pClass);
        if (fldMap == null) {
          fldMap = new Hashtable<String, Field>();
          Field[] fields = getUtlReflection().retrieveFields(pClass);
          for (Field field : fields) {
            fldMap.put(field.getName(), field);
          }
          this.rapiFieldsMap.put(pClass, fldMap);
        }
      }
    }
    return fldMap.get(pFieldName);
  }

  /**
   * <p>Set thing for given class and thing name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pFieldName Thing Name
   **/
  @Override
  public final void setFor(final Field pThing,
    final Class<?> pClass, final String pFieldName) {
    synchronized (this.rapiFieldsMap) {
      Map<String, Field> fldMap = this.rapiFieldsMap.get(pClass);
      if (fldMap == null) {
        fldMap = new Hashtable<String, Field>();
        this.rapiFieldsMap.put(pClass, fldMap);
      }
      fldMap.put(pFieldName, pThing);
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
   * <p>Getter for rapiFieldsMap.</p>
   * @return final Map<Class<?>, Map<String, Field>>
   **/
  public final Map<Class<?>, Map<String, Field>> getRapiFieldsMap() {
    return this.rapiFieldsMap;
  }
}
