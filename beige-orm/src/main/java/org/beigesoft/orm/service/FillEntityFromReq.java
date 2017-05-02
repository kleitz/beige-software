package org.beigesoft.orm.service;

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
import java.nio.charset.Charset;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IRequestData;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that fill object(entity) from request with fields converters
 * and object filler.
 * According Beige-Web specification #2.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FillEntityFromReq implements IFillerObjectsFrom<IRequestData> {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFactoryAppBeansByClass<IFillerObjectFields<?>> fillersFieldsFactory;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterToFromString<?>>
    convertersFieldsFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Fill entity from pequest.</p>
   * @param T entity type
   * @param pAddParam additional param
   * @param pEntity Entity to fill
   * @param pReq - request
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pAddParam,
    final T pEntity, final IRequestData pReq) throws Exception {
    getLogger().debug(FillEntityFromReq.class,
      "Default charset = " + Charset.defaultCharset());
    @SuppressWarnings("unchecked")
    IFillerObjectFields<T> filler = (IFillerObjectFields<T>)
      this.fillersFieldsFactory.lazyGet(pAddParam, pEntity.getClass());
    for (String fieldName : filler.getFieldsNames()) {
      try {
        String valStr = pReq.getParameter(pEntity.getClass().getSimpleName()
          + "." + fieldName); // standard
        if (valStr != null) { // e.g. Boolean checkbox or none-editable
          String convName = this.fieldConverterNamesHolder.getFor(pEntity
           .getClass(), fieldName);
          getLogger().debug(FillEntityFromReq.class,
            "Try fill field/inClass/converterName/value: " + fieldName + "/"
              + pEntity.getClass().getCanonicalName() + "/" + convName
                + "/" + valStr);
          IConverterToFromString conv = this.convertersFieldsFatory
            .lazyGet(pAddParam, convName);
          Object fieldVal = conv.fromString(pAddParam,  valStr);
          if (fieldVal != null) {
            getLogger().debug(FillEntityFromReq.class,
              "Converted fieldClass/toString: " + fieldVal.getClass()
                .getCanonicalName() + "/" + fieldVal);
          }
          filler.fill(pAddParam, pEntity, fieldVal, fieldName);
        }
      } catch (Exception ex) {
        String msg = "Can't fill field/class: " + fieldName + "/"
          + pEntity.getClass().getCanonicalName();
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG, msg, ex);
      }
    }
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
   * <p>Getter for convertersFieldsFatory.</p>
   * @return IFactoryAppBeansByName<IConverterToFromString<?>>
   **/
  public final IFactoryAppBeansByName<IConverterToFromString<?>>
    getConvertersFieldsFatory() {
    return this.convertersFieldsFatory;
  }

  /**
   * <p>Setter for convertersFieldsFatory.</p>
   * @param pConvertersFieldsFatory reference
   **/
  public final void setConvertersFieldsFatory(
    final IFactoryAppBeansByName<IConverterToFromString<?>>
      pConvertersFieldsFatory) {
    this.convertersFieldsFatory = pConvertersFieldsFatory;
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
}
