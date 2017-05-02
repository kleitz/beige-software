package org.beigesoft.replicator.service;

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
import java.util.Date;
import java.math.BigDecimal;
import java.lang.reflect.Field;

import org.beigesoft.service.IUtilXml;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.UtlReflection;

/**
 * <p>Service to fill a field of replicable/persistable entity.
 * According Beige Replicator specification #1.</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityFieldFillerStd implements ISrvEntityFieldFiller {

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

  /**
   * <p>XML service.</p>
   **/
  private IUtilXml utilXml;

  /**
   * <p>
   * Fill given field of given entity according value represented as
   * string.
   * </p>
   * @param pAddParam additional params
   * @param pEntity Entity.
   * @param pFieldName Field Name
   * @param pFieldStrValue Field value
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pAddParam,
    final Object pEntity, final String pFieldName,
      final String pFieldStrValue) throws Exception {
    Field rField = getUtlReflection().retrieveField(pEntity.getClass(),
      pFieldName);
    rField.setAccessible(true);
    if ("NULL".equals(pFieldStrValue)) {
      rField.set(pEntity, null);
      return;
    }
    boolean isFilled = true;
    try {
     if (rField.getType() == Double.class) {
        rField.set(pEntity, Double.valueOf(pFieldStrValue));
      } else if (rField.getType() == Float.class) {
        rField.set(pEntity, Float.valueOf(pFieldStrValue));
      } else if (rField.getType() == BigDecimal.class) {
        rField.set(pEntity, new BigDecimal(pFieldStrValue));
      } else if (rField.getType() == Date.class) {
        rField.set(pEntity, new Date(Long.parseLong(pFieldStrValue)));
      } else if (Enum.class.isAssignableFrom(rField.getType())) {
        Integer intVal = Integer.valueOf(pFieldStrValue);
        Enum val = null;
        if (intVal != null) {
          val = (Enum) rField.getType().getEnumConstants()[intVal];
        }
        rField.set(pEntity, val);
      } else if (rField.getType() == Boolean.class) {
        rField.set(pEntity, Boolean.valueOf(pFieldStrValue));
      } else if (Integer.class == rField.getType()) {
        rField.set(pEntity, Integer.valueOf(pFieldStrValue));
      } else if (Long.class == rField.getType()) {
        rField.set(pEntity, Long.valueOf(pFieldStrValue));
      } else if (String.class == rField.getType()) {
        String unescaped = this.utilXml.unescapeXml(pFieldStrValue);
        rField.set(pEntity, unescaped);
      } else {
        isFilled = false;
      }
    } catch (Exception ex) {
      throw new ExceptionWithCode(ExceptionWithCode
        .WRONG_PARAMETER, "Can not fill field: " + pEntity + "/" + pFieldName
          + "/" + pFieldStrValue + ", " + ex.getMessage(), ex);
    }
    if (!isFilled) {
      throw new ExceptionWithCode(ExceptionWithCode
        .CONFIGURATION_MISTAKE, "There is no rule to fill field: " + pEntity
          + "/" + pFieldName + "/" + pFieldStrValue);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final UtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final UtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }

  /**
   * <p>Getter for utilXml.</p>
   * @return IUtilXml
   **/
  public final IUtilXml getUtilXml() {
    return this.utilXml;
  }

  /**
   * <p>Setter for utilXml.</p>
   * @param pUtilXml reference
   **/
  public final void setUtilXml(final IUtilXml pUtilXml) {
    this.utilXml = pUtilXml;
  }
}
