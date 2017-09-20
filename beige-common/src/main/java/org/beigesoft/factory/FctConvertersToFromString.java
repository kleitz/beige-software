package org.beigesoft.factory;

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

import org.beigesoft.model.IHasId;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.converter.CnvTfsBigDecimal;
import org.beigesoft.converter.CnvTfsFloat;
import org.beigesoft.converter.CnvTfsDouble;
import org.beigesoft.converter.CnvTfsBoolean;
import org.beigesoft.converter.CnvTfsInteger;
import org.beigesoft.converter.CnvTfsLong;
import org.beigesoft.converter.CnvTfsString;
import org.beigesoft.converter.CnvTfsDate;
import org.beigesoft.converter.CnvTfsDateTime;
import org.beigesoft.converter.CnvTfsDateTimeSec;
import org.beigesoft.converter.CnvTfsDateTimeSecMs;
import org.beigesoft.converter.CnvTfsEnum;
import org.beigesoft.converter.CnvTfsHasId;
import org.beigesoft.converter.CnvTfsObject;

/**
 * <p>Converters to/from string factory.
 * It produces converters that usually used as fields converters
 * for WEB-interface.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FctConvertersToFromString
  implements IFactoryAppBeansByName<IConverterToFromString<?>> {

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Set of enum classes.</p>
   **/
  private final Set<Class<? extends Enum<?>>> enumsClasses =
    new HashSet<Class<? extends Enum<?>>>();

  /**
   * <p>Set of Classes that represent composite object,
   * e.g. composite ID - @see org.beigesoft.persistable.IdUserRoleTomcat.</p>
   **/
  private final Set<Class<?>> compositeClasses =
    new HashSet<Class<?>>();

  /**
   * <p>Map of Classes of type IHasId<Long>
   * and their ID name.</p>
   **/
  private final Map<Class<? extends IHasId<Long>>, String> hasLongIdMap =
    new Hashtable<Class<? extends IHasId<Long>>, String>();

  /**
   * <p>Map of Classes of type IHasId<Integer>
   * and their ID name.</p>
   **/
  private final Map<Class<? extends IHasId<Integer>>, String> hasIntegerIdMap =
    new Hashtable<Class<? extends IHasId<Integer>>, String>();

  /**
   * <p>Map of Classes of type IHasId<String>
   * and their ID name.</p>
   **/
  private final Map<Class<? extends IHasId<String>>, String> hasStringIdMap =
    new Hashtable<Class<? extends IHasId<String>>, String>();

  /**
   * <p>Map of Classes of type IHasId with composite ID
   * and their ID name.</p>
   **/
  private final Map<Class<? extends IHasId<?>>, String> hasCompositeIdMap =
    new Hashtable<Class<? extends IHasId<?>>, String>();

  /**
   * <p>Converters map "converter name"-"object' s converter".</p>
   **/
  private final Map<String, IConverterToFromString<?>> convertersMap
    = new Hashtable<String, IConverterToFromString<?>>();

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> settersRapiHolder;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanName - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IConverterToFromString<?> lazyGet(
    final Map<String, Object> pAddParam,
      final String pBeanName) throws Exception {
    // There is no way to get from Map partially initialized bean
    // in this double-checked locking implementation
    // cause putting to the Map fully initialized bean
    IConverterToFromString<?> convrt = this.convertersMap.get(pBeanName);
    if (convrt == null) {
      // locking:
      synchronized (this.convertersMap) {
        // make sure again whether it's null after locking:
        convrt = this.convertersMap.get(pBeanName);
        if (convrt == null) {
          if (pBeanName.equals(CnvTfsLong.class.getSimpleName())) {
            convrt = lazyGetCnvTfsLong();
          } else if (pBeanName.equals(CnvTfsDate.class.getSimpleName())) {
            convrt = createPutCnvTfsDate();
          } else if (pBeanName.equals(CnvTfsDateTime.class
            .getSimpleName())) {
            convrt = createPutCnvTfsDateTime();
          } else if (pBeanName.equals(CnvTfsDateTimeSec.class
            .getSimpleName())) {
            convrt = createPutCnvTfsDateTimeSec();
          } else if (pBeanName.equals(CnvTfsDateTimeSecMs.class
            .getSimpleName())) {
            convrt = createPutCnvTfsDateTimeSecMs();
          } else if (pBeanName.equals(CnvTfsBigDecimal.class
            .getSimpleName())) {
            convrt = createPutCnvTfsBigDecimal();
          } else if (pBeanName.equals(CnvTfsFloat.class.getSimpleName())) {
            convrt = createPutCnvTfsFloat();
          } else if (pBeanName.equals(CnvTfsDouble.class.getSimpleName())) {
            convrt = createPutCnvTfsDouble();
          } else if (pBeanName.equals(CnvTfsBoolean.class.getSimpleName())) {
            convrt = createPutCnvTfsBoolean();
          } else if (pBeanName.equals(CnvTfsInteger.class.getSimpleName())) {
            convrt = lazyGetCnvTfsInteger();
          } else if (pBeanName.equals(CnvTfsString.class.getSimpleName())) {
            convrt = lazyGetCnvTfsString();
          } else {
            for (Map.Entry<Class<? extends IHasId<Long>>, String> entry
              : this.hasLongIdMap.entrySet()) {
              if (pBeanName.equals(CnvTfsHasId.class.getSimpleName()
                + "Long" + entry.getKey().getSimpleName())) {
                convrt = createHasLongIdConverter(pBeanName,
                  entry.getKey(), entry.getValue());
                break;
              }
            }
            for (Map.Entry<Class<? extends IHasId<Integer>>, String> entry
              : this.hasIntegerIdMap.entrySet()) {
              if (pBeanName.equals(CnvTfsHasId.class.getSimpleName()
                + "Integer" + entry.getKey().getSimpleName())) {
                convrt = createHasIntegerIdConverter(pBeanName,
                  entry.getKey(), entry.getValue());
                break;
              }
            }
            for (Map.Entry<Class<? extends IHasId<String>>, String> entry
              : this.hasStringIdMap.entrySet()) {
              if (pBeanName.equals(CnvTfsHasId.class.getSimpleName()
                + "String" + entry.getKey().getSimpleName())) {
                convrt = createHasStringIdConverter(pBeanName,
                  entry.getKey(), entry.getValue());
                break;
              }
            }
            for (Map.Entry<Class<? extends IHasId<?>>, String> entry
              : this.hasCompositeIdMap.entrySet()) {
              if (pBeanName.equals(CnvTfsHasId.class.getSimpleName()
                + "Composite" + entry.getKey().getSimpleName())) {
                convrt = createHasCompositeIdConverter(pBeanName,
                  entry.getKey(), entry.getValue());
                break;
              }
            }
            for (Class<?> cmpsClass : this.compositeClasses) {
              if (pBeanName.equals(getCnvTfsObjectName(cmpsClass))) {
                convrt = lazyGetCnvTfsObject(cmpsClass);
                break;
              }
            }
            for (Class<? extends Enum<?>> enumClass : this.enumsClasses) {
              if (pBeanName.equals(CnvTfsEnum
                .class.getSimpleName() + enumClass.getSimpleName())) {
                convrt = createCnvTfsEnum(pBeanName, enumClass);
                break;
              }
            }
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
    final IConverterToFromString<?> pBean) throws Exception {
    this.convertersMap.put(pBeanName, pBean);
  }

  /**
   * <p>Create CnvTfsEnum([Enum class]).</p>
   * @param pBeanName - bean name
   * @param pClass - enum class
   * @return requested CnvTfsEnum([ec])
   * @throws Exception - an exception
   */
  protected final CnvTfsEnum
    createCnvTfsEnum(final String pBeanName,
      final Class pClass) throws Exception {
    CnvTfsEnum convrt = new CnvTfsEnum();
    convrt.setEnumClass(pClass);
    this.convertersMap.put(pBeanName, convrt);
    return convrt;
  }

  /**
   * <p>Create put CnvTfsHasId(Integer).</p>
   * @param pBeanName - bean name
   * @param pClass - bean class
   * @param pIdName - bean ID name
   * @return requested CnvTfsHasId(Integer)
   * @throws Exception - an exception
   */
  protected final CnvTfsHasId<IHasId<Integer>, Integer>
    createHasIntegerIdConverter(final String pBeanName,
      final Class pClass, final String pIdName) throws Exception {
    CnvTfsHasId<IHasId<Integer>, Integer> convrt =
      new CnvTfsHasId<IHasId<Integer>, Integer>();
    convrt.setUtlReflection(getUtlReflection());
    convrt.setIdConverter(lazyGetCnvTfsInteger());
    convrt.init(pClass, pIdName);
    this.convertersMap.put(pBeanName, convrt);
    return convrt;
  }

  /**
   * <p>Create put CnvTfsHasId(Long).</p>
   * @param pBeanName - bean name
   * @param pClass - bean class
   * @param pIdName - bean ID name
   * @return requested CnvTfsHasId(Long)
   * @throws Exception - an exception
   */
  protected final CnvTfsHasId<IHasId<Long>, Long>
    createHasLongIdConverter(final String pBeanName,
      final Class pClass, final String pIdName) throws Exception {
    CnvTfsHasId<IHasId<Long>, Long> convrt =
      new CnvTfsHasId<IHasId<Long>, Long>();
    convrt.setUtlReflection(getUtlReflection());
    convrt.setIdConverter(lazyGetCnvTfsLong());
    convrt.init(pClass, pIdName);
    this.convertersMap.put(pBeanName, convrt);
    return convrt;
  }

  /**
   * <p>Create put CnvTfsHasId(Composite).</p>
   * @param pBeanName - bean name
   * @param pClass - bean class
   * @param pIdName - bean ID name
   * @return requested CnvTfsHasId(String)
   * @throws Exception - an exception
   */
  protected final CnvTfsHasId<IHasId<Object>, Object>
    createHasCompositeIdConverter(final String pBeanName,
      final Class pClass, final String pIdName) throws Exception {
    CnvTfsHasId<IHasId<Object>, Object> convrt =
      new CnvTfsHasId<IHasId<Object>, Object>();
    convrt.setUtlReflection(getUtlReflection());
    Field rapiFieldId = this.fieldsRapiHolder.getFor(pClass, pIdName);
    convrt.setIdConverter(lazyGetCnvTfsObject(rapiFieldId.getType()));
    convrt.init(pClass, pIdName);
    this.convertersMap.put(pBeanName, convrt);
    return convrt;
  }

  /**
   * <p>Create put CnvTfsHasId(String).</p>
   * @param pBeanName - bean name
   * @param pClass - bean class
   * @param pIdName - bean ID name
   * @return requested CnvTfsHasId(String)
   * @throws Exception - an exception
   */
  protected final CnvTfsHasId<IHasId<String>, String>
    createHasStringIdConverter(final String pBeanName,
      final Class pClass, final String pIdName) throws Exception {
    CnvTfsHasId<IHasId<String>, String> convrt =
      new CnvTfsHasId<IHasId<String>, String>();
    convrt.setUtlReflection(getUtlReflection());
    convrt.setIdConverter(lazyGetCnvTfsString());
    convrt.init(pClass, pIdName);
    this.convertersMap.put(pBeanName, convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsDateTime (create and put into map).</p>
   * @return requested CnvTfsDateTime
   * @throws Exception - an exception
   */
  protected final CnvTfsDateTime
    createPutCnvTfsDateTime() throws Exception {
    CnvTfsDateTime convrt = new CnvTfsDateTime();
    this.convertersMap.put(CnvTfsDateTime.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsDateTimeSec (create and put into map).</p>
   * @return requested CnvTfsDateTimeSec
   * @throws Exception - an exception
   */
  protected final CnvTfsDateTimeSec
    createPutCnvTfsDateTimeSec() throws Exception {
    CnvTfsDateTimeSec convrt = new CnvTfsDateTimeSec();
    this.convertersMap.put(CnvTfsDateTimeSec.class
      .getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsDateTimeSecMs (create and put into map).</p>
   * @return requested CnvTfsDateTimeSecMs
   * @throws Exception - an exception
   */
  protected final CnvTfsDateTimeSecMs
    createPutCnvTfsDateTimeSecMs() throws Exception {
    CnvTfsDateTimeSecMs convrt = new CnvTfsDateTimeSecMs();
    this.convertersMap.put(CnvTfsDateTimeSecMs.class
      .getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsDate (create and put into map).</p>
   * @return requested CnvTfsDate
   * @throws Exception - an exception
   */
  protected final CnvTfsDate
    createPutCnvTfsDate() throws Exception {
    CnvTfsDate convrt = new CnvTfsDate();
    this.convertersMap.put(CnvTfsDate.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsBigDecimal (create and put into map).</p>
   * @return requested CnvTfsBigDecimal
   * @throws Exception - an exception
   */
  protected final CnvTfsBigDecimal
    createPutCnvTfsBigDecimal() throws Exception {
    CnvTfsBigDecimal convrt = new CnvTfsBigDecimal();
    this.convertersMap.put(CnvTfsBigDecimal.class
      .getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsFloat (create and put into map).</p>
   * @return requested CnvTfsFloat
   * @throws Exception - an exception
   */
  protected final CnvTfsFloat
    createPutCnvTfsFloat() throws Exception {
    CnvTfsFloat convrt = new CnvTfsFloat();
    this.convertersMap.put(CnvTfsFloat.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsDouble (create and put into map).</p>
   * @return requested CnvTfsDouble
   * @throws Exception - an exception
   */
  protected final CnvTfsDouble
    createPutCnvTfsDouble() throws Exception {
    CnvTfsDouble convrt = new CnvTfsDouble();
    this.convertersMap.put(CnvTfsDouble.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Get CnvTfsBoolean (create and put into map).</p>
   * @return requested CnvTfsBoolean
   * @throws Exception - an exception
   */
  protected final CnvTfsBoolean
    createPutCnvTfsBoolean() throws Exception {
    CnvTfsBoolean convrt = new CnvTfsBoolean();
    this.convertersMap.put(CnvTfsBoolean.class.getSimpleName(), convrt);
    return convrt;
  }

  /**
   * <p>Lazy get CnvTfsInteger.</p>
   * @return requested CnvTfsInteger
   * @throws Exception - an exception
   */
  protected final CnvTfsInteger
    lazyGetCnvTfsInteger() throws Exception {
    CnvTfsInteger convrt = (CnvTfsInteger) this.convertersMap
      .get(CnvTfsInteger.class.getSimpleName());
    if (convrt == null) {
      convrt = new CnvTfsInteger();
      this.convertersMap.put(CnvTfsInteger.class.getSimpleName(), convrt);
    }
    return convrt;
  }

  /**
   * <p>Lazy get CnvTfsLong.</p>
   * @return requested CnvTfsLong
   * @throws Exception - an exception
   */
  protected final CnvTfsLong
    lazyGetCnvTfsLong() throws Exception {
    CnvTfsLong convrt = (CnvTfsLong) convertersMap
      .get(CnvTfsLong.class.getSimpleName());
    if (convrt == null) {
      convrt = new CnvTfsLong();
      this.convertersMap.put(CnvTfsLong.class.getSimpleName(), convrt);
    }
    return convrt;
  }

  /**
   * <p>Lazy get put CnvTfsString.</p>
   * @return requested CnvTfsString
   * @throws Exception - an exception
   */
  protected final CnvTfsString
    lazyGetCnvTfsString() throws Exception {
    CnvTfsString convrt = (CnvTfsString) this.convertersMap
      .get(CnvTfsString.class.getSimpleName());
    if (convrt == null) {
      convrt = new CnvTfsString();
      this.convertersMap.put(CnvTfsString.class.getSimpleName(), convrt);
    }
    return convrt;
  }

  /**
   * <p>Lazy get put CnvTfsObject([Composite class]).</p>
   * @param pClass - composite class
   * @return requested CnvTfsObject([cc])
   * @throws Exception - an exception
   */
  protected final CnvTfsObject
    lazyGetCnvTfsObject(final Class pClass) throws Exception {
    String beanName = getCnvTfsObjectName(pClass);
    @SuppressWarnings("unchecked")
    CnvTfsObject<Object> convrt = (CnvTfsObject<Object>)
      this.convertersMap.get(beanName);
    if (convrt == null) {
      convrt = new CnvTfsObject<Object>();
      convrt.setUtlReflection(getUtlReflection());
      convrt.setFieldsConvertersFatory(this);
      convrt.setFieldConverterNamesHolder(this.fieldConverterNamesHolder);
      convrt.setGettersRapiHolder(this.gettersRapiHolder);
      convrt.setSettersRapiHolder(this.settersRapiHolder);
      convrt.init(pClass);
      this.convertersMap.put(beanName, convrt);
    }
    return convrt;
  }

  /**
   * <p>Get CnvTfsObject name.</p>
   * @param pClass - bean class
   * @return converter name
   */
  protected final String getCnvTfsObjectName(final Class pClass) {
    return CnvTfsObject.class.getSimpleName()
      + pClass.getSimpleName();
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
   * <p>Getter for compositeClasses.</p>
   * @return Set<Class<?>>
   **/
  public final Set<Class<?>> getCompositeClasses() {
    return this.compositeClasses;
  }

  /**
   * <p>Getter for enumsClasses.</p>
   * @return final Set<? extends Enum<?>>
   **/
  public final Set<Class<? extends Enum<?>>> getEnumsClasses() {
    return this.enumsClasses;
  }

  /**
   * <p>Getter for hasLongIdMap.</p>
   * @return Map<Class<? extends IHasId<Long>>, String>
   **/
  public final Map<Class<? extends IHasId<Long>>, String> getHasLongIdMap() {
    return this.hasLongIdMap;
  }

  /**
   * <p>Getter for hasIntegerIdMap.</p>
   * @return Map<Class<? extends IHasId<Integer>>, String>
   **/
  public final Map<Class<? extends IHasId<Integer>>, String>
    getHasIntegerIdMap() {
    return this.hasIntegerIdMap;
  }

  /**
   * <p>Getter for hasCompositeIdMap.</p>
   * @return Map<Class<? extends IHasId<?>>, String>
   **/
  public final Map<Class<? extends IHasId<?>>, String>
    getHasCompositeIdMap() {
    return this.hasCompositeIdMap;
  }

  /**
   * <p>Getter for hasStringIdMap.</p>
   * @return Map<Class<? extends IHasId<String>>, String>
   **/
  public final Map<Class<? extends IHasId<String>>, String>
    getHasStringIdMap() {
    return this.hasStringIdMap;
  }

  /**
   * <p>Getter for fieldConverterNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldConverterNamesHolder() {
    return this.fieldConverterNamesHolder;
  }

  /**
   * <p>Setter for fieldConverterNamesHolder.</p>
   * @param pFieldConverterNamesHolder reference
   **/
  public final void setFieldConverterNamesHolder(
    final IHolderForClassByName<String> pFieldConverterNamesHolder) {
    this.fieldConverterNamesHolder = pFieldConverterNamesHolder;
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
}
