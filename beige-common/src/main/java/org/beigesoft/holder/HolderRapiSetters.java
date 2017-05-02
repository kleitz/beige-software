package org.beigesoft.holder;

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
import java.util.Hashtable;
import java.lang.reflect.Method;

import org.beigesoft.service.IUtlReflection;

/**
 * <p>Holder of RAPI setters for any requested class.</p>
 *
 * @author Yury Demidenko
 */
public class HolderRapiSetters implements IHolderForClassByName<Method> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Map of classes and their fields names - RAPI setters.</p>
   **/
  private final Map<Class<?>, Map<String, Method>> rapiMethodsMap =
    new Hashtable<Class<?>, Map<String, Method>>();

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pClass a Class
   * @param pFieldName Thing Name
   * @return a thing
   **/
  @Override
  public final Method getFor(final Class<?> pClass, final String pFieldName) {
    Map<String, Method> methMap = this.rapiMethodsMap.get(pClass);
    if (methMap == null) {
      // There is no way to get from Map partially initialized bean
      // in this double-checked locking implementation
      // cause putting to the Map fully initialized bean
      synchronized (this.rapiMethodsMap) {
        methMap = this.rapiMethodsMap.get(pClass);
        if (methMap == null) {
          methMap = new Hashtable<String, Method>();
          Method[] methods = getUtlReflection().retrieveMethods(pClass);
          for (Method method : methods) {
            if (method.getName().startsWith("set")) {
              String fldNm = method.getName().substring(3, 4).toLowerCase()
                + method.getName().substring(4);
              methMap.put(fldNm, method);
            }
          }
          //assigning fully initialized object:
          this.rapiMethodsMap.put(pClass, methMap);
        }
      }
    }
    return methMap.get(pFieldName);
  }

  /**
   * <p>Set thing for given class and thing name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pFieldName Thing Name
   **/
  @Override
  public final void setFor(final Method pThing,
    final Class<?> pClass, final String pFieldName) {
    synchronized (this.rapiMethodsMap) {
      Map<String, Method> methMap = this.rapiMethodsMap.get(pClass);
      if (methMap == null) {
        methMap = new Hashtable<String, Method>();
        this.rapiMethodsMap.put(pClass, methMap);
      }
      methMap.put(pFieldName, pThing);
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
   * <p>Getter for rapiMethodsMap.</p>
   * @return final Map<Class<?>, Map<String, Method>>
   **/
  public final Map<Class<?>, Map<String, Method>> getRapiMethodsMap() {
    return this.rapiMethodsMap;
  }
}
