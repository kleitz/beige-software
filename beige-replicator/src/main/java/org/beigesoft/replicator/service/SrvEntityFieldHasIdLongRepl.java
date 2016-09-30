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
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

import org.beigesoft.persistable.AHasIdLong;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.UtlReflection;

/**
 * <p>Service to fill a owned entity of replicable entity.
 * Only for database replication!!!</p>
 *
 * @author Yury Demidenko
 */
public class SrvEntityFieldHasIdLongRepl implements ISrvEntityFieldFiller {

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

  /**
   * <p>
   * Fill given field of given entity according value represented as
   * string.
   * </p>
   * @param pEntity Entity.
   * @param pFieldName Field Name
   * @param pFieldStrValue Field value
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Object pEntity, final String pFieldName,
    final String pFieldStrValue,
      final Map<String, ?> pAddParam) throws Exception {
    Field rField = getUtlReflection().retrieveField(pEntity.getClass(),
      pFieldName);
    rField.setAccessible(true);
    if (!AHasIdLong.class.isAssignableFrom(rField.getType())) {
      throw new ExceptionWithCode(ExceptionWithCode
        .CONFIGURATION_MISTAKE, "It's wrong service to fill that field: "
          + pEntity + "/" + pFieldName + "/" + pFieldStrValue);
    }
    if ("NULL".equals(pFieldStrValue)) {
      rField.set(pEntity, null);
      return;
    }
    try {
      @SuppressWarnings("unchecked")
      Constructor constructor = rField.getType().getDeclaredConstructor();
      Object ownedEntity = constructor.newInstance();
      ((AHasIdLong) ownedEntity).setItsId(Long.parseLong(pFieldStrValue));
      rField.set(pEntity, ownedEntity);
    } catch (Exception ex) {
      throw new ExceptionWithCode(ExceptionWithCode
        .WRONG_PARAMETER, "Can not fill field: " + pEntity + "/" + pFieldName
          + "/" + pFieldStrValue + ", " + ex.getMessage(), ex);
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
}
