package org.beigesoft.factory;

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

/**
 * <pre>
 * Simple factory that create a request(or) scoped bean
 * by using reflection.
 * </pre>
 *
 * @author Yury Demidenko
 * @param <M> type of created bean
 **/
public class FactorySimple<M> implements IFactorySimple<M> {

  /**
   * <p>Object class.</p>
   **/
  private Class<M> objectClass;

  /**
   * <p>Create a bean.</p>
   * @param pAddParam additional param
   * @return M request(or) scoped bean
   * @throws Exception - an exception
   */
  @Override
  public final M create(final Map<String, Object> pAddParam) throws Exception {
    M object = this.objectClass.newInstance();
    return object;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for objectClass.</p>
   * @return Class<M>
   **/
  public final Class<M> getObjectClass() {
    return this.objectClass;
  }

  /**
   * <p>Setter for objectClass.</p>
   * @param pObjectClass reference
   **/
  public final void setObjectClass(final Class<M> pObjectClass) {
    this.objectClass = pObjectClass;
  }
}
