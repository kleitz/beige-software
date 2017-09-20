package org.beigesoft.orm.factory;

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

import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.converter.IConverterByName;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.converter.CnvBnRsToEntity;
import org.beigesoft.orm.converter.CnvBnRsToDouble;
import org.beigesoft.orm.converter.CnvBnRsToInteger;
import org.beigesoft.orm.converter.CnvBnRsToLong;
import org.beigesoft.orm.converter.CnvBnRsToBoolean;
import org.beigesoft.orm.converter.CnvBnRsToDate;
import org.beigesoft.orm.converter.CnvBnRsToString;
import org.beigesoft.orm.converter.CnvBnRsToBigDecimal;
import org.beigesoft.orm.converter.CnvBnRsToFloat;
import org.beigesoft.orm.converter.CnvBnRsToEnum;

/**
 * <p>Converters from RS to fields values factory.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctBnCnvBnFromRs<RS>
  implements IFactoryAppBeansByName<IConverterByName<IRecordSet<RS>, ?>> {

  /**
   * <p>Entitie's factories factory.</p>
   **/
  private IFactoryAppBeansByClass<IFactorySimple<?>> entitiesFactoriesFatory;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFactoryAppBeansByClass<IFillerObjectFields<?>> fillersFieldsFactory;

  /**
   * <p>Converters map "converter name"-"object' s converter".</p>
   **/
  private final Map<String, IConverterByName<IRecordSet<RS>, ?>>
    convertersMap =
      new Hashtable<String, IConverterByName<IRecordSet<RS>, ?>>();

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
   * <p>Filler Objects From IRecordSet.</p>
   **/
  private IFillerObjectsFrom<IRecordSet<RS>> fillerObjectsFromRs;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanName - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IConverterByName<IRecordSet<RS>, ?> lazyGet(
    final Map<String, Object> pAddParam,
      final String pBeanName) throws Exception {
    IConverterByName<IRecordSet<RS>, ?> convrt =
      this.convertersMap.get(pBeanName);
    if (convrt == null) {
      // locking:
      synchronized (this.convertersMap) {
        // make sure again whether it's null after locking:
        convrt = this.convertersMap.get(pBeanName);
        if (convrt == null) {
          if (pBeanName
            .equals(CnvBnRsToFloat.class.getSimpleName())) {
            convrt = createPutCnvBnRsToFloat();
          } else if (pBeanName
            .equals(CnvBnRsToEnum.class.getSimpleName())) {
            convrt = createPutCnvBnRsToEnum();
          } else if (pBeanName
            .equals(CnvBnRsToEntity.class.getSimpleName())) {
            convrt = createPutCnvBnRsToEntity();
          } else if (pBeanName
            .equals(CnvBnRsToDouble.class.getSimpleName())) {
            convrt = createPutCnvBnRsToDouble();
          } else if (pBeanName
            .equals(CnvBnRsToInteger.class.getSimpleName())) {
            convrt = createPutCnvBnRsToInteger();
          } else if (pBeanName
            .equals(CnvBnRsToLong.class.getSimpleName())) {
            convrt = createPutCnvBnRsToLong();
          } else if (pBeanName
            .equals(CnvBnRsToBoolean.class.getSimpleName())) {
            convrt = createPutCnvBnRsToBoolean();
          } else if (pBeanName
            .equals(CnvBnRsToDate.class.getSimpleName())) {
            convrt = createPutCnvBnRsToDate();
          } else if (pBeanName
            .equals(CnvBnRsToString.class.getSimpleName())) {
            convrt = createPutCnvBnRsToString();
          } else if (pBeanName
            .equals(CnvBnRsToBigDecimal.class.getSimpleName())) {
            convrt = createPutCnvBnRsToBigDecimal();
          }
        }
      }
    }
    if (convrt == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no converter with name " + pBeanName);
    }
    return convrt;
  }

  /**
   * <p>Set bean.</p>
   * @param pBeanName - bean name
   * @param pBean bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void set(final String pBeanName,
    final IConverterByName<IRecordSet<RS>, ?> pBean) throws Exception {
    this.convertersMap.put(pBeanName, pBean);
  }

  /**
   * <p>Get CnvBnRsToEntity (create and put into map).</p>
   * @return requested CnvBnRsToEntity
   * @throws Exception - an exception
   */
  protected final CnvBnRsToEntity<RS>
    createPutCnvBnRsToEntity() throws Exception {
    CnvBnRsToEntity<RS> convrt = new CnvBnRsToEntity<RS>();
    convrt.setTablesMap(getTablesMap());
    convrt.setFieldsRapiHolder(getFieldsRapiHolder());
    convrt.setFillersFieldsFactory(getFillersFieldsFactory());
    convrt.setEntitiesFactoriesFatory(getEntitiesFactoriesFatory());
    convrt.setFillerObjectsFromRs(getFillerObjectsFromRs());
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToEntity.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToDouble (create and put into map).</p>
   * @return requested CnvBnRsToDouble
   * @throws Exception - an exception
   */
  protected final CnvBnRsToDouble<RS>
    createPutCnvBnRsToDouble() throws Exception {
    CnvBnRsToDouble<RS> convrt = new CnvBnRsToDouble<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToDouble.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToInteger (create and put into map).</p>
   * @return requested CnvBnRsToInteger
   * @throws Exception - an exception
   */
  protected final CnvBnRsToInteger<RS>
    createPutCnvBnRsToInteger() throws Exception {
    CnvBnRsToInteger<RS> convrt = new CnvBnRsToInteger<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToInteger.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToLong (create and put into map).</p>
   * @return requested CnvBnRsToLong
   * @throws Exception - an exception
   */
  protected final CnvBnRsToLong<RS>
    createPutCnvBnRsToLong() throws Exception {
    CnvBnRsToLong<RS> convrt = new CnvBnRsToLong<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToLong.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToBoolean (create and put into map).</p>
   * @return requested CnvBnRsToBoolean
   * @throws Exception - an exception
   */
  protected final CnvBnRsToBoolean<RS>
    createPutCnvBnRsToBoolean() throws Exception {
    CnvBnRsToBoolean<RS> convrt = new CnvBnRsToBoolean<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToBoolean.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToDate (create and put into map).</p>
   * @return requested CnvBnRsToDate
   * @throws Exception - an exception
   */
  protected final CnvBnRsToDate<RS>
    createPutCnvBnRsToDate() throws Exception {
    CnvBnRsToDate<RS> convrt = new CnvBnRsToDate<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToDate.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToString (create and put into map).</p>
   * @return requested CnvBnRsToString
   * @throws Exception - an exception
   */
  protected final CnvBnRsToString<RS>
    createPutCnvBnRsToString() throws Exception {
    CnvBnRsToString<RS> convrt = new CnvBnRsToString<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToString.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToBigDecimal (create and put into map).</p>
   * @return requested CnvBnRsToBigDecimal
   * @throws Exception - an exception
   */
  protected final CnvBnRsToBigDecimal<RS>
    createPutCnvBnRsToBigDecimal() throws Exception {
    CnvBnRsToBigDecimal<RS> convrt = new CnvBnRsToBigDecimal<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToBigDecimal.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToEnum (create and put into map).</p>
   * @return requested CnvBnRsToEnum
   * @throws Exception - an exception
   */
  protected final CnvBnRsToEnum<RS>
    createPutCnvBnRsToEnum() throws Exception {
    CnvBnRsToEnum<RS> convrt = new CnvBnRsToEnum<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToEnum.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvBnRsToFloat (create and put into map).</p>
   * @return requested CnvBnRsToFloat
   * @throws Exception - an exception
   */
  protected final CnvBnRsToFloat<RS>
    createPutCnvBnRsToFloat() throws Exception {
    CnvBnRsToFloat<RS> convrt = new CnvBnRsToFloat<RS>();
    //assigning fully initialized object:
    this.convertersMap
      .put(CnvBnRsToFloat.class.getSimpleName(), convrt);
    return convrt;
  }

  //Simple getters and setters:
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
   * <p>Getter for entitiesFactoriesFatory.</p>
   * @return IFactoryAppBeansByClass<IFactorySimple<?>>
   **/
  public final IFactoryAppBeansByClass<IFactorySimple<?>>
    getEntitiesFactoriesFatory() {
    return this.entitiesFactoriesFatory;
  }

  /**
   * <p>Setter for entitiesFactoriesFatory.</p>
   * @param pEntitiesFactoriesFatory reference
   **/
  public final void setEntitiesFactoriesFatory(
    final IFactoryAppBeansByClass<IFactorySimple<?>>
      pEntitiesFactoriesFatory) {
    this.entitiesFactoriesFatory = pEntitiesFactoriesFatory;
  }

  /**
   * <p>Getter for fillersFieldsFactory.</p>
   * @return IFactoryAppBeansByClass<IFillerObjectFields<?>>
   **/
  public final IFactoryAppBeansByClass<IFillerObjectFields<?>>
    getFillersFieldsFactory() {
    return this.fillersFieldsFactory;
  }

  /**
   * <p>Setter for fillersFieldsFactory.</p>
   * @param pFillersFieldsFactory reference
   **/
  public final void setFillersFieldsFactory(
    final IFactoryAppBeansByClass<IFillerObjectFields<?>>
      pFillersFieldsFactory) {
    this.fillersFieldsFactory = pFillersFieldsFactory;
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
   * <p>Getter for fillerObjectsFromRs.</p>
   * @return IFillerObjectsFrom<IRecordSet<RS>>
   **/
  public final IFillerObjectsFrom<IRecordSet<RS>> getFillerObjectsFromRs() {
    return this.fillerObjectsFromRs;
  }

  /**
   * <p>Setter for fillerObjectsFromRs.</p>
   * @param pFillerObjectsFromRs reference
   **/
  public final void setFillerObjectsFromRs(
    final IFillerObjectsFrom<IRecordSet<RS>> pFillerObjectsFromRs) {
    this.fillerObjectsFromRs = pFillerObjectsFromRs;
  }
}
