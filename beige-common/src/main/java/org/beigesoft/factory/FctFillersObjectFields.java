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
import java.util.Hashtable;
import java.lang.reflect.Method;

import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.service.FillerObjectFieldsStd;

/**
 * <p>Factory of objects fillers.
 * It produces/hold object fillers that is used to fill entity
 * in ORM and MVC services. Factory for any object can be
 * initialized/replaced externally.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FctFillersObjectFields
  implements IFactoryAppBeansByClass<IFillerObjectFields<?>> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Fillers map "object class"-"object's filler".</p>
   **/
  private final Map<Class<?>, IFillerObjectFields<?>> fillersMap
    = new Hashtable<Class<?>, IFillerObjectFields<?>>();

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> settersRapiHolder;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanClass - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IFillerObjectFields<?> lazyGet(//NOPMD Rule:DoubleCheckedLocking
    final Map<String, Object> pAddParam,
      final Class<?> pBeanClass) throws Exception {
    // There is no way to get from Map partially initialized bean
    // in this double-checked locking implementation
    // cause putting to the Map fully initialized bean
    IFillerObjectFields<?> filler = this.fillersMap.get(pBeanClass);
    if (filler == null) {
      // locking:
      synchronized (this.fillersMap) {
        // make sure again whether it's null after locking:
        filler = this.fillersMap.get(pBeanClass);
        if (filler == null) {
          filler = createFillerObjectFieldsStd(pBeanClass);
        }
      }
    }
    return filler;
  }

  /**
   * <p>Set/replace bean.</p>
   * @param pBeanClass - bean class
   * @param pBean bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void set(final Class<?> pBeanClass,
    final IFillerObjectFields<?> pBean) throws Exception {
    this.fillersMap.put(pBeanClass, pBean);
  }

  /**
   * <p>Create FillerObjectFieldsStd.</p>
   * @param pBeanClass - bean class
   * @return requested FillerObjectFieldsStd
   * @throws Exception - an exception
   */
  protected final FillerObjectFieldsStd
    createFillerObjectFieldsStd(final Class pBeanClass) throws Exception {
    FillerObjectFieldsStd filler = new FillerObjectFieldsStd();
    filler.setUtlReflection(getUtlReflection());
    filler.setSettersRapiHolder(getSettersRapiHolder());
    filler.init(pBeanClass);
    //assigning fully initialized object:
    this.fillersMap.put(pBeanClass, filler);
    return filler;
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
  public final void setUtlReflection(
    final IUtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
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
