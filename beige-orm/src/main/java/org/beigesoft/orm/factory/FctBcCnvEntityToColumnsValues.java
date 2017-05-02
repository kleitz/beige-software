package org.beigesoft.orm.factory;

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
import java.lang.reflect.Field;

import org.beigesoft.log.ILogger;
import org.beigesoft.model.IHasVersion;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.converter.IConverterIntoByName;
import org.beigesoft.converter.IConverter;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.converter.CnvObjectToColumnsValues;
import org.beigesoft.orm.converter.CnvHasVersionToColumnsValues;

/**
 * <p>Converters entities into SQL ColumnValues factory.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FctBcCnvEntityToColumnsValues
  implements IFactoryAppBeansByClass<IConverter<?, ColumnsValues>> {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>>
    fieldsConvertersFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldsConvertersNamesHolder;

  /**
   * <p>Converters map "object's class"-"object' s converter".</p>
   **/
  private final Map<Class<?>, IConverter<?, ColumnsValues>>
    convertersMap =
      new Hashtable<Class<?>, IConverter<?, ColumnsValues>>();

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private Map<String, TableSql> tablesMap;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanClass - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IConverter<?, ColumnsValues> lazyGet(//NOPMD
    // Rule:DoubleCheckedLocking isn't true see in beige-common:
    // org.beigesoft.test.DoubleCkeckLockingWrApTest
    final Map<String, Object> pAddParam,
      final Class<?> pBeanClass) throws Exception {
    IConverter<?, ColumnsValues> convrt =
      this.convertersMap.get(pBeanClass);
    if (convrt == null) {
      // locking:
      synchronized (this.convertersMap) {
        // make sure again whether it's null after locking:
        convrt = this.convertersMap.get(pBeanClass);
        if (convrt == null) {
          if (IHasVersion.class.isAssignableFrom(pBeanClass)) {
            convrt = lazyGetCnvHasVersionToColumnsValues(pBeanClass);
          } else {
            convrt = lazyGetCnvObjectToColumnsValues(pBeanClass);
          }
        }
      }
    }
    return convrt;
  }

  /**
   * <p>Set bean.</p>
   * @param pBeanClass - bean class
   * @param pBean bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void set(final Class<?> pBeanClass,
    final IConverter<?, ColumnsValues> pBean) throws Exception {
    this.convertersMap.put(pBeanClass, pBean);
  }

  /**
   * <p>Get CnvHasVersionToColumnsValues (create and put into map).</p>
   * @param pBeanClass - bean class
   * @return requested CnvHasVersionToColumnsValues
   * @throws Exception - an exception
   */
  protected final CnvHasVersionToColumnsValues
    lazyGetCnvHasVersionToColumnsValues(
      final Class pBeanClass) throws Exception {
    @SuppressWarnings("unchecked")
    CnvHasVersionToColumnsValues<IHasVersion> convrt =
      (CnvHasVersionToColumnsValues<IHasVersion>)
        this.convertersMap.get(pBeanClass);
    if (convrt == null) {
      convrt = new CnvHasVersionToColumnsValues<IHasVersion>();
      convrt.setCnvObjectToColumnsValues(
        lazyGetCnvObjectToColumnsValues(pBeanClass));
      //assigning fully initialized object:
      this.convertersMap.put(pBeanClass, convrt);
    }
    return convrt;
  }

  /**
   * <p>Get CnvObjectToColumnsValues (create and put into map).</p>
   * @param pBeanClass - bean class
   * @return requested CnvObjectToColumnsValues
   * @throws Exception - an exception
   */
  protected final CnvObjectToColumnsValues
    lazyGetCnvObjectToColumnsValues(
      final Class pBeanClass) throws Exception {
    @SuppressWarnings("unchecked")
    CnvObjectToColumnsValues<Object> convrt =
      (CnvObjectToColumnsValues<Object>)
        this.convertersMap.get(pBeanClass);
    if (convrt == null) {
      convrt = new CnvObjectToColumnsValues<Object>();
      convrt.setLogger(getLogger());
      convrt.setTablesMap(getTablesMap());
      convrt.setFieldsConvertersNamesHolder(getFieldsConvertersNamesHolder());
      convrt.setGettersRapiHolder(getGettersRapiHolder());
      convrt.setFieldsRapiHolder(getFieldsRapiHolder());
      convrt.setFieldsConvertersFatory(getFieldsConvertersFatory());
      convrt.setObjectClass(pBeanClass);
      //assigning fully initialized object:
      this.convertersMap.put(pBeanClass, convrt);
    }
    return convrt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for fieldsConvertersFatory.</p>
   * @return IFactoryAppBeansByName<IConverter<?, ?>>
   **/
  public final IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>>
    getFieldsConvertersFatory() {
    return this.fieldsConvertersFatory;
  }

  /**
   * <p>Setter for fieldsConvertersFatory.</p>
   * @param pFieldsConvertersFatory reference
   **/
  public final void setFieldsConvertersFatory(
    final IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>>
      pFieldsConvertersFatory) {
    this.fieldsConvertersFatory = pFieldsConvertersFatory;
  }

  /**
   * <p>Getter for fieldsConvertersNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldsConvertersNamesHolder() {
    return this.fieldsConvertersNamesHolder;
  }

  /**
   * <p>Setter for fieldsConvertersNamesHolder.</p>
   * @param pFieldsConvertersNamesHolder reference
   **/
  public final void setFieldsConvertersNamesHolder(
    final IHolderForClassByName<String> pFieldsConvertersNamesHolder) {
    this.fieldsConvertersNamesHolder = pFieldsConvertersNamesHolder;
  }

  /**
   * <p>Getter for tablesMap.</p>
   * @return Map<String, TableSql>
   **/
  public final Map<String, TableSql> getTablesMap() {
    return this.tablesMap;
  }

  /**
   * <p>Setter for tablesMap.</p>
   * @param pTablesMap reference
   **/
  public final void setTablesMap(final Map<String, TableSql> pTablesMap) {
    this.tablesMap = pTablesMap;
  }

  /**
   * <p>Getter for fieldsRapiHolder.</p>
   * @return IHolderForClassByName<Field>
   **/
  public final IHolderForClassByName<Field> getFieldsRapiHolder() {
    return this.fieldsRapiHolder;
  }

  /**
   * <p>Setter for fieldsRapiHolder.</p>
   * @param pFieldsRapiHolder reference
   **/
  public final void setFieldsRapiHolder(
    final IHolderForClassByName<Field> pFieldsRapiHolder) {
    this.fieldsRapiHolder = pFieldsRapiHolder;
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
}
